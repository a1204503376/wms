package org.nodes.wms.biz.receive.header.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.biz.receive.header.ReceiveBiz;
import org.nodes.wms.biz.receive.header.modular.ReceiveFactory;
import org.nodes.wms.dao.receive.detail.DetailDao;
import org.nodes.wms.dao.receive.detail.dto.input.DetailRequest;
import org.nodes.wms.dao.receive.detail.dto.output.DetailResponse;
import org.nodes.wms.dao.receive.header.HeaderDao;
import org.nodes.wms.dao.receive.header.dto.input.HeaderPageQuery;
import org.nodes.wms.dao.receive.header.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.receive.header.dto.output.HeaderDetailResponse;
import org.nodes.wms.dao.receive.header.dto.output.HeaderResponse;
import org.nodes.wms.dao.receive.header.entities.ReceiveHeader;
import org.nodes.wms.dao.receive.log.LogDao;
import org.nodes.wms.dao.receive.log.dto.output.LogResponse;
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
	private  final HeaderDao headerDao;
	private  final DetailDao detailDao;
	private  final LogDao logDao;
	private  final NoGeneratorUtil noGeneratorUtil;
	private final ReceiveFactory receiveFactory;
	@Override
	public IPage<HeaderResponse> getPage(HeaderPageQuery headerPageQuery, Query query) {
		IPage<HeaderResponse> page = Condition.getPage(query);
		return headerDao.selectPage(page,headerPageQuery);
	}

	@Override
	@Transactional
	public boolean remove(Long receiveId) {
		//获取关联明细表的集合
		List<DetailRequest> detailRequestList =  detailDao.selectDetailByHeaderId(receiveId);
		List<Long> ids = new ArrayList<Long>();
		BigDecimal bigDecimal = new BigDecimal(0);
		//遍历实收数量，如果大于0则不允许删除
		for(int i=0;i<detailRequestList.size();i++){
			if (BigDecimalUtil.gt(detailRequestList.get(i).getScanQty(),bigDecimal)){
				throw new ServiceException("删除收货单失败，已收货单据不允许删除");
			}
			ids.add(detailRequestList.get(i).getReceiveDetailId());
		}
         //删除明细
          detailDao.delete(ids);
		return headerDao.delete(receiveId);
	}

	@Override
	public HeaderDetailResponse detail(Long receiveId) {
		//查询收货单头表
		HeaderResponse headerResponse = headerDao.selectHeaderById(receiveId);
		//查询收货单头表关联明细
		List<DetailResponse> detailList = detailDao.selectDetailById(receiveId);
		//拼接成一个返回对象
		HeaderDetailResponse headerDetailResponse = new HeaderDetailResponse();
		headerDetailResponse.setHeaderResponse(headerResponse);
		headerDetailResponse.setDetailList(detailList);
		return  headerDetailResponse;
	}

	@Override
	public boolean edit(Long receiveDetailId) {
		return  headerDao.updateStatusById(receiveDetailId);
	}

	@Override
	public List<LogResponse> queryLog(Long receiveId) {
		//获取明细表Id的集合
	   List<Long> detailIds = detailDao.selectDetailIdByReceiveId(receiveId);
	   //根据明细表Id集合获取清点记录
       return logDao.selectByDetailIds(detailIds);
	}

	@Override
	public boolean newReceive(NewReceiveRequest newReceiveRequest) {
		//创建保存实体类
        ReceiveHeader receiveHeader = receiveFactory.createReceiveHeader(newReceiveRequest.getNewHeaderRequest());
        //设置收货单编码
        receiveHeader.setReceiveNo(noGeneratorUtil.createReceiveBillNo());
        headerDao.insert(receiveHeader);

		return true;
	}
}
