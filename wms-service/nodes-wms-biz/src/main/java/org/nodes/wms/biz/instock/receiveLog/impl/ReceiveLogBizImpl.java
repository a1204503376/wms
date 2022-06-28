package org.nodes.wms.biz.instock.receiveLog.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.instock.receive.dto.input.PdaByPieceReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaRequest;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnItemDto;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receiveLog.ReceiveLogDao;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveLogPageQuery;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogExcelResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogPageResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 清点记录业务层实现类
 */
@Service
@RequiredArgsConstructor
public class ReceiveLogBizImpl implements ReceiveLogBiz {
	private final ReceiveLogDao receiveLogDao;
	private final LocationBiz locationBiz;
	private final ReceiveBiz receiveBiz;
	private final StockBiz stockBiz;

	@Override
	public List<ReceiveLogResponse> getReceiveLogList(Long receiveId) {
		return receiveLogDao.getReceiveLogList(receiveId);
	}

	@Override
	public List<ReceiveLogIndexResponse> findReceiveSkuQtyTop10() {
		return receiveLogDao.getReceiveSkuQtyTop10();
	}

	@Override
	public ReceiveLog newReceiveLog(PdaByPieceReceiveRequest request) {
		ReceiveLog receiveLog = new ReceiveLog();
		receiveLog.setReceiveId(request.getReceiveId());
		receiveLog.setReceiveDetailId(request.getReceiveDetailId());
		receiveLog.setBoxCode(request.getBoxCode());
		receiveLog.setSnCode(request.getSerialNumberList().toString());
		receiveLog.setWhCode(request.getWhCode());
		receiveLog.setQty(request.getSurplusQty());
		// TODO
		receiveLog.setLpnCode(request.getBoxCode());
		receiveLog.setLocCode(request.getLocCode());
		receiveLog.setSkuCode(request.getSkuCode());
		receiveLog.setSkuName(request.getSkuName());
		receiveLog.setSkuSpec(request.getSkuLot2());
		return createReceiveLog(receiveLog);
	}

	ReceiveLog createReceiveLog(ReceiveLog receiveLog) {
		ReceiveDetail detail = receiveBiz.getDetailByReceiveDetailId(receiveLog.getReceiveDetailId());
		ReceiveHeader receiveHeader = receiveBiz.selectReceiveHeaderById(receiveLog.getReceiveId());
		Location location = locationBiz.findLocationByLocCode(0L,receiveLog.getLocCode());
		receiveLog.setReceiveNo(receiveHeader.getReceiveNo());
		receiveLog.setAsnBillId(receiveHeader.getAsnBillId());
		receiveLog.setAsnBillNo(receiveHeader.getAsnBillNo());
		receiveLog.setLineNo(detail.getLineNo());
		receiveLog.setLocId(location.getLocId());
		receiveLog.setSkuId(detail.getSkuId());
		receiveLog.setWspId(detail.getWspId());
		receiveLog.setSkuLevel(detail.getSkuLevel());
		receiveLog.setWhId(detail.getWhId());
		receiveLog.setWhCode(detail.getWhCode());
		if (Func.isNotEmpty(detail.getWhId()) && Func.isNotEmpty(detail.getOwnerCode())) {
			receiveLog.setWoId(detail.getWoId());
			receiveLog.setOwnerCode(detail.getOwnerCode());
		} else {
			receiveLog.setWoId(receiveHeader.getWoId());
			receiveLog.setOwnerCode(receiveHeader.getOwnerCode());
		}
		return receiveLog;
	}

	@Override
	public Page<ReceiveLogPageResponse> page(Query query, ReceiveLogPageQuery receiveLogPageQuery) {
		return receiveLogDao.page(Condition.getPage(query), receiveLogPageQuery);
	}

	@Override
	public void exportExcel(ReceiveLogPageQuery receiveLogPageQuery, HttpServletResponse response) {
		List<ReceiveLogExcelResponse> receiveLogList
			= receiveLogDao.getReceiveLogListByQuery(receiveLogPageQuery);
		ExcelUtil.export(response, "收货记录", "收货记录", receiveLogList, ReceiveLogExcelResponse.class);
	}

	@Override
	public ReceiveLog newReceiveLog(ReceiveDetailLpnPdaRequest request, ReceiveDetailLpnItemDto item, ReceiveDetailLpn lpn) {
		ReceiveLog receiveLog = new ReceiveLog();
		receiveLog.setReceiveId(request.getReceiveHeaderId());
		receiveLog.setReceiveDetailId(item.getReceiveDetailId());
		receiveLog.setBoxCode(request.getBoxCode());
		receiveLog.setSnCode(lpn.getSnCode());
		receiveLog.setQty(item.getPlanQty());
		// TODO
		receiveLog.setLpnCode(request.getLpnCode());
		receiveLog.setLocCode(request.getLocCode());
		receiveLog.setSkuCode(item.getSkuCode());
		receiveLog.setSkuName(item.getSkuName());
		receiveLog.setSkuSpec(request.getSkuLot2());
		receiveLog =  createReceiveLog(receiveLog);
		receiveLogDao.save(receiveLog);
		return receiveLog;
	}
}
