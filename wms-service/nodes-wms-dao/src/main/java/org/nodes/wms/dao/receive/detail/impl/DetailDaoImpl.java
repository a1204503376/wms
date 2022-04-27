package org.nodes.wms.dao.receive.detail.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.receive.detail.DetailDao;
import org.nodes.wms.dao.receive.detail.dto.input.DetailRequest;
import org.nodes.wms.dao.receive.detail.dto.output.DetailResponse;
import org.nodes.wms.dao.receive.detail.entities.ReceiveDetail;
import org.nodes.wms.dao.receive.detail.mapper.DetailMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货单明细 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class DetailDaoImpl extends BaseServiceImpl<DetailMapper, ReceiveDetail>  implements DetailDao {
	private final DetailMapper detailMapper;
	@Override
	public List<DetailResponse> selectDetailById(Long receiveId) {
		return detailMapper.selectDetailById(receiveId);
	}

	@Override
	public List<DetailRequest> selectDetailByHeaderId(Long receiveId) {
		return detailMapper.selectDetailByHeaderId(receiveId);
	}

	@Override
	public boolean delete(List<Long> ids) {
		return super.deleteLogic(ids);
	}

	@Override
	public List<Long> selectDetailIdByReceiveId(Long receiveId) {
		return detailMapper.selectDetailIdByReceiveId(receiveId);
	}

}
