package org.nodes.wms.biz.instock.receive.header.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.biz.instock.receive.header.modular.ReceiveFactory;
import org.nodes.wms.biz.instock.receive.header.ReceiveBiz;
import org.nodes.wms.dao.instock.receive.detail.ReceiveDetailDao;
import org.nodes.wms.dao.instock.receive.detail.dto.input.ReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.detail.dto.output.ReceiveDetailResponse;
import org.nodes.wms.dao.instock.receive.header.ReceiveHeaderDao;
import org.nodes.wms.dao.instock.receive.header.dto.input.ReceiveHeaderPageQuery;
import org.nodes.wms.dao.instock.receive.header.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.instock.receive.header.dto.output.ReceiveHeaderDetailResponse;
import org.nodes.wms.dao.instock.receive.header.dto.output.ReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.header.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.log.ReceiveLogDao;
import org.nodes.wms.dao.instock.receive.log.dto.output.ReceiveLogResponse;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 收货单管理业务类
 */
@Service
@RequiredArgsConstructor
public class ReceiveBizImpl implements ReceiveBiz {
	private  final ReceiveHeaderDao receiveHeaderDao;
	private  final ReceiveDetailDao receiveDetailDao;
	private  final ReceiveLogDao receiveLogDao;
	private  final NoGeneratorUtil noGeneratorUtil;
	private final ReceiveFactory receiveFactory;
	@Override
	public IPage<ReceiveHeaderResponse> getPage(ReceiveHeaderPageQuery receiveHeaderPageQuery, Query query) {
		IPage<ReceiveHeaderResponse> page = Condition.getPage(query);
		return receiveHeaderDao.selectPage(page, receiveHeaderPageQuery);
	}

	@Override
	@Transactional
	public boolean remove(Long receiveId) {
		//获取关联明细表的集合
		List<ReceiveDetailRequest> receiveDetailRequestList =  receiveDetailDao.selectDetailByHeaderId(receiveId);
		List<Long> ids = new ArrayList<>();
		BigDecimal bigDecimal = new BigDecimal(0);
		//遍历实收数量，如果大于0则不允许删除
		for(int i = 0; i< receiveDetailRequestList.size(); i++){
			if (BigDecimalUtil.gt(receiveDetailRequestList.get(i).getScanQty(),bigDecimal)){
				throw new ServiceException("删除收货单失败，已收货单据不允许删除");
			}
			ids.add(receiveDetailRequestList.get(i).getReceiveDetailId());
		}
         //删除明细
          receiveDetailDao.delete(ids);
		return receiveHeaderDao.delete(receiveId);
	}

	@Override
	public ReceiveHeaderDetailResponse detail(Long receiveId) {
		//查询收货单头表
		ReceiveHeaderResponse receiveHeaderResponse = receiveHeaderDao.selectHeaderById(receiveId);
		//查询收货单头表关联明细
		List<ReceiveDetailResponse> detailList = receiveDetailDao.selectDetailById(receiveId);
		//拼接成一个返回对象
		ReceiveHeaderDetailResponse receiveHeaderDetailResponse = new ReceiveHeaderDetailResponse();
		receiveHeaderDetailResponse.setReceiveHeaderResponse(receiveHeaderResponse);
		receiveHeaderDetailResponse.setDetailList(detailList);
		return receiveHeaderDetailResponse;
	}

	@Override
	public boolean editBillState(Long receiveDetailId) {
		return  receiveHeaderDao.updateBillStateById(receiveDetailId);
	}

	@Override
	public List<ReceiveLogResponse> queryLog(Long receiveId) {
		//获取明细表Id的集合
	   List<Long> detailIds = receiveDetailDao.selectDetailIdByReceiveId(receiveId);
	   //根据明细表Id集合获取清点记录
       return receiveLogDao.selectByDetailIds(detailIds);
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
}
