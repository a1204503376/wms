package org.nodes.wms.dao.stock.impl;

import org.nodes.wms.dao.stock.SerialLogDao;
import org.nodes.wms.dao.stock.entities.SerialLog;
import org.nodes.wms.dao.stock.mapper.SerialLogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 序列号日志Dao接口实现类
 **/
@Repository
public class SerialLogDaoImpl
	extends BaseServiceImpl<SerialLogMapper, SerialLog> implements SerialLogDao {
}
