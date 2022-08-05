package org.nodes.wms.biz.count.modular;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.utils.OrderNoGeneratorUtil;
import org.nodes.wms.dao.count.dto.input.StockCountRequest;
import org.nodes.wms.dao.count.entity.CountDetail;
import org.nodes.wms.dao.count.entity.CountHeader;
import org.nodes.wms.dao.count.enums.CountByEnum;
import org.nodes.wms.dao.count.enums.StockCountStateEnum;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 盘点工厂
 */
@Component
@RequiredArgsConstructor
public class StockCountFactory {

	private final OrderNoGeneratorUtil orderNoGeneratorUtil;

	public CountHeader createCountHeader(StockCountRequest stockCountRequest){
		CountHeader countHeader = Func.copy(stockCountRequest, CountHeader.class);
		assert countHeader != null;
		countHeader.setCountBillId(IdWorker.getId());
		if (Func.isBlank(countHeader.getCountBillNo())){
			countHeader.setCountBillNo(orderNoGeneratorUtil.createCountBillNo());
		}
		countHeader.setCountBillState(StockCountStateEnum.CREATE);
		countHeader.setCountBy(CountByEnum.SKU);
		countHeader.setProcTime(LocalDateTime.now());
		countHeader.setCreator(AuthUtil.getUserName());
		return countHeader;
	}

	public CountHeader getCountHeader(StockCountRequest stockCountRequest,CountHeader countHeader){
		countHeader.setCountRemark(stockCountRequest.getCountRemark());
		countHeader.setWhId(stockCountRequest.getWhId());
		return countHeader;
	}

	public List<CountDetail> createCountDetail(StockCountRequest stockCountRequest){
		List<CountDetail> countDetailList = Func.copy(stockCountRequest.getStockCountDetailRequestList(), CountDetail.class);
		countDetailList.forEach(d->{
			d.setCountBillId(stockCountRequest.getCountBillId());
			d.setCountBillNo(stockCountRequest.getCountBillNo());
		});
		return countDetailList;
	}
}
