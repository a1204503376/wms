
package org.nodes.modules.wms.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.auth.provider.ITokenGranter;
import org.nodes.core.auth.provider.TokenGranterBuilder;
import org.nodes.core.auth.provider.TokenParameter;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.entity.UserInfo;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.service.IMenuService;
import org.nodes.core.base.service.IParamService;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.base.vo.PdaMenuVO;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.utils.TokenUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.*;
import org.nodes.wms.core.basedata.vo.SkuVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotValWrapper;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
import org.nodes.wms.core.basedata.wrapper.SkuWrapper;
import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.nodes.wms.core.outstock.so.service.ISoPickService;
import org.nodes.wms.core.outstock.so.vo.MergeTraySkuVo;
import org.nodes.wms.core.stock.core.dto.StockDTO;
import org.nodes.wms.core.stock.core.dto.StockFrozenDTO;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.enums.FrozenTypeEnum;
import org.nodes.wms.core.stock.core.service.ISerialService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.StockVO;
import org.nodes.wms.core.stock.core.wrapper.StockWrapper;
import org.nodes.wms.core.system.entity.*;
import org.nodes.wms.core.system.mapper.UserRegisterMapper;
import org.nodes.wms.core.system.service.IBarcodeRuleService;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.system.service.IUpdateVerService;
import org.nodes.wms.core.system.service.IUserOnlineService;
import org.nodes.wms.core.system.vo.TaskVO;
import org.nodes.wms.core.system.vo.UserOnlineVO;
import org.nodes.wms.core.system.vo.UserRegisterVO;
import org.nodes.wms.core.system.wrapper.TaskWrapper;
import org.nodes.wms.core.system.wrapper.UserOnlineWrapper;
import org.nodes.wms.core.system.wrapper.UserRegisterWrapper;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.system.updateVer.entities.UpdateVer;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.support.Kv;
import org.springblade.core.tool.utils.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springblade.core.cache.constant.CacheConstant.USER_CACHE;


/**
 * ????????????(??????) ?????????
 *
 * @author xujy
 * @since 2020-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA")
@ApiSort(1)
@Api(value = "??????????????????", tags = "??????????????????")
public class ApiPDAController extends BladeController {

	private IWarehouseService warehouseService;
	private IUpdateVerService updateVerService;
	private IStockService stockService;
	private IBarcodeRuleService barcodeRuleService;

	private ISkuIncService skuIncService;
	private ISkuReplaceService skuReplaceService;
	private ISkuService skuService;
	private ISerialService serialService;
	private IUserService userService;
	private IMenuService menuService;
	private IUserOnlineService userOnlineService;
	private final UserRegisterMapper userRegisterMapper;

	private ITaskService taskService;
	private ISoPickService soPickService;
	private IParamService paramService;
	private ILocationService locationService;

	/**
	 * ???????????????????????????(????????????)
	 */

	@PostMapping("/Warehouseselect")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "???????????????", notes = "??????whId")
	public R<List<Warehouse>> Warehouseselect(Warehouse warehouse) {
		QueryWrapper<Warehouse> queryWrapper = Condition.getQueryWrapper(warehouse);
		List<Warehouse> list = warehouseService.list(queryWrapper);
		return R.data(list);
	}

	@ApiLog("PDA-??????????????????")
	@PostMapping("/token")
	@ApiOperation(value = "????????????token", notes = "????????????ID:tenantId,??????:account,??????:password")
	public Kv token(@ApiParam(value = "??????ID", required = true) @RequestParam String tenantId,
					@ApiParam(value = "??????", required = true) @RequestParam(required = false) String username,
					@ApiParam(value = "??????", required = true) @RequestParam(required = false) String password) {
		Kv authInfo = Kv.create();

		String grantType = WebUtil.getRequest().getParameter("grant_type");

		String refreshToken = WebUtil.getRequest().getParameter("refresh_token");

		String userType = Func.toStr(WebUtil.getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE);

		TokenParameter tokenParameter = new TokenParameter();
		tokenParameter.getArgs().set("tenantId", tenantId).set("username", username).set("password", password).set("grantType", grantType).set("refreshToken", refreshToken).set("userType", userType);

		ITokenGranter granter = TokenGranterBuilder.getGranter(grantType);
		UserInfo userInfo = granter.grant(tokenParameter);

		if (userInfo == null || userInfo.getUser() == null) {
			return authInfo.set("error_code", HttpServletResponse.SC_BAD_REQUEST).set("error_description", "???????????????????????????");
		}

		if (Func.isEmpty(userInfo.getRoles())) {
			return authInfo.set("error_code", HttpServletResponse.SC_BAD_REQUEST).set("error_description", "??????????????????????????????");
		}
		return TokenUtil.createAuthInfo(userInfo);
	}

	/**
	 * ????????????
	 *
	 * @return
	 */
	@ApiLog("PDA-????????????")
	@PostMapping("/UpdatePassword")
	@ApiOperation(value = "????????????", notes = "????????????ID:id,?????????:oldPassword,?????????:newPassword,????????????:newPassword1")
	@CacheEvict(cacheNames = {USER_CACHE}, allEntries = true)
	public boolean UpdatePassword(@ApiParam(value = "??????ID", required = true) @RequestParam(required = false) String id,
								  @ApiParam(value = "?????????", required = true) @RequestParam(required = false) String oldPassword,
								  @ApiParam(value = "?????????", required = true) @RequestParam(required = false) String newPassword,
								  @ApiParam(value = "????????????", required = true) @RequestParam(required = false) String newPassword1) {
		try {
			return userService.updatePassword(Long.valueOf(id), oldPassword, newPassword, newPassword1);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * ?????????????????????
	 */
	@ApiLog("PDA-?????????????????????")
	@PostMapping("/UpdateVerDetail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "??????", notes = "??????updateVer")
	public R<UpdateVer> UpdateVerDetail(@RequestBody UpdateVer updateVer) {
		UpdateVer detail = updateVerService.getOne(Condition.getQueryWrapper(updateVer));
		return R.data(detail);
	}


	/**
	 * ????????????????????????????????????
	 */
	@ApiLog("PDA-????????????????????????????????????")
	@PostMapping("/takewayList")
	@ApiOperation(value = "??????", notes = "??????stock")
	public R<IPage<TaskVO>> takewayList(Query query, @RequestParam String orderNo) {
		if (Func.isNotEmpty(orderNo)) {
			List<Task> tasks = taskService.getTakesBybillNo(orderNo);
			Page<TaskVO> page = new Page<>();
			page.setTotal(tasks.size());
			page.setSize(query.getSize());
			page.setCurrent(query.getCurrent());
			Page<TaskVO> taskVOPage = page.setRecords(TaskWrapper.build().listVO(tasks));
			return R.data(taskVOPage);
		}
		return R.data(null);
	}


	/**
	 * ????????????????????????????????????
	 */
	@ApiLog("PDA-????????????????????????????????????")
	@PostMapping("/StockDetail")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "??????", notes = "??????stock")
	public R<StockVO> StockDetail(@RequestBody StockVO stock) {
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		if (stock.getLocation() != null) {
			Location locModel = LocationCache.getByCode(stock.getLocation().getLocCode());
			if (locModel != null) {
				stock.setLocId(locModel.getLocId());
			} else {
				throw new ServiceException("?????????????????????");
			}
		}
		List<Stock> detail = stockService.list(Condition.getQueryWrapper(stock));
		if (detail == null || detail.size() == 0) {
			return R.data(null);
		} else {
			return R.data(StockWrapper.build().entityVO(detail.get(0)));
		}

	}

	/**
	 * ????????????????????????????????????
	 *
	 * @param lpnCode
	 * @return
	 */
	@ApiLog("PDA-????????????????????????????????????")
	@PostMapping("/stockGetLocByLpnCode")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "??????????????????????????????", notes = "??????????????????")
	public R<Location> stockGetLocByLpnCode(@RequestParam String lpnCode) {
//		Stock stock = stockService.singleByLpnCode(lpnCode);
//		if (ObjectUtil.isEmpty(stock)) {
//			return R.data(null);
//		} else {
//			return R.data(LocationCache.getById(stock.getLocId()));
//		}
		return R.data(null);
	}

	/**
	 * ????????????????????????
	 *
	 * @author zx
	 */
	@ApiLog("PDA-????????????????????????")
	@PostMapping("/BarcodeRuleList")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "??????????????????", notes = "??????barcodeRule")
	public R<List<BarcodeRule>> BarcodeRuleList(BarcodeRule barcodeRule) {
		List<BarcodeRule> list = barcodeRuleService.list(Condition.getQueryWrapper(barcodeRule));
		return R.data(list);
	}

	/**
	 * ??????
	 */
	@ApiLog("PDA-??????????????????")
	@PostMapping("/skuDetail")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "??????", notes = "??????sku")
	public R<SkuVO> skuDetail(@RequestBody Sku sku) {
		List<Sku> skuModel = skuService.list(Condition.getQueryWrapper(sku));
		if (skuModel.size() <= 0) throw new ServiceException("?????????????????????");

		SkuVO skuVo = SkuWrapper.build().entityVO(SkuCache.getById(skuModel.get(0).getSkuId()));
//		skuVo.setSkuInstock(skuInstockService.getBySkuId(skuModel.get(0).getSkuId()));
//		skuVo.setSkuOutstock(skuOutstockService.getBySkuId(skuModel.get(0).getSkuId()));
		skuVo.setSkuIncList(skuIncService.listBySkuId(skuModel.get(0).getSkuId()));
		skuVo.setSkuReplaceList(skuReplaceService.listBySkuId(skuModel.get(0).getSkuId()));
		ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
		skuVo.setSkuLot(SkuLotWrapper.build().entityVO(skuLotService.getById(skuVo.getWslId())));
		return R.data(skuVo);
	}

	/**
	 * ??????????????????
	 *
	 * @param sku
	 * @return
	 */
	@ApiLog("PDA-????????????????????????")
	@PostMapping("/skuList")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "??????", notes = "??????sku")
	public R<List<SkuVO>> skuList(@RequestBody Sku sku) {
		List<SkuVO> skuList = SkuWrapper.build().listVO(skuService.list(Condition.getQueryWrapper(sku)));
		if (ObjectUtil.isNotEmpty(skuList)) {
			for (SkuVO skuVo : skuList) {
//				skuVo.setSkuInstock(skuInstockService.getBySkuId(skuVo.getSkuId()));
//				skuVo.setSkuOutstock(skuOutstockService.getBySkuId(skuVo.getSkuId()));
				skuVo.setSkuIncList(skuIncService.listBySkuId(skuVo.getSkuId()));
				skuVo.setSkuReplaceList(skuReplaceService.listBySkuId(skuVo.getSkuId()));
				ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
				skuVo.setSkuLot(SkuLotWrapper.build().entityVO(skuLotService.getById(skuVo.getWslId())));
				ISkuLotValService skuLotValService = SpringUtil.getBean(ISkuLotValService.class);
				skuVo.setSkuLotVal(SkuLotValWrapper.build().entityVO(skuLotValService.getById(skuVo.getWslvId())));
			}
		}
		return R.data(skuList);
	}

	@ApiLog("PDA-??????????????????????????????")
	@PostMapping("/getSkuBaseUm")
	public R<String> getSkuBaseUm(@RequestBody Sku sku) {
		Sku one = skuService.getOne(Wrappers.lambdaQuery(Sku.class).eq(Sku::getSkuCode, sku.getSkuCode()));
		if (Func.isEmpty(one)) {
			throw new ServiceException("???????????????????????????");
		}
		SkuPackageDetail one1 = SkuPackageDetailCache.getOne(one.getWspId(), SkuLevelEnum.Base.getIndex());
		return R.data(one1.getWsuName());
	}

	/**
	 * ???????????????????????????????????????
	 *
	 * @param mergeTraySkuVo
	 * @param sqlSkuLotMap
	 * @return
	 */
	private BigDecimal getBigDecimal(MergeTraySkuVo mergeTraySkuVo, Map<String, String> sqlSkuLotMap) {
		BigDecimal reduce = soPickService.list(Wrappers.lambdaQuery(SoPick.class)
			.in(SoPick::getSoBillId, Func.toLongList(mergeTraySkuVo.getSoBillId()))
			.eq(SoPick::getSkuCode, mergeTraySkuVo.getSkuCode())
			.func(sql -> {
				sqlSkuLotMap.forEach((key, value) -> {
					sql.apply(key + "='" + value + "'");
				});
			})
		).stream().map(SoPick::getPickRealQty).reduce(BigDecimal.ZERO, BigDecimal::add);
		return reduce;
	}
	/**
	 * ????????????ID??????????????????????????????????????????
	 *
	 * @param skuId ??????ID
	 * @return
	 */
//	@PostMapping("/skuLotList")
//	@ApiOperationSupport(order = 5)
//	@ApiOperation(value = "???????????????", notes = "??????skuId")
//	public R<List<SkuLotAndLotValVO>> skuLotList(@ApiParam(value = "??????", required = true)String skuId) {
//		//???????????????
//		List<SkuLotAndLotValVO> skuConfigs = new ArrayList<>();
//		try {
//			skuConfigs = skuLotService.getSkuConfig(Long.parseLong(skuId));
//		} catch (IllegalAccessException e) {
//			new ServiceException(String.format("??????????????????????????????????????????"));
//
//		}
//		return R.data(skuConfigs);
//	}

	/**
	 * PDA-?????????????????????
	 */
	@ApiLog("PDA-?????????????????????")
	@PostMapping("/frozenForSku")
	@ApiOperation(value = "?????????????????????", notes = "??????stock")
	public R frozenForSku(@Valid @RequestBody StockFrozenDTO stockFrozenDTO) {
		if (stockFrozenDTO.getType().equals(FrozenTypeEnum.LOC.getValue())) {
			Location location= LocationCache.getByCode(stockFrozenDTO.getQueryKey());
			if (Func.isNotEmpty(location)) {
				if (StringPool.N.toUpperCase().equals(location.getLockFlag())) {
					locationService.lockById(location.getLocId().toString(), null);
				} else {
					locationService.lockById(location.getLocId().toString(), StringPool.N.toUpperCase());
				}
				List<Stock> list = stockService.list(Wrappers.lambdaQuery(Stock.class)
					.eq(Stock::getLocId, location.getLocId().toString()));
				String stockId = NodesUtil.join(list, "stockId");
				stockService.lockByStock(Func.toLongList(stockId), stockFrozenDTO.getFlag());
			} else {
				return R.fail("??????????????????");
			}
			return R.status(true);
		} else if (stockFrozenDTO.getType().equals(FrozenTypeEnum.SINGLE.getValue())) {
			return R.data(stockService.lock(Func.toLongList(stockFrozenDTO.getQueryKey())));
		} else {
			stockService.lockByStock(Func.toLongList(stockFrozenDTO.getQueryKey()), stockFrozenDTO.getFlag());
			return R.status(true);
		}
	}

	/**
	 * PDA-????????????
	 *
	 * @param stockDTO ????????????
	 * @param query    ????????????
	 * @param type     ??????????????????1????????????0???????????????
	 * @return
	 */
	@ApiLog("PDA-????????????")
	@PostMapping("/StockQuery")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "????????????", notes = "??????stock")
	public R<IPage<StockVO>> StockQuery(@RequestBody StockDTO stockDTO, Query query, @RequestParam String type) {
		if (StringPool.ONE.equals(type)) {
			//List<Sku> skuList = SkuCache.listByCode(stockDTO.getSkuCode());
			ISkuService skuService = SpringUtil.getBean(ISkuService.class);
			List<Sku> skuList = skuService.list(Condition.getQueryWrapper(new Sku())
				.lambda()
				.eq(Sku::getSkuCode, stockDTO.getSkuCode())
			).stream().collect(Collectors.toList());
			QueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock());
			if (Func.isNotEmpty(skuList)) {
				queryWrapper.lambda().in(Stock::getSkuId, NodesUtil.toList(skuList, Sku::getSkuId));
			} else {
				return R.data(null);
			}
			// ??????????????????
			String boxCodeConfig = ParamCache.getValue(ParamEnum.PDA_STOCK_QUERY_BOXCODE.getKey());
			if (Func.isNotEmpty(boxCodeConfig)) {
				List<String> boxCodeConfigList = Func.toStrList(boxCodeConfig);
				List<String> collect = boxCodeConfigList.stream().map(str -> str.toLowerCase().replaceAll(" ", "")).collect(Collectors.toList());
				List<Field> fieldList = ReflectionKit.getFieldList(Stock.class);
				for (Field field : fieldList) {
					if (!collect.contains(field.getName().toLowerCase())) {
						continue;
					}
					field.setAccessible(true);
					String columnName = StringUtil.humpToUnderline(field.getName());
					queryWrapper.eq(columnName, ReflectionKit.getFieldValue(stockDTO, field.getName()));
				}
			}

			IPage<Stock> pages = stockService.page(Condition.getPage(query), queryWrapper);
			if (pages.getTotal() <= 0)
				throw new ServiceException("????????????");
			List<Stock> list = stockService.list(queryWrapper);
			List<StockVO> stockVOS = StockWrapper.build().listVO(list);
			IPage<StockVO> stockVOIPage = StockWrapper.build().pageVO(pages);
			BigDecimal reduce = stockVOS.stream()
				.map(StockVO::getAvailableQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
			stockVOIPage.getRecords().forEach(stockVO -> stockVO.setTotalQty(reduce.stripTrailingZeros().toPlainString()));
			return R.data(stockVOIPage);
		} else {
			LambdaQueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock()).lambda();
			//?????????????????????
			if (Func.isNotEmpty(stockDTO.getSerialNumber())) {
				Serial serial = serialService.getOne(Condition.getQueryWrapper(new Serial())
					.lambda()
					.eq(Serial::getSerialNumber, stockDTO.getSerialNumber()));
				if (serial != null)
					queryWrapper.eq(Stock::getStockId, serial.getStockId());
				else
					throw new ServiceException("????????????????????????");
			}

			if (Func.isNotEmpty(stockDTO.getLocCode())) {
				/*List<Location> locationList = LocationCache.list().stream().filter(u -> {
					return u.getLocCode().indexOf(stockDTO.getLocCode()) > -1;
				}).collect(Collectors.toList());*/
				ILocationService locationService = SpringUtil.getBean(ILocationService.class);
				List<Location> locationList = locationService.list(Condition.getQueryWrapper(new Location())
					.lambda()
					.like(Location::getLocCode, stockDTO.getLocCode()));
				if (Func.isNotEmpty(locationList)) {
					queryWrapper.in(Stock::getLocId, NodesUtil.toList(locationList, Location::getLocId));
				} else {
					throw new ServiceException("??????????????????");
				}
			}
			if (Func.isNotEmpty(stockDTO.getSkuCode())) {
				ISkuService skuService = SpringUtil.getBean(ISkuService.class);
				List<Sku> skuList = skuService.list(Condition.getQueryWrapper(new Sku()).lambda()
					.like(Sku::getSkuCode, stockDTO.getSkuCode()));
				if (Func.isNotEmpty(skuList)) {
					queryWrapper.in(Stock::getSkuId, NodesUtil.toList(skuList, Sku::getSkuId));
				}
			}
			if (Func.isNotEmpty(stockDTO.getLotNumber())) {
				queryWrapper.eq(Stock::getLotNumber, stockDTO.getLotNumber());
			}
			IPage<Stock> pages = stockService.page(Condition.getPage(query), queryWrapper);
			if (pages.getTotal() <= 0)
				throw new ServiceException("????????????");
			List<Stock> list = stockService.list(queryWrapper);
			List<StockVO> stockVOS = StockWrapper.build().listVO(list);
			IPage<StockVO> stockVOIPage = StockWrapper.build().pageVO(pages);
			BigDecimal reduce = stockVOS.stream()
				.map(StockVO::getAvailableQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
			stockVOIPage.getRecords().forEach(stockVO -> stockVO.setTotalQty(reduce.stripTrailingZeros().toPlainString()));
			return R.data(stockVOIPage);
		}
	}

	/**
	 * ??????????????????
	 */
	@ApiLog("PDA-??????????????????")
	@GetMapping("/routes")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "PDA????????????", notes = "PDA????????????")
	public R<List<PdaMenuVO>> routes(BladeUser user, Long topMenuId) {
		List<PdaMenuVO> list = menuService.routesPDA((user == null) ? null : user.getRoleId(), topMenuId);
		return R.data(list);
	}

	/**
	 * ????????????????????????
	 */
	@ApiLog("PDA-????????????????????????")
	@GetMapping("/userOnlineList")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "??????", notes = "??????userOnline")
	public R<List<UserOnlineVO>> userOnlineList(UserOnline userOnline) {
		BladeUser user = AuthUtil.getUser();
		userOnline.setUserId(user.getUserId());
		List<UserOnline> list = userOnlineService.list(Condition.getQueryWrapper(userOnline));
		return R.data(UserOnlineWrapper.build().listVO(list));
	}

	/**
	 * ??????????????????????????????
	 */
	@ApiLog("PDA-??????????????????????????????")
	@GetMapping("/userRegisterList")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "??????", notes = "??????userRegister")
	public R<List<UserRegister>> userRegisterList() {
		BladeUser user = AuthUtil.getUser();
		List<UserRegister> pages = userRegisterMapper.selectUserRegisterToday(user.getUserId());
		return R.data(pages);
	}

	/**
	 * ????????????????????????
	 */
	@ApiLog("PDA-????????????????????????")
	@GetMapping("/userRegisterListMoth")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "??????", notes = "??????userRegister")
	public R<List<UserRegisterVO>> userRegisterListMoth(String time) {
		BladeUser user = AuthUtil.getUser();
		List<UserRegisterVO> pages = UserRegisterWrapper.build().entityVODay(userRegisterMapper.selectUserRegisterTomonth(user.getUserId(), time));
		return R.data(pages);
	}

	/**
	 * ???????????????????????????
	 */
	@ApiLog("PDA-????????????")
	@PostMapping("/userOnlinesubmit")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "???????????????", notes = "??????userRegister")
	public R<UserRegisterVO> userOnlinesubmit(@Valid @RequestBody UserOnline userOnline, HttpServletRequest request) {
		userOnline.setIpAddress(WebUtil.getIP(request));
		UserRegisterVO userRegisterVO = userOnlineService.sign(userOnline);
		return R.data(userRegisterVO);
	}

	/**
	 * ??????
	 */
	@ApiLog("PDA-??????????????????")
	@GetMapping("/ParamList")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "??????", notes = "??????param")

	public R<List<Param>> ParamList() {
		IParamService paramService = SpringUtil.getBean(IParamService.class);
		return R.data(paramService.list());
	}
}
