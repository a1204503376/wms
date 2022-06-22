package org.nodes.wms.dao.instock.receiveLog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.receiveLog.ReceiveLogDao;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.instock.receiveLog.mapper.ReceiveLogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 清点记录Dao实现类
 */
@Repository
@RequiredArgsConstructor
public class ReceiveLogDaoImpl extends BaseServiceImpl<ReceiveLogMapper, ReceiveLog> implements ReceiveLogDao {
	@Override
	public List<ReceiveLogResponse> getReceiveLogList(Long receiveId) {
		return super.baseMapper.selectReceiveLogList(receiveId);
	}
}
