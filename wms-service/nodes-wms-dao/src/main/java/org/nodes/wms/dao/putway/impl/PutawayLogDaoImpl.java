package org.nodes.wms.dao.putway.impl;


import org.nodes.wms.dao.putway.PutawayLogDao;
import org.nodes.wms.dao.putway.entities.PutawayLog;
import org.nodes.wms.dao.putway.mapper.PutawayLogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;


@Repository
public class PutawayLogDaoImpl
	extends BaseServiceImpl<PutawayLogMapper,PutawayLog> implements PutawayLogDao {

}
