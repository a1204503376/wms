package org.nodes.wms.dao.outstock.so.impl;

import org.nodes.wms.dao.outstock.so.SoDetailDao;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.mapper.SoDetailMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 出库单Dao接口实现类
 **/
@Repository
public class SoDetailDaoImpl extends BaseServiceImpl<SoDetailMapper, SoDetail> implements SoDetailDao {
	@Override
	public boolean saveOrUpdateBatch(List<SoDetail> soDetailList) {
		return super.saveOrUpdateBatch(soDetailList);
	}
}
