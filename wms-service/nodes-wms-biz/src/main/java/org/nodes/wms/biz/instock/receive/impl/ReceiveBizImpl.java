package org.nodes.wms.biz.instock.receive.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receive.modular.ReceiveFactory;
import org.nodes.wms.dao.instock.receive.ReceiveDetailDao;
import org.nodes.wms.dao.instock.receive.ReceiveHeaderDao;
import org.nodes.wms.dao.instock.receive.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePageQuery;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.enums.ReceiveBillStateEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 收货单管理业务类
 */
@Service
@RequiredArgsConstructor
public class ReceiveBizImpl implements ReceiveBiz {
	private final ReceiveHeaderDao receiveHeaderDao;
	private final ReceiveDetailDao receiveDetailDao;

	private final NoGeneratorUtil noGeneratorUtil;
	private final ReceiveFactory receiveFactory;

	@Override
	public IPage<ReceiveHeaderResponse> getPage(ReceivePageQuery receivePageQuery, Query query) {
		IPage<ReceiveHeaderResponse> page = Condition.getPage(query);
		IPage<ReceiveHeaderResponse> responsePage  =  receiveHeaderDao.selectPage(page, receivePageQuery);
		List<ReceiveHeaderResponse> receiveHeaderResponseList = responsePage.getRecords();

		for(int i=0;i<receiveHeaderResponseList.size();i++){
			ReceiveBillStateEnum billState = receiveHeaderResponseList.get(i).getBillState();
			String billStateDesc = billState.getDesc();
			receiveHeaderResponseList.get(i).setBillStateDesc(billStateDesc);
		}

        responsePage.setRecords(receiveHeaderResponseList);
		return responsePage;
	}

	@Override
	@Transactional
	public boolean remove(Long receiveId) {
		//获取关联明细表的集合
		List<ReceiveDetailRequest> receiveDetailRequestList = receiveDetailDao.selectDetailByHeaderId(receiveId);
		List<Long> ids = new ArrayList<>();
		BigDecimal bigDecimal = new BigDecimal(0);
		//遍历实收数量，如果大于0则不允许删除
		for (int i = 0; i < receiveDetailRequestList.size(); i++) {
			if (BigDecimalUtil.gt(receiveDetailRequestList.get(i).getScanQty(), bigDecimal)) {
				throw new ServiceException("删除收货单失败，已收货单据不允许删除");
			}
			ids.add(receiveDetailRequestList.get(i).getReceiveDetailId());
		}
		//删除明细
		receiveDetailDao.delete(ids);
		return receiveHeaderDao.delete(receiveId);
	}

	@Override
	public ReceiveResponse getReceivedetail(Long receiveId) {
		//查询收货单头表
		ReceiveHeaderResponse receiveHeaderResponse = receiveHeaderDao.selectHeaderById(receiveId);
		//查询收货单头表关联明细
		List<ReceiveDetailResponse> detailList = receiveDetailDao.selectDetailById(receiveId);
		//拼接成一个返回对象
		ReceiveResponse receiveResponse = new ReceiveResponse();
		receiveResponse.setReceiveHeaderResponse(receiveHeaderResponse);
		receiveResponse.setDetailList(detailList);
		return receiveResponse;
	}

	@Override
	public boolean editBillState(Long receiveId) {
		return receiveHeaderDao.updateBillStateById(receiveId);
	}


	@Override
	public boolean newReceive(NewReceiveRequest newReceiveRequest) {
		//创建保存实体类
		ReceiveHeader receiveHeader = receiveFactory.createReceiveHeader(newReceiveRequest.getNewReceiveHeaderRequest());
		//设置收货单编码
		receiveHeader.setReceiveNo(noGeneratorUtil.createReceiveBillNo());
		receiveHeaderDao.insert(receiveHeader);

		return true;
	}

	@Override
	public void exportExcel(ReceivePageQuery receivePageQuery, HttpServletResponse response) {
		List<ReceiveHeaderResponse> receiveHeaderResponseList = receiveHeaderDao.getReceiveHeaderResponseByQuery(receivePageQuery);
		for(int i=0;i<receiveHeaderResponseList.size();i++){
			String billState = receiveHeaderResponseList.get(i).getBillState().getDesc();
			receiveHeaderResponseList.get(i).setBillStateDesc(billState);
		}
		ExcelUtil.export(response, "收货单", "收货单数据表", receiveHeaderResponseList, ReceiveHeaderResponse.class);
	}
}
