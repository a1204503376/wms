package org.nodes.wms.biz.outstock;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 出库业务实现类
 * 
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class OutStockBizImpl implements OutStockBiz {

	private final SoPickPlanDao soPickPlanDao;

    @Override
    public void pickByPc(PickByPcRequest request) {
        // TODO Auto-generated method stub
        // 单据状态为终结状态，则不能进行拣货
        // 已经分配过的不能进行PC拣货
        //
    }

    @Override
    public List<SoPickPlanForDistributionResponse> getSoPickPlanBySoBillIdAndSoDetailId(Long soBillId, Long soDetailId) {
		AssertUtil.notNull(soBillId.toString(),"查询拣货计划失败，发货单id为空");
		List<SoPickPlanForDistributionResponse> soPickPlanList = soPickPlanDao.getBySoBillIdAndSoDetailId(soBillId, soDetailId);
		soPickPlanList.forEach(item -> {
			item.setStockStatusValue(item.getStockStatus().getDesc());
		});
		return soPickPlanList;
    }
}
