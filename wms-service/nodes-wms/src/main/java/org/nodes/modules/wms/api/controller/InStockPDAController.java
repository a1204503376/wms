package org.nodes.modules.wms.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.validation.Add;
import org.nodes.core.tool.validation.Pda;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.instock.asn.dto.*;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.entity.AsnLpnDetail;
import org.nodes.wms.core.instock.asn.entity.ContainerLog;
import org.nodes.wms.core.instock.asn.entity.Sn;
import org.nodes.wms.core.instock.asn.service.*;
import org.nodes.wms.core.instock.asn.vo.*;
import org.nodes.wms.core.instock.asn.wrapper.AsnHeaderWrapper;
import org.nodes.wms.core.instock.asn.wrapper.ContainerLogWrapper;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 入库(手持) 控制器
 *
 * @author xujy
 * @since 2020-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA/InStockPDA")
@ApiSort(1)
@Api(value = "手持入库接口", tags = "手持入库接口")
public class InStockPDAController extends BladeController {
	//收货单头表服务层接口
	private IAsnHeaderService asnHeaderService;
	//序列号接口
	private ISnService snService;
	//容器明细接口
	private IAsnLpnDetailService asnLpnDetailService;
	//按箱收货
	private IAsnByBoxService asnByBoxService;
	//按箱上架
	private IPutawayByBoxService putawayByBoxService;

	private IContainerLogService containerLogService;

	private ISkuLotService skuLotService;

	/**
	 * 模糊查询收货单头表列表
	 */
	@ApiLog("PDA-模糊查询收货单头表列表")
	@PostMapping("/AsnHeaderList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询收货单头表列表", notes = "传入单据编号")
	public R<List<AsnHeaderMinVO>> AsnHeaderList(@RequestBody AsnHeader asnHeader) {
		List<AsnHeaderMinVO> result = new ArrayList<>();
		List<AsnHeader> list = asnHeaderService.listForPDA(asnHeader);
		for (AsnHeader header : list) {
			AsnHeaderMinVO vo = AsnHeaderWrapper.toMinVO(header);
			vo.setAsnDetailMinVO(asnHeaderService.getFinishAsnDetail(header.getAsnBillId()));
			result.add(vo);
		}
		if (result.size() == 0) {
			String msg = String.format("收货单[%s]不存在或者已完成", asnHeader.getAsnBillNo());
			throw new ServiceException(msg);
		}
		return R.data(result);
	}

	/**
	 * 通过收货单编号查询详情
	 *
	 * @return
	 */
	@ApiLog("PDA-通过收货单编号查询收货单详情")
	@PostMapping("/getAsnHeaderDetail")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "通过收货单编号查询详情", notes = "传入收货单编号,物品编号,包装ID")
	public R<Map<String, Object>> getAsnHeaderDetail(@NotEmpty @RequestParam String asnBillNo,
													 @NotEmpty @RequestParam String skuCode/*,
	                                                 @NotEmpty @RequestParam String wspId*/) {
		return R.data(asnHeaderService.getAsnHeaderDetail(asnBillNo, skuCode/*, wspId*/));
	}

	/**
	 * 手持-按件收货-手持提交
	 *
	 * @param dto
	 * @return
	 */
	@ApiLog("PDA-按件收货")
	@PostMapping(value = "/AsnHeaderSubmitAsnHeader")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "收货单提交", notes = "传入AsnDetailInstockDTO")
	public R<SubmitAsnHeaderVO> AsnHeaderSubmitAsnHeader(@Valid @RequestBody AsnDTO dto) {
		return R.data(asnHeaderService.submitAsnHeader(dto));
	}

	/**
	 * 入库货品序列号
	 */
	@ApiLog("PDA-入库货品序列号")
	@PostMapping("/SnListInfo")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "", notes = "传入sn")
	public R<List<Sn>> SnlistInfo(@RequestBody SnVO snVO) {
//		List<Sn> snInfo = snService.list(Condition.getQueryWrapper(sn));
		return R.data(snService.selectSnListBySku(snVO));
	}

	/**
	 * 整托上架获得物品信息
	 */
	@ApiLog("PDA-整托上架获得物品信息")
	@PostMapping(value = "/AsnHeaderQueryStockByLpnCode")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "整托上架获得物品信息", notes = "传入容器编码")
	public R AsnHeaderQueryStockByLpnCode(InStockSubmitVO inStockSubmitVO) {
		return R.data(asnHeaderService.queryStockByLpnCode(inStockSubmitVO));
	}

	/**
	 * 按托上架提交
	 *
	 * @param inStockSubmitVO
	 * @return
	 */
	@ApiLog("PDA-按托上架提交")
	@PostMapping(value = "/AsnHeaderSubmitPutaway")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "按托上架提交", notes = "传入stock")
	public R AsnHeaderSubmitPutaway(@Valid InStockSubmitVO inStockSubmitVO) {
		return R.status(asnHeaderService.submitPutaway(inStockSubmitVO));
	}

	/**
	 * 批量按托上架提交
	 *
	 * @param putawayByTranSubmitVO
	 * @return
	 */
	@ApiLog("PDA-批量按托上架提交")
	@PostMapping(value = "/submitPutawayNew")
	@ApiOperation(value = "批量按托上架提交", notes = "传入stock")
	public R submitPutawayNew(@Valid @RequestBody PutawayByTranSubmitVO putawayByTranSubmitVO) {
		return R.status(asnHeaderService.submitPutawayNew(putawayByTranSubmitVO));
	}

	/**
	 * 通过收货单编号查询详情
	 *
	 * @return
	 */
	@ApiLog("PDA-通过收货单编号查询详情")
	@PostMapping("/getAsnLpnDetail")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "通过托盘LPN查询详情", notes = "传入托盘LPN")
	public R<Map<String, Object>> getAsnLpnDetail(@RequestBody AsnLpnDetail asnLpnDetail) {
		return R.data(asnLpnDetailService.getAsnLpnDetail(asnLpnDetail));
	}

	/**
	 * 按托收货提交
	 */
	@ApiLog("PDA-按托收货提交")
	@PostMapping("/stockAddByLPN")
	@ApiOperationSupport(order = 15)
	@ApiOperation(value = "按托收货提交", notes = "传入locCode")
	public R<Map<String, Object>> stockAddByLPN(@RequestBody AsnDTO asnDTO) {
		return R.data(asnLpnDetailService.submitAsnLpn(asnDTO));
	}

	/**
	 * 按托收货提交
	 */
	@ApiLog("PDA-按托收货提交")
	@PostMapping("/submitAsnHeaderForTray")
	@ApiOperation(value = "按托收货提交")
	public R submitAsnHeaderForTray(@RequestBody AsnDTO asnDTO) {
		asnHeaderService.submitAsnTray(asnDTO);
		return R.success("");
	}

	/**
	 * 收货序列号验证
	 */
	@ApiLog("PDA-收货序列号验证")
	@GetMapping("/isHasSerial")
	@ApiOperationSupport(order = 16)
	@ApiOperation(value = "收货序列号验证", notes = "传入单据明细ID，序列号")
	public R isHasSerial(AsnDTO asnDTO) {
		return R.status(asnHeaderService.instockHasSerial(asnDTO));
	}

	/**
	 * 按单收货提交
	 */
	@ApiLog("PDA-按单收货提交")
	@PostMapping("/submitAsnHeaderWithOrder")
	@ApiOperation(value = "按单收货提交", notes = "传入单据编号")
	public R submitAsnHeaderWithOrder(@Valid @RequestBody AsnHeaderOrderDto asnHeaderOrderDto) {
		asnHeaderService.submitAsnHeaderWithOrder(asnHeaderOrderDto);
		return R.status(true);
	}

	/**
	 * 按单收货获取订单详情
	 */
	@ApiLog("PDA-按单收货获取订单详情")
	@GetMapping("/getAsnHeaderAndDetails")
	@ApiOperation(value = "按单收货获取订单详情", notes = "传入单据编号")
	@ApiImplicitParams(@ApiImplicitParam(name = "asnBillNo", dataType = "String", paramType = "query"))
	public R getAsnHeaderAndDetails(@NotEmpty(message = "订单号不能为空！") @RequestParam String asnBillNo) {
		AsnHeaderVO asnHeader = asnHeaderService.getAsnHeaderAndDetails(asnBillNo);
		return R.data(asnHeader);
	}

	/**
	 * 获取按箱收货的单据信息
	 */
	@ApiLog("PDA-获取按箱收货的单据信息")
	@GetMapping("/getAsnHeaderInfoForBox")
	@ApiOperationSupport(order = 17)
	@ApiOperation(value = "获取按箱收货的单据列表", notes = "传入单据编号")
	@ApiImplicitParams(@ApiImplicitParam(name = "asnBillNo", dataType = "String", paramType = "query"))
	public R getAsnHeaderListByBox(@ApiIgnore AsnHeader asnHeader) {
		AsnHeaderVO result = asnByBoxService.getAsnHeader(asnHeader);
		return R.data(result);
	}


	/**
	 * 按箱收货获取物品详情
	 *
	 * @return
	 */
	@ApiLog("PDA-按箱收货获取物品详情")
	@PostMapping("/getAsnHeaderDetailForBox")
	@ApiOperationSupport(order = 18)
	@ApiOperation(value = "按箱收货获取物品详情", notes = "传入收货单编号,物品编号")
	public R<AsnHeaderBoxQueryVO> getAsnHeaderDetailForBox(
		@Valid @RequestBody AsnHeaderBoxQueryDTO asnHeaderBoxQueryDTO) {

		return R.data(asnByBoxService.getAsnHeaderDetail(asnHeaderBoxQueryDTO));
	}

	/**
	 * 复制标签根据扫描物品箱码信息查询物品批属性对应名称
	 *
	 * @return
	 */
	@ApiLog("PDA-复制标签根据扫描物品箱码信息查询物品批属性对应名称")
	@PostMapping("/getLabelInfoWithStock")
	@ApiOperationSupport(order = 18)
	@ApiOperation(value = "按箱收货获取物品详情", notes = "传入收货单编号,物品编号")
	public R<AsnHeaderBoxQueryVO> getLabelInfoWithStock(
		@RequestBody AsnHeaderBoxQueryDTO asnHeaderBoxQueryDTO) {
		AsnHeaderBoxQueryVO asnHeaderBoxQueryVO = new AsnHeaderBoxQueryVO();
		if (Func.isNotEmpty(asnHeaderBoxQueryDTO.getSkuCode())) {
			//List<Sku> skus = SkuCache.listByCode(asnHeaderBoxQueryDTO.getSkuCode());
			ISkuService skuService = SpringUtil.getBean(ISkuService.class);
			List<Sku> skus = skuService.list(Condition.getQueryWrapper(new Sku())
				.lambda()
				.eq(Sku::getSkuCode, asnHeaderBoxQueryDTO.getSkuCode())
			).stream().collect(Collectors.toList());
			if (Func.isNotEmpty(skus)) {
				//批属性规则
				List<SkuLotConfigVO> skuConfigs = new ArrayList<>();
				skuConfigs = skuLotService.getConfig(skus.get(0).getSkuId());
				asnHeaderBoxQueryVO.setSkuConfig(skuConfigs);
			}
		}
		return R.data(asnHeaderBoxQueryVO);
	}

	/**
	 * 手持-按箱收货-提交
	 *
	 * @param asnByBoxSubmitDTO
	 * @return
	 */
	@ApiLog("PDA-按箱收货")
	@PostMapping(value = "/submitAsnHeaderForBox")
	@ApiOperationSupport(order = 19)
	@ApiOperation(value = "按箱收货提交", notes = "传入物品参数")
	public R<AsnByBoxSubmitVO> submitAsnHeaderForBox(@RequestBody AsnByBoxSubmitDTO asnByBoxSubmitDTO) {
//		return R.data(asnByBoxService.submitAsnHeader(boxSubmit));
		return R.data(asnByBoxService.submit(asnByBoxSubmitDTO));
	}

	/**
	 * 按箱上架获得物品信息
	 */
	@ApiLog("PDA-按箱上架获得物品信息")
	@PostMapping(value = "/getStockInfoPutawayForBox")
	@ApiOperationSupport(order = 20)
	@ApiOperation(value = "按箱上架获得物品信息", notes = "传入箱码")
	public R getStockInfoPutawayForBox(@Valid @RequestBody PutawayByBoxQueryDTO putawayByBoxQueryDTO) {
		return R.data(putawayByBoxService.queryStockForBox(putawayByBoxQueryDTO));
	}

	/**
	 * 按箱上架提交
	 *
	 * @param putawayByBoxSubimitVO
	 * @return
	 */
	@ApiLog("PDA-按箱上架提交")
	@PostMapping(value = "/submitPutawayForBox")
	@ApiOperationSupport(order = 21)
	@ApiOperation(value = "按箱上架提交", notes = "传入stock")
	public R submitPutawayForBox(@Validated({Add.class}) @RequestBody PutawayByBoxSubimitVO putawayByBoxSubimitVO) {
		return R.status(putawayByBoxService.submitPutaway(putawayByBoxSubimitVO));
	}

	/**
	 * 新按箱上架提交
	 *
	 * @param putawayByBoxSubimitVO
	 * @return
	 */
	@ApiLog("PDA-新按箱上架提交")
	@PostMapping(value = "/submitPutawayForBoxNew")
	@ApiOperationSupport(order = 21)
	@ApiOperation(value = "新按箱上架提交", notes = "传入stock")
	public R submitPutawayForBoxNew(@Validated({Pda.class}) @RequestBody PutawayByBoxSubimitVO putawayByBoxSubimitVO) {
		return R.status(putawayByBoxService.submitPutawayNew(putawayByBoxSubimitVO));
	}

	/**
	 * 按箱上架-根据箱号获取推荐信息
	 *
	 * @param boxCode
	 * @return
	 */
	@ApiLog("PDA-根据箱号获取推荐信息")
	@GetMapping(value = "/getRecommendInfoByBoxCode")
	@ApiOperationSupport(order = 21)
	@ApiOperation(value = "根据箱号获取推荐信息", notes = "boxCode")
	public R<BoxItemVo> getRecommendInfoByBoxCode(@RequestParam String boxCode) {
		return R.data(putawayByBoxService.getRecommendInfoByBoxCode(boxCode));
	}

	/**
	 * 新按箱移动提交
	 *
	 * @param putawayByBoxSubimitVO
	 * @return
	 */
	@ApiLog("PDA-新按箱移动提交")
	@PostMapping(value = "/submitMoveForBoxNew")
	@ApiOperation(value = "新按箱上架提交", notes = "传入stock")
	public R submitMoveForBoxNew(@Validated({Pda.class}) @RequestBody PutawayByBoxSubimitVO putawayByBoxSubimitVO) {
		return R.status(putawayByBoxService.submitMoveForBoxNew(putawayByBoxSubimitVO));
	}

	/**
	 * 按箱移动-根据箱号获取箱号信息
	 *
	 * @param boxCode
	 * @return
	 */
	@ApiLog("PDA-根据箱号获取推荐信息")
	@GetMapping(value = "/getInfoMoveByBoxCode")
	@ApiOperation(value = "根据箱号获取推荐信息", notes = "boxCode")
	public R<BoxItemVo> getInfoMoveByBoxCode(@RequestParam String boxCode) {
		return R.data(putawayByBoxService.getInfoMoveByBoxCode(boxCode));
	}

	/**
	 * 分页获取创建类型入库单据列表
	 */
	@ApiLog("PDA-分页获取创建类型入库单据")
	@PostMapping("/getAsnHeaderList")
	@ApiOperation(value = "分页", notes = "创建类型")
	public R<IPage<AsnHeaderVO>> getAsnHeaderList(Query query, @RequestParam String orderNo) {
		if (Func.isNotEmpty(orderNo)) {
			IPage<AsnHeader> page = asnHeaderService.page(Condition.getPage(query), Condition.getQueryWrapper(new AsnHeader())
				.lambda()
				.and(q ->
					q.like(AsnHeader::getAsnBillNo, orderNo)
						.or()
						.like(AsnHeader::getOrderNo, orderNo)
				).and(q ->
					q.eq(AsnHeader::getAsnBillState, AsnBillStateEnum.NOT_RECEIPT.getCode())
						.or()
						.eq(AsnHeader::getAsnBillState, AsnBillStateEnum.PART.getCode())
				));
			return R.data(AsnHeaderWrapper.build().pageVO(page));
		}
		return R.data(null);
	}

	/**
	 * 分页 清点记录
	 */
	@ApiLog("PDA-分页清点记录")
	@GetMapping("/ContainerLogList")
	@ApiOperationSupport(order = 22)
	@ApiOperation(value = "分页", notes = "传入containerLog")
	public R<IPage<ContainerLogVO>> ContainerLogList(ContainerLogDTO containerLog, Query query) {
		QueryWrapper<ContainerLog> containerLogQueryWrapper = new QueryWrapper<ContainerLog>().orderByDesc("acl_id");
		//订单编号
		if (Func.isNotEmpty(containerLog.getAsnBillNo())) {
			containerLogQueryWrapper.like("asn_bill_no", containerLog.getAsnBillNo());
		}
		//物品编码
		if (Func.isNotEmpty(containerLog.getSkuCode())) {
			//List<Sku> skuList = SkuCache.listByCode(containerLog.getSkuCode());
			ISkuService skuService = SpringUtil.getBean(ISkuService.class);
			List<Sku> skuList = skuService.list(Condition.getQueryWrapper(new Sku())
				.lambda()
				.eq(Sku::getSkuCode, containerLog.getSkuCode())
			).stream().collect(Collectors.toList());
			if (ObjectUtil.isNotEmpty(skuList)) {
				containerLogQueryWrapper.in("sku_id", NodesUtil.toList(skuList, Sku::getSkuId));
			}
		}

		IPage<ContainerLog> pages = containerLogService.page(Condition.getPage(query), containerLogQueryWrapper);
		IPage<ContainerLogVO> containerLogVOIPage = ContainerLogWrapper.build().pageVO(pages);
		List<ContainerLogVO> records = containerLogVOIPage.getRecords();
		if (Func.isNotEmpty(records)) {

			Map<Long, BigDecimal> collect = records.stream().collect(Collectors.groupingBy(ContainerLogVO::getSkuId)).entrySet()
				.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue().stream()
					.collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ContainerLogVO::getPlanQty))), ArrayList::new))
					.stream()
					.filter(d -> Objects.nonNull(d.getPlanQty()))
					.map(ContainerLogVO::getPlanQty).reduce(BigDecimal.ZERO, BigDecimal::add))
				);

			Map<Long, BigDecimal> collect1 = records.stream().collect(Collectors.groupingBy(ContainerLogVO::getSkuId))
				.entrySet()
				.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
					.stream()
					.filter(d -> Objects.nonNull(d.getAclQty()))
					.map(ContainerLogVO::getAclQty).reduce(BigDecimal.ZERO, BigDecimal::add)));
			records.forEach(containerLog1 -> {
				containerLog1.setTotalPlanQty(collect.get(containerLog1.getSkuId()));
				containerLog1.setTotalScanQty(collect1.get(containerLog1.getSkuId()));
			});
		}
		return R.data(containerLogVOIPage);
	}


	/**
	 * 获得箱号
	 */
	@ApiLog("PDA-获得箱号")
	@GetMapping("/getBoxCode")
	@ApiOperationSupport(order = 24)
	@ApiOperation(value = "获得箱号", notes = "")
	public R getBoxCode() {
		return R.data(asnByBoxService.createBoxCode());
	}

	/**
	 * 收货-获得物品列表
	 */
	@ApiLog("PDA-收货获得物品列表")
	@GetMapping("/getSkuListForInstock")
	@ApiOperationSupport(order = 25)
	@ApiOperation(value = " 收货-获得物品列表", notes = "")
	public R getSkuListForInstock(@NotEmpty @RequestParam String asnBillNo,
								  @NotEmpty @RequestParam String skuCode) {
		return R.data(asnHeaderService.getSkuListForInstock(asnBillNo, skuCode));
	}
}
