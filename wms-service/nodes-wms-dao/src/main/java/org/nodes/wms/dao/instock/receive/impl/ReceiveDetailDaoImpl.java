package org.nodes.wms.dao.instock.receive.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.wms.dao.instock.receive.ReceiveDetailDao;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailPdaQuery;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailPdaResponse;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.mapper.ReceiveDetailMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收货单明细 DAO 实现类
 */
@Repository
@RequiredArgsConstructor
public class ReceiveDetailDaoImpl extends BaseServiceImpl<ReceiveDetailMapper, ReceiveDetail> implements ReceiveDetailDao {
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
		lambdaQueryWrapper.eq(ReceiveDetail::getReceiveId, receiveId);
		return super.list(lambdaQueryWrapper);
	}

	@Override
	public void saveOrUpdateReceive(ReceiveDetail receiveDetail) {
		super.saveOrUpdate(receiveDetail);
	}

	@Override
	public List<DetailReceiveDetailPdaResponse> selectDetailListByReceiveId(ReceiveDetailPdaQuery receiveDetailPdaQuery) {
		return receiveDetailMapper.selectDetailListByReceiveId(receiveDetailPdaQuery);
	}

	@Override
	public ReceiveDetail getDetailByReceiveDetailId(Long receiveDetailId) {
		if (Func.isEmpty(receiveDetailId)) {
			throw new NullArgumentException("ReceiveDetailDaoImpl.getDetailByReceiveDetailId");
		}
		return super.baseMapper.selectById(receiveDetailId);
	}

	@Override
	public Boolean updateReceiveDetail(ReceiveDetail detail) {
		return super.updateById(detail);
	}

}
