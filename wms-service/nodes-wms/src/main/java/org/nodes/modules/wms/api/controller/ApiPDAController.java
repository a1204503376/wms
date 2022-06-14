
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
import org.nodes.core.base.vo.MenuVO;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.utils.TokenUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
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
import org.nodes.wms.core.stock.core.entity.Stock;
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
 * 公共接口(手持) 控制器
 *
 * @author xujy
 * @since 2020-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA")
@ApiSort(1)
@Api(value = "手持公共接口", tags = "手持公共接口")
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
	 * 下拉数据源手持版本(测试勿删)
	 */

	@PostMapping("/Warehouseselect")
	@ApiOperationSupport(order = 1)
	@ApiLog("PDA-仓库列表框数据源")
	@ApiOperation(value = "下拉数据源", notes = "传入whId")
	public R<List<Warehouse>> Warehouseselect(Warehouse warehouse) {
		QueryWrapper<Warehouse> queryWrapper = Condition.getQueryWrapper(warehouse);
		List<Warehouse> list = warehouseService.list(queryWrapper);
		return R.data(list);
	}

	@ApiLog("PDA-登录用户验证")
	@PostMapping("/token")
	@ApiOperation(value = "获取认证token", notes = "传入租户ID:tenantId,账号:account,密码:password")
	public Kv token(@ApiParam(value = "租户ID", required = true) @RequestParam String tenantId,
					@ApiParam(value = "账号", required = true) @RequestParam(required = false) String username,
					@ApiParam(value = "密码", required = true) @RequestParam(required = false) String password) {
		Kv authInfo = Kv.create();

		String grantType = WebUtil.getRequest().getParameter("grant_type");

		String refreshToken = WebUtil.getRequest().getParameter("refresh_token");

		String userType = Func.toStr(WebUtil.getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE);

		TokenParameter tokenParameter = new TokenParameter();
		tokenParameter.getArgs().set("tenantId", tenantId).set("username", username).set("password", password).set("grantType", grantType).set("refreshToken", refreshToken).set("userType", userType);

		ITokenGranter granter = TokenGranterBuilder.getGranter(grantType);
		UserInfo userInfo = granter.grant(tokenParameter);

		if (userInfo == null || userInfo.getUser() == null) {
			return authInfo.set("error_code", HttpServletResponse.SC_BAD_REQUEST).set("error_description", "用户名或密码不正确");
		}

		if (Func.isEmpty(userInfo.getRoles())) {
			return authInfo.set("error_code", HttpServletResponse.SC_BAD_REQUEST).set("error_description", "未获得用户的角色信息");
		}
		return TokenUtil.createAuthInfo(userInfo);
	}

	/**
	 * 修改密码
	 *
	 * @return
	 */
	@ApiLog("PDA-修改密码")
	@PostMapping("/UpdatePassword")
	@ApiOperation(value = "修改密码", notes = "传入用户ID:id,原密码:oldPassword,新密码:newPassword,确认密码:newPassword1")
	@CacheEvict(cacheNames = {USER_CACHE}, allEntries = true)
	public boolean UpdatePassword(@ApiParam(value = "用户ID", required = true) @RequestParam(required = false) String id,
								  @ApiParam(value = "原密码", required = true) @RequestParam(required = false) String oldPassword,
								  @ApiParam(value = "新密码", required = true) @RequestParam(required = false) String newPassword,
								  @ApiParam(value = "确认密码", required = true) @RequestParam(required = false) String newPassword1) {
		try {
			return userService.updatePassword(Long.valueOf(id), oldPassword, newPassword, newPassword1);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * 获取版本号详情
	 */
	@ApiLog("PDA-获取版本号详情")
	@PostMapping("/UpdateVerDetail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入updateVer")
	public R<UpdateVer> UpdateVerDetail(@RequestBody UpdateVer updateVer) {
		UpdateVer detail = updateVerService.getOne(Condition.getQueryWrapper(updateVer));
		return R.data(detail);
	}


	/**
	 * 根据订单编号查询拣货任务
	 */
	@ApiLog("PDA-根据订单编号查询拣货任务")
	@PostMapping("/takewayList")
	@ApiOperation(value = "详情", notes = "传入stock")
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
	 * 根据容器编码获取库存库位
	 */
	@ApiLog("PDA-根据容器编码获取库存库位")
	@PostMapping("/StockDetail")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "详情", notes = "传入stock")
	public R<StockVO> StockDetail(@RequestBody StockVO stock) {
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		if (stock.getLocation() != null) {
			Location locModel = LocationCache.getByCode(stock.getLocation().getLocCode());
			if (locModel != null) {
				stock.setLocId(locModel.getLocId());
			} else {
				throw new ServiceException("当前库位不存在");
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
	 * 根据容器编码获取库存库位
	 *
	 * @param lpnCode
	 * @return
	 */
	@ApiLog("PDA-根据容器编码获取库存库位")
	@PostMapping("/stockGetLocByLpnCode")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "根据容器编码获取库存", notes = "传入容器编码")
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
	 * 条码规则列表查询
	 *
	 * @author zx
	 */
	@ApiLog("PDA-条码规则列表查询")
	@PostMapping("/BarcodeRuleList")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "条码规则列表", notes = "传入barcodeRule")
	public R<List<BarcodeRule>> BarcodeRuleList(BarcodeRule barcodeRule) {
		List<BarcodeRule> list = barcodeRuleService.list(Condition.getQueryWrapper(barcodeRule));
		return R.data(list);
	}

	/**
	 * 详情
	 */
	@ApiLog("PDA-获取物品详情")
	@PostMapping("/skuDetail")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "详情", notes = "传入sku")
	public R<SkuVO> skuDetail(@RequestBody Sku sku) {
		List<Sku> skuModel = skuService.list(Condition.getQueryWrapper(sku));
		if (skuModel.size() <= 0) throw new ServiceException("当前物品不存在");

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
	 * 物品列表查询
	 *
	 * @param sku
	 * @return
	 */
	@ApiLog("PDA-获取物品列表数据")
	@PostMapping("/skuList")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "列表", notes = "传入sku")
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

	@ApiLog("PDA-获取物品基本计量单位")
	@PostMapping("/getSkuBaseUm")
	public R<String> getSkuBaseUm(@RequestBody Sku sku) {
		Sku one = skuService.getOne(Wrappers.lambdaQuery(Sku.class).eq(Sku::getSkuCode, sku.getSkuCode()));
		if (Func.isEmpty(one)) {
			throw new ServiceException("系统中没有该物品！");
		}
		SkuPackageDetail one1 = SkuPackageDetailCache.getOne(one.getWspId(), SkuLevelEnum.Base.getIndex());
		return R.data(one1.getWsuName());
	}

	/**
	 * 求和客户下物料实际拣货数量
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
	 * 根据物品ID获取当前物品的批属性以及验证
	 *
	 * @param skuId 物品ID
	 * @return
	 */
//	@PostMapping("/skuLotList")
//	@ApiOperationSupport(order = 5)
//	@ApiOperation(value = "批属性列表", notes = "传入skuId")
//	public R<List<SkuLotAndLotValVO>> skuLotList(@ApiParam(value = "账号", required = true)String skuId) {
//		//批属性规则
//		List<SkuLotAndLotValVO> skuConfigs = new ArrayList<>();
//		try {
//			skuConfigs = skuLotService.getSkuConfig(Long.parseLong(skuId));
//		} catch (IllegalAccessException e) {
//			new ServiceException(String.format("当前物品的批属性规则获取失败"));
//
//		}
//		return R.data(skuConfigs);
//	}

	/**
	 * PDA-库位或库存冻结
	 */
	@ApiLog("PDA-库位或库存冻结")
	@PostMapping("/frozenForSku")
	@ApiOperation(value = "库位或库存冻结", notes = "传入stock")
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
				return R.fail("库位不存在！");
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
	 * PDA-库存查询
	 *
	 * @param stockDTO 查询条件
	 * @param query    分页信息
	 * @param type     是否为箱码（1：箱码，0：非箱码）
	 * @return
	 */
	@ApiLog("PDA-库存查询")
	@PostMapping("/StockQuery")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "库存查询", notes = "传入stock")
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
			// 获取箱码规则
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
				throw new ServiceException("没有库存");
			List<Stock> list = stockService.list(queryWrapper);
			List<StockVO> stockVOS = StockWrapper.build().listVO(list);
			IPage<StockVO> stockVOIPage = StockWrapper.build().pageVO(pages);
			BigDecimal reduce = stockVOS.stream()
				.map(StockVO::getAvailableQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
			stockVOIPage.getRecords().forEach(stockVO -> stockVO.setTotalQty(reduce.stripTrailingZeros().toPlainString()));
			return R.data(stockVOIPage);
		} else {
			LambdaQueryWrapper<Stock> queryWrapper = Condition.getQueryWrapper(new Stock()).lambda();
			//根据序列号查询
			if (Func.isNotEmpty(stockDTO.getSerialNumber())) {
				Serial serial = serialService.getOne(Condition.getQueryWrapper(new Serial())
					.lambda()
					.eq(Serial::getSerialNumber, stockDTO.getSerialNumber()));
				if (serial != null)
					queryWrapper.eq(Stock::getStockId, serial.getStockId());
				else
					throw new ServiceException("当前序列号不存在");
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
					throw new ServiceException("库位不存在！");
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
				throw new ServiceException("没有库存");
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
	 * 前端菜单数据
	 */
	@ApiLog("PDA-前端菜单数据")
	@GetMapping("/routes")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "PDA菜单数据", notes = "PDA菜单数据")
	public R<List<MenuVO>> routes(BladeUser user, Long topMenuId) {
		List<MenuVO> list = menuService.routesPDA((user == null) ? null : user.getRoleId(), topMenuId);
		return R.data(list);
	}

	/**
	 * 获取在线用户列表
	 */
	@ApiLog("PDA-获取在线用户列表")
	@GetMapping("/userOnlineList")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "列表", notes = "传入userOnline")
	public R<List<UserOnlineVO>> userOnlineList(UserOnline userOnline) {
		BladeUser user = AuthUtil.getUser();
		userOnline.setUserId(user.getUserId());
		List<UserOnline> list = userOnlineService.list(Condition.getQueryWrapper(userOnline));
		return R.data(UserOnlineWrapper.build().listVO(list));
	}

	/**
	 * 获取当天签到日志列表
	 */
	@ApiLog("PDA-获取当天签到日志列表")
	@GetMapping("/userRegisterList")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "列表", notes = "传入userRegister")
	public R<List<UserRegister>> userRegisterList() {
		BladeUser user = AuthUtil.getUser();
		List<UserRegister> pages = userRegisterMapper.selectUserRegisterToday(user.getUserId());
		return R.data(pages);
	}

	/**
	 * 获取签到日志列表
	 */
	@ApiLog("PDA-获取签到日志列表")
	@GetMapping("/userRegisterListMoth")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "列表", notes = "传入userRegister")
	public R<List<UserRegisterVO>> userRegisterListMoth(String time) {
		BladeUser user = AuthUtil.getUser();
		List<UserRegisterVO> pages = UserRegisterWrapper.build().entityVODay(userRegisterMapper.selectUserRegisterTomonth(user.getUserId(), time));
		return R.data(pages);
	}

	/**
	 * 在线人员新增或修改
	 */
	@ApiLog("PDA-人员签到")
	@PostMapping("/userOnlinesubmit")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "新增或修改", notes = "传入userRegister")
	public R<UserRegisterVO> userOnlinesubmit(@Valid @RequestBody UserOnline userOnline, HttpServletRequest request) {
		userOnline.setIpAddress(WebUtil.getIP(request));
		UserRegisterVO userRegisterVO = userOnlineService.sign(userOnline);
		return R.data(userRegisterVO);
	}

	/**
	 * 分页
	 */
	@ApiLog("PDA-参数分页列表")
	@GetMapping("/ParamList")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "分页", notes = "传入param")

	public R<List<Param>> ParamList() {
		IParamService paramService = SpringUtil.getBean(IParamService.class);
		return R.data(paramService.list());
	}
}
