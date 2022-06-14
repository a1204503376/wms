package org.nodes.wms.dao.instock.receive.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.mapper.ReceiveDetailMapper;
import org.nodes.wms.dao.instock.receive.ReceiveDetailDao;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailResponse;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货单明细 DAO 实现类
 */
@Repository
@RequiredArgsConstructor
public class ReceiveDetailDaoImpl extends BaseServiceImpl<ReceiveDetailMapper, ReceiveDetail>  implements ReceiveDetailDao {
	private final ReceiveDetailMapper receiveDetailMapper;
	@Override
	public List<DetailReceiveDetailResponse> selectDetailById(Long receiveId) {
		return receiveDetailMapper.selectDetailById(receiveId);
	}

	@Override
	public List<ReceiveDetailRequest> selectDetailByHeaderId(Long receiveId) {
		return receiveDetailMapper.selectDetailByHeaderId(receiveId);
	}

	@Override
	public boolean delete(List<Long> ids) {
		return super.deleteLogic(ids);
	}

	@Override
	public List<Long> selectDetailIdByReceiveId(Long receiveId) {
		return receiveDetailMapper.selectDetailIdByReceiveId(receiveId);
	}

    @Override
    public boolean insert(ReceiveDetail receiveDetail) {
		return super.save(receiveDetail);
    }

    @Override
    public List<ReceiveDetail> selectReceiveDetailById(Long receiveId) {
		 LambdaQueryWrapper<ReceiveDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(ReceiveDetail::getReceiveId,receiveId);
		return super.list(lambdaQueryWrapper);
    }

	@Override
	public void saveOrUpdateReceive(ReceiveDetail receiveDetail) {
		super.saveOrUpdate(receiveDetail);
	}

}
