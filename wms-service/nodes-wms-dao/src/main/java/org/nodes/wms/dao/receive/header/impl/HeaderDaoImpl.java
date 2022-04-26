package org.nodes.wms.dao.receive.header.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;
import org.nodes.wms.dao.receive.header.HeaderDao;
import org.nodes.wms.dao.receive.header.dto.input.HeaderPageQuery;
import org.nodes.wms.dao.receive.header.dto.output.HeaderResponse;
import org.nodes.wms.dao.receive.header.entities.ReceiveHeader;
import org.nodes.wms.dao.receive.header.mapper.HeaderMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货单 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class HeaderDaoImpl  extends BaseServiceImpl<HeaderMapper,ReceiveHeader>  implements HeaderDao {
	private final HeaderMapper headerMapper;
	@Override
	public IPage<HeaderResponse> selectPage(IPage<HeaderResponse> page, HeaderPageQuery headerPageQuery) {
		return headerMapper.getPage(page, headerPageQuery);
	}

	@Override
	public boolean delete(Long receiveId) {
		List<Long> list = new ArrayList<Long>();
		list.add(receiveId);
		return  super.deleteLogic(list);
	}

	@Override
	public HeaderResponse selectHeaderById(Long receiveId) {
		return headerMapper.selectHeaderById(receiveId);
	}

	@Override
	public boolean updateStatusById(Long receiveDetailId) {
		LambdaQueryWrapper<ReceiveHeader> lambdaQueryWrapper = new LambdaQueryWrapper<ReceiveHeader>();
		lambdaQueryWrapper.eq(ReceiveHeader::getReceiveId,receiveDetailId);
		lambdaQueryWrapper.eq(ReceiveHeader::getStatus,91);
		return super.update(lambdaQueryWrapper);
	}

	@Override
	public boolean insert(ReceiveHeader receiveHeader) {
		return super.save(receiveHeader);
	}

}
