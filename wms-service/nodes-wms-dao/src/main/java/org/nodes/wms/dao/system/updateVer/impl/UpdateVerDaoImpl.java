package org.nodes.wms.dao.system.updateVer.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.system.updateVer.UpdateVerDao;
import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerPageQuery;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerExportResponse;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerPageResponse;
import org.nodes.wms.dao.system.updateVer.entities.UpdateVer;
import org.nodes.wms.dao.system.updateVer.mapper.UpdateVerMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 **/
@Repository
public class UpdateVerDaoImpl extends BaseServiceImpl<UpdateVerMapper, UpdateVer> implements UpdateVerDao {

	@Override
	public Page<UpdateVerPageResponse> page(IPage<?> page, UpdateVerPageQuery query) {
		return super.baseMapper.selectUpdateVerPage(page, query);
	}

	@Override
	public UpdateVer getById(Long suvId) {
		return super.getById(suvId);
	}

	@Override
	public void updateUpdateVerById(UpdateVer updateVer) {
		super.updateById(updateVer);
	}

	@Override
	public List<UpdateVerExportResponse> listByQuery(UpdateVerPageQuery updateVerPageQuery) {
		return super.baseMapper.selectUpdateByQuery(updateVerPageQuery);
	}
}
