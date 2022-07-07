
package org.nodes.wms.core.outstock.so.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.outstock.so.dto.PickPlanDTO;
import org.nodes.wms.core.outstock.so.entity.PickPlan;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.service.IWellenService;
import org.nodes.wms.core.outstock.so.vo.PickPlanVO;
import org.nodes.wms.core.outstock.so.vo.PickSkuVO;
import org.nodes.wms.core.outstock.so.vo.PickTaskSubmitVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.*;

import java.util.List;

/**
 * 拣货计划表包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-02-10
 */
public class PickPlanWrapper extends BaseEntityWrapper<PickPlan, PickPlanVO> {

	static IWellenService wellenService;
	static IWellenDetailService wellenDetailService;

	static {
		wellenService = SpringUtil.getBean(IWellenService.class);
		wellenDetailService = SpringUtil.getBean(IWellenDetailService.class);
	}

	public static PickPlanWrapper build() {
		return new PickPlanWrapper();
	}

	@Override
	public PickPlanVO entityVO(PickPlan pickPlan) {
		PickPlanVO pickPlanVO = BeanUtil.copy(pickPlan, PickPlanVO.class);
		if (Func.isNotEmpty(pickPlanVO)) {
			SkuPackage skuPackage = SkuPackageCache.getById(pickPlan.getWspId());
			if (Func.isNotEmpty(skuPackage)) {
				pickPlanVO.setWspName(skuPackage.getWspName());
				SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(
					pickPlan.getWspId(), pickPlan.getSkuLevel());
				if (Func.isNotEmpty(packageDetail)) {
					pickPlanVO.setWsuName(packageDetail.getWsuName());
				}
			}
			if (Func.isNotEmpty(pickPlan.getWhId())) {
				pickPlanVO.setWhName(WarehouseCache.getById(pickPlan.getWhId()).getWhName());
			}
			if (Func.isNotEmpty(pickPlan.getWellenId())) {
				Wellen wellen = wellenService.getById(pickPlan.getWellenId());
				if (ObjectUtil.isNotEmpty(wellen)) {
					pickPlanVO.setWellenNo(wellen.getWellenNo());
				}
				List<WellenDetail> wellenDetailList = wellenDetailService.listByWellenId(pickPlan.getWellenId());
				pickPlanVO.setSoBillNo(NodesUtil.join(wellenDetailList, WellenDetail::getSoBillNo));
			}
			pickPlanVO.setSkuLevelName(DictCache.getValue(DictCodeConstant.SKU_LEVEL, pickPlan.getSkuLevel()));
		}
		return pickPlanVO;
	}

	/**
	 * 拣货计划封装成拣货物品信息
	 *
	 * @param pickPlan
	 * @param pickSkuVO
	 */
	public static void pickPlan2PickSkuVO(PickPlan pickPlan, PickSkuVO pickSkuVO) {
		pickSkuVO.setPickPlanId(pickPlan.getPickPlanId()); //拣货计划ID
		pickSkuVO.setPlanCountQty(pickPlan.getPickPlanQty()); //计划数量
		pickSkuVO.setRealCountQty(pickPlan.getPickRealQty()); // 实际数量
		pickSkuVO.setSkuCode(pickPlan.getSkuCode()); // 物品编码
		pickSkuVO.setSkuName(pickPlan.getSkuName()); // 物品名称
		pickSkuVO.setSourceLocCode(pickPlan.getLocCode()); // 源库位编码
		pickSkuVO.setWspId(pickPlan.getWspId()); //包装id
	}

	/**
	 * 封装拣货提交VO为DTO
	 *
	 * @param pickPlan
	 * @param pickTaskSubmitVO
	 * @return
	 */
	public static PickPlanDTO pickSubmitVO2PickPlanDTO(PickPlan pickPlan, PickTaskSubmitVO pickTaskSubmitVO) {
		//登录用户信息
		PickPlanDTO dto = BeanUtil.copy(pickTaskSubmitVO, PickPlanDTO.class);
		dto.setWhId(pickPlan.getWhId());
		dto.setSkuId(pickPlan.getSkuId());
		dto.setSkuCode(pickPlan.getSkuCode());
		dto.setSkuName(pickPlan.getSkuName());
		dto.setSkuLevel(pickPlan.getSkuLevel());
		dto.setUserId(AuthUtil.getUserId());
		dto.setUserName(AuthUtil.getNickName());
		dto.setWspId(pickPlan.getWspId());
		//计算数量乘以换算倍率
		dto.setPickQty(pickTaskSubmitVO.getPickQty());
		dto.setPickPlanId(pickPlan.getPickPlanId()); //拣货计划id
		dto.setLocId(pickPlan.getLocId()); //库位id
		dto.setLocCode(pickPlan.getLocCode()); //库位编码
		dto.setTaskId(pickPlan.getTaskId()); //任务id
		dto.setWellenId(pickPlan.getWellenId()); //波次id
		dto.setWellenNo(pickTaskSubmitVO.getWellenNo()); //波次编码
		return dto;
	}
}
