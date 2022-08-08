package org.nodes.wms.dao.stock.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.stock.SerialLogDao;
import org.nodes.wms.dao.stock.dto.input.SerialLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.SerialLogExcelResponse;
import org.nodes.wms.dao.stock.dto.output.SerialLogPageResponse;
import org.nodes.wms.dao.stock.entities.SerialLog;
import org.nodes.wms.dao.stock.mapper.SerialLogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 序列号日志Dao接口实现类
 **/
@Repository
public class SerialLogDaoImpl
	extends BaseServiceImpl<SerialLogMapper, SerialLog> implements SerialLogDao {
    @Override
    public Page<SerialLogPageResponse> page(IPage<Object> page, SerialLogPageQuery serialLogPageQuery) {
        return super.baseMapper.page(page, serialLogPageQuery);
    }

	@Override
	public List<SerialLogExcelResponse> listByQuery(SerialLogPageQuery serialLogPageQuery) {
		return super.baseMapper.listByQuery(serialLogPageQuery);
	}
}
