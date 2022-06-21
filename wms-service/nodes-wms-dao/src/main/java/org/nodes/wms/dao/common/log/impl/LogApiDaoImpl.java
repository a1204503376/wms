package org.nodes.wms.dao.common.log.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.common.log.LogApiDao;
import org.nodes.wms.dao.common.log.dto.input.LogApiPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogApiPageResponse;
import org.nodes.wms.dao.common.log.entities.LogApi;
import org.nodes.wms.dao.common.log.mapper.LogApiMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 请求日志dao实现类
 */
@Repository
public class LogApiDaoImpl extends BaseServiceImpl<LogApiMapper, LogApi> implements LogApiDao {

	@Override
	public IPage<LogApiPageResponse> selectPage(LogApiPageQuery logApiPageQuery, IPage<LogApiPageResponse> page) {
		return super.baseMapper.getPage(logApiPageQuery,page);
	}

    @Override
    public List<LogApiPageResponse> getLogApiResponseByQuery(LogApiPageQuery logApiPageQuery) {
        return super.baseMapper.getLogApiResponseByQuery(logApiPageQuery);
    }
}
