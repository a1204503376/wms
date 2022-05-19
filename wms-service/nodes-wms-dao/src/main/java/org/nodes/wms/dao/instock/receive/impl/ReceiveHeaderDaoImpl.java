package org.nodes.wms.dao.instock.receive.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderEditResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.mapper.ReceiveHeaderMapper;
import org.nodes.wms.dao.instock.receive.ReceiveHeaderDao;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePageQuery;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货单 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class ReceiveHeaderDaoImpl extends BaseServiceImpl<ReceiveHeaderMapper, ReceiveHeader>  implements ReceiveHeaderDao {
	private final ReceiveHeaderMapper receiveHeaderMapper;
	@Override
	public IPage<ReceiveHeaderResponse> selectPage(IPage<ReceiveHeaderResponse> page, ReceivePageQuery receivePageQuery) {
		return receiveHeaderMapper.getPage(page, receivePageQuery);
	}

	@Override
	public boolean delete(Long receiveId) {
		List<Long> list = new ArrayList<>();
		list.add(receiveId);
		return  super.deleteLogic(list);
	}

	@Override
	public ReceiveHeaderResponse selectHeaderById(Long receiveId) {
		return receiveHeaderMapper.selectHeaderById(receiveId);
	}

	@Override
	public boolean updateBillStateById(Long receiveDetailId) {
		LambdaUpdateWrapper<ReceiveHeader> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		lambdaUpdateWrapper.set(ReceiveHeader::getBillState, 40);
		lambdaUpdateWrapper.eq(ReceiveHeader::getReceiveId, receiveDetailId);
		return super.update(lambdaUpdateWrapper);
	}

	@Override
	public boolean insert(ReceiveHeader receiveHeader) {
		return super.save(receiveHeader);
	}

	@Override
	public List<ReceiveHeaderResponse> getReceiveHeaderResponseByQuery(ReceivePageQuery receivePageQuery) {
		return receiveHeaderMapper.getReceiveHeaderResponseByQuery(receivePageQuery);
	}

    @Override
    public ReceiveHeader selectReceiveHeaderById(Long receiveId) {
		return super.getById(receiveId);

    }

}
