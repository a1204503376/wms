package org.nodes.wms.dao.basics.skulot.impl;

import org.nodes.wms.dao.basics.skulot.SkuLotDao;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;
import org.nodes.wms.dao.basics.skulot.mapper.SkuLotMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class SkuLotDaoImpl
	extends BaseServiceImpl<SkuLotMapper, SkuLot>
	implements SkuLotDao {
}
