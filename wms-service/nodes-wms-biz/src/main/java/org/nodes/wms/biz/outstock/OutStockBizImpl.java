package org.nodes.wms.biz.outstock;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutStockBizImpl implements OutStockBiz {

	private final SoPickPlanDao soPickPlanDao;

    @Override
    public void pcPickByPcs(PickByPcRequest request) {
        // TODO Auto-generated method stub

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
