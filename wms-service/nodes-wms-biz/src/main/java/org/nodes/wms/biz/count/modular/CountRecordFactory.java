package org.nodes.wms.biz.count.modular;

import org.nodes.wms.dao.count.dto.input.AutoLocationBoxQty;
import org.nodes.wms.dao.count.dto.input.GenerateCountReport;
import org.nodes.wms.dao.count.dto.output.PdaBoxQtyResponse;
import org.nodes.wms.dao.count.entity.CountDetail;
import org.nodes.wms.dao.count.entity.CountRecord;
import org.nodes.wms.dao.count.enums.CountRecordStateEnum;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 盘点生成差异报告工厂对象
 *
 * @author nodes
 */
@Component
public class CountRecordFactory {
	/**
	 * 盘点生成差异报告构造工厂
	 *
	 * @param generateCountReport 生成盘点差异报告请求对象
	 * @param stock               库存对象
	 * @param countDetail         盘点单明细表
	 * @return 盘点差异报告表实体类
	 */
	public CountRecord createCountRecord(GenerateCountReport generateCountReport, Stock stock, CountDetail countDetail) {
		CountRecord countRecord = BeanUtil.copy(stock, CountRecord.class);
		countRecord.setCountBillId(countDetail.getCountBillId());
		countRecord.setCountBillNo(countDetail.getCountBillNo());
		countRecord.setCountQty(generateCountReport.getStockBalance());
		countRecord.setBoxCode(generateCountReport.getBoxCode());
		countRecord.setLocCode(generateCountReport.getLocCode());
		if (generateCountReport.getIsValid()) {
			countRecord.setCountQty(stock.getStockBalance());
		} else {
			countRecord.setCountQty(BigDecimal.ZERO);
		}
		countRecord.setUserId(AuthUtil.getUserId());
		countRecord.setUserName(AuthUtil.getUserName());
		countRecord.setCreateUser(AuthUtil.getUserId());
		countRecord.setRecordState(CountRecordStateEnum.Complated.getIndex());

		return countRecord;
	}

	/**
	 * 盘点生成差异报告构造工厂
	 *
	 * @param stock       库存集合对象
	 * @param countDetail 盘点单明细表
	 * @return 盘点差异报告表实体类
	 */
	public CountRecord createCountReport(PdaBoxQtyResponse stock, CountDetail countDetail, AutoLocationBoxQty autoLocationBoxQty) {

		CountRecord countRecord = BeanUtil.copy(stock, CountRecord.class);
		countRecord.setCountBillId(countDetail.getCountBillId());
		countRecord.setCountBillNo(countDetail.getCountBillNo());
		countRecord.setBoxCode(stock.getBoxCode());
		countRecord.setSkuCode(stock.getSkuCode());
		countRecord.setSkuName(stock.getSkuName());
		countRecord.setWsuName(stock.getWsuCode());
		countRecord.setUserId(AuthUtil.getUserId());
		countRecord.setUserName(AuthUtil.getUserName());
		countRecord.setCreateUser(AuthUtil.getUserId());
		countRecord.setRecordState(CountRecordStateEnum.Complated.getIndex());
		if (autoLocationBoxQty.getIsValid()) {
			countRecord.setCountQty(autoLocationBoxQty.getTotalQty());
		} else {
			countRecord.setCountQty(BigDecimal.ZERO);
		}
		return countRecord;
	}


}
