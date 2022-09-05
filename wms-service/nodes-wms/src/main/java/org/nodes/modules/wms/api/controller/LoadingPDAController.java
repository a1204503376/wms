
package org.nodes.modules.wms.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.wms.core.outstock.loading.entity.SoTruckDetail;
import org.nodes.wms.core.outstock.loading.entity.SoTruckHeader;
import org.nodes.wms.core.outstock.loading.service.ISoTruckDetailService;
import org.nodes.wms.core.outstock.loading.service.ISoTruckHeaderService;
import org.nodes.wms.core.outstock.loading.vo.SoTruckDetailPdaVO;
import org.nodes.wms.core.outstock.loading.vo.SoTruckHeaderPdaVO;
import org.nodes.wms.core.outstock.so.entity.SoRegister;
import org.nodes.wms.core.outstock.so.service.ISoRegisterService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *装车(手持) 控制器
 *
 * @author chz
 * @since 2020-03-5
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA/LoadingPDA")
@ApiSort(1)
@Api(value = "手持装车接口", tags = "装车接口")
public class LoadingPDAController extends BladeController {
	private ISoTruckHeaderService iSoTruckHeaderService;
	//装车牌
	private ISoRegisterService registerService;

	private ISoTruckHeaderService soTruckHeaderService;

	private ISoTruckDetailService soTruckDetailService;
	/**
	 * 查询收货单头表列表getTruckHeader
	 */
	@GetMapping("/getTruckHeader")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "根据车次编码查询车次信息", notes = "传入truckCode")
	public R<SoTruckHeaderPdaVO> getTruckHeader(@RequestParam String truckCode, String driverName, String driverPhone, String plateNumber, String truckHeaderWaybill) {
		return R.data(iSoTruckHeaderService.getTruckHeader(truckCode,driverName,driverPhone,plateNumber,truckHeaderWaybill));
	}

	/**
	 * 根据LPNCode查询物品简要信息
	 *
	 */
	@ApiLog("PDA-根据LPNCode查询物品简要信息")
	@PostMapping("/getSkuStock")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "根据LPNCode查询物品简要信息", notes = "传入tLpnCode")
	public R<SoTruckDetailPdaVO> getSkuStock(@RequestParam String LpnCode) {
		return R.data(iSoTruckHeaderService.getSkuStock(LpnCode));
	}

	/**
	 * 根据LPNCode查询物品明细和出库信息
	 */
	@ApiLog("PDA-根据LPNCode查询物品明细和出库信息")
	@PostMapping("/getSkuStockDetailList")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "根据LPNCode查询物品信息列表", notes = "传入tLpnCode")
	public R<SoTruckHeaderPdaVO> getSkuStockDetailList(@RequestParam String LpnCode) {
		return R.data(iSoTruckHeaderService.getSkuStockDetailList(LpnCode));
	}

	/**
	 * 提交出库LPN物品明出库信息
	 */
	@ApiLog("PDA-提交出库LPN物品明出库信息")
	@PostMapping("/saveSoTruckDetail")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "提交出库LPN物品明出库信息", notes = "传入soTruckDetailList")
	public R saveSoTruckDetail(@RequestBody List<SoTruckDetail> soTruckDetailList) {
		return R.status(iSoTruckHeaderService.saveSoTruckDetail(soTruckDetailList));
	}
	/**
	 * 确认发货
	 */
	@ApiLog("PDA-确认发货")
	@PostMapping("/confirmSo")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "确认发货", notes = "确认发货truckId")
	public R confirmSo(@ApiParam(value = "主键集合", required = true) @RequestParam String truckId) {
		return R.status(iSoTruckHeaderService.confirmSo(truckId));
	}
	/**
	 * 装车登记详情
	 */
	@ApiLog("PDA-装车登记详情")
	@GetMapping("/soRegisterDetail")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "装车登记详情", notes = "传入register")
	public R<SoRegister> soRegisterDetail(SoRegister soRegister) {
		SoRegister detail = registerService.getOne(Condition.getQueryWrapper(soRegister));
		return R.data(detail);
	}

	/**
	 * 新增或修改 车次头表
	 */
	@ApiLog("PDA-新增或修改车次头表")
	@PostMapping("/soTruckHeaderSubmit")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "新增或修改", notes = "传入soTruckHeader")
	public SoTruckHeader soTruckHeaderSubmit(@Valid @RequestBody SoTruckHeader soTruckHeader) {
		BladeUser user = AuthUtil.getUser();
		//查询车次头表
		SoTruckHeader soTruckHeaderModel = soTruckHeaderService.getOne(Condition.getQueryWrapper(new SoTruckHeader()).lambda()
			.eq(SoTruckHeader::getPlateNumber, soTruckHeader.getPlateNumber()).eq(SoTruckHeader::getTruckState,10));
		if(soTruckHeaderModel!=null){
			return  soTruckHeaderModel;
		}else{
			soTruckHeader.setTruckCode(SerialNoCache.getTcNo());
			soTruckHeader.setTruckState(WmsAppConstant.Loading);
			soTruckHeader.setTruckDate(LocalDateTime.now());
			soTruckHeaderService.saveOrUpdate(soTruckHeader);
			return soTruckHeaderService.getById(soTruckHeader.getTruckId());
		}
	}

	/**
	 * 详情
	 */
	@ApiLog("PDA-车次明细详情")
	@GetMapping("/Lodingdetail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "车次明细详情", notes = "传入soTruckDetail")
	public R<List<SoTruckDetail>> Lodingdetail(SoTruckDetail soTruckDetail) {
		List<SoTruckDetail> detail = soTruckDetailService.list(Condition.getQueryWrapper(soTruckDetail));
		return R.data(detail);
	}
	/**
	 * 删除装车明细
	 */
	@ApiLog("PDA-删除装车明细")
	@PostMapping("/deleteTruckDetail")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "删除装车明细", notes = "传入soTruckDetail")
	public R deleteTruckDetail(@RequestParam String truckId,@RequestParam String truckCode,@RequestParam String lpnCode) {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("truck_id",truckId);
		paramMap.put("truck_code",truckCode);
		paramMap.put("lpn_code",lpnCode);
		boolean flag = soTruckDetailService.removeByMap(paramMap);
		if(flag){
			return R.success("删除成功！");
		}
		return R.fail("删除失败！");
	}


	/**
	 * 确认发货
	 */
	@ApiLog("PDA-确认发货")
	@PostMapping("/SaveconfirmSo")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "确认发货", notes = "确认发货truckId")
	public R SaveconfirmSo(@ApiParam(value = "主键集合", required = true) @RequestParam String truckId) {
		return R.status(iSoTruckHeaderService.SaveconfirmSo(truckId));
	}
}
