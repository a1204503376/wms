package org.nodes.wms.dao.outstock.so.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.so.SoHeaderDao;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderPageResponse;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.mapper.SoHeaderMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 发货单Dao接口实现类
 **/
@Repository
public class SoHeaderDaoImpl extends BaseServiceImpl<SoHeaderMapper, SoHeader> implements SoHeaderDao {

	@Override
	public Page<SoHeaderPageResponse> page(IPage<?> page, SoHeaderPageQuery soHeaderPageQuery) {
		return super.baseMapper.page(page, soHeaderPageQuery);
	}

	@Override
	public boolean saveOrUpdateSoHeader(SoHeader soHeader) {
		return super.saveOrUpdate(soHeader);
	}

	@Override
	public SoHeaderEditResponse getSoHeaderEditBySoBillId(Long soBillId) {
		return super.baseMapper.selectSoHeaderEditBySoBillId(soBillId);
	}

}
