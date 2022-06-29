package org.nodes.wms.biz.instock.receiveLog.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
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
import java.text.DateFormat;
import java.util.Date;
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
	private final OwnerBiz ownerBiz;


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
		if (request.getIsSn()) {
			receiveLog.setSnCode(String.join(",", request.getSerialNumberList()));
		}
		receiveLog.setWhCode(request.getWhCode());
		receiveLog.setQty(request.getSurplusQty());
		receiveLog.setLpnCode(request.getBoxCode());
		receiveLog.setLocCode(request.getLocCode());
		receiveLog.setSkuCode(request.getSkuCode());
		receiveLog.setSkuName(request.getSkuName());
		receiveLog.setSkuSpec(request.getSkuLot2());
		receiveLog.setWhId(request.getWhId());
		ReceiveLog log = createReceiveLog(receiveLog);
		log.setSkuLot1(request.getSkuLot1());
		log.setSkuLot2(request.getSkuLot2());
		log.setSkuLot3(DateFormat.getInstance().format(new Date())); //TODO 收货时间
		receiveLogDao.save(log);
		return log;
	}

	ReceiveLog createReceiveLog(ReceiveLog receiveLog) {
		ReceiveDetail detail = receiveBiz.getDetailByReceiveDetailId(receiveLog.getReceiveDetailId());
		ReceiveHeader receiveHeader = receiveBiz.selectReceiveHeaderById(receiveLog.getReceiveId());
		SkuLotUtil.setAllSkuLot(receiveLog, detail);
		Location location = locationBiz.findLocationByLocCode(receiveLog.getWhId(), receiveLog.getLocCode());
		receiveLog.setReceiveNo(receiveHeader.getReceiveNo());
		receiveLog.setAsnBillId(receiveHeader.getAsnBillId());
		receiveLog.setAsnBillNo(receiveHeader.getAsnBillNo());
		receiveLog.setLineNo(detail.getLineNo());
		receiveLog.setLocId(location.getLocId());
		receiveLog.setWhCode(detail.getWhCode());
		receiveLog.setSkuId(detail.getSkuId());
		receiveLog.setWspId(detail.getWspId());
		receiveLog.setSkuLevel(detail.getSkuLevel());
		Owner owner;
		if (Func.isNotEmpty(detail.getWoId())) {
			owner = ownerBiz.findById(detail.getWoId());
		} else {
			owner = ownerBiz.findById(receiveHeader.getWoId());
		}
		if (Func.isNotEmpty(owner)) {
			receiveLog.setWoId(owner.getWoId());
			receiveLog.setOwnerCode(owner.getOwnerCode());
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
		receiveLog.setLpnCode(request.getLpnCode());
		receiveLog.setLocCode(request.getLocCode());
		receiveLog.setSkuCode(item.getSkuCode());
		receiveLog.setSkuName(item.getSkuName());
		receiveLog.setSkuSpec(request.getSkuLot2());
		receiveLog.setWhId(request.getWhId());
		receiveLog = createReceiveLog(receiveLog);
		receiveLogDao.save(receiveLog);
		return receiveLog;
	}
}
