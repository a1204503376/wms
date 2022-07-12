package org.nodes.wms.dao.outstock.logSoPick.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.logSoPick.LogSoPickDao;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.logSoPick.mapper.LogSoPickMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 拣货记录日志Dao接口实现类
 **/
@Repository
public class LogSoPickDaoImpl extends BaseServiceImpl<LogSoPickMapper, LogSoPick> implements LogSoPickDao {

	@Override
	public List<LogSoPickIndexResponse> getPickSkuQtyTop10() {
		return super.baseMapper.selectPickSkuQtyTop10();
	}

	@Override
	public Page<LogSoPickForSoDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, Long soBillId) {
		return super.baseMapper.pageForSoDetailBySoBillId(page, soBillId);
	}
}
