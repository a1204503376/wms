package org.nodes.wms.dao.putaway.impl;


import org.nodes.wms.dao.putaway.PutawayLogDao;
import org.nodes.wms.dao.putaway.entities.PutawayLog;
import org.nodes.wms.dao.putaway.mapper.PutawayLogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;


@Repository
public class PutawayLogDaoImpl
	extends BaseServiceImpl<PutawayLogMapper,PutawayLog> implements PutawayLogDao {

}
