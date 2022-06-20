package org.nodes.wms.dao.common.log.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.common.log.LogActionDao;
import org.nodes.wms.dao.common.log.LogErrorDao;
import org.nodes.wms.dao.common.log.dto.input.LogErrorPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogErrorPageResponse;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.entities.LogError;
import org.nodes.wms.dao.common.log.mapper.LogActionMapper;
import org.nodes.wms.dao.common.log.mapper.LogErrorMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 异常日志dao实现类
 */
@Repository
public class LogErrorDaoImpl  extends BaseServiceImpl<LogErrorMapper, LogError> implements LogErrorDao {

	@Override
	public IPage<LogErrorPageResponse> selectPage(LogErrorPageQuery logErrorPageQuery, IPage<LogErrorPageResponse> page) {
		return super.baseMapper.getPage(logErrorPageQuery,page);
	}

    @Override
    public List<LogErrorPageResponse> getLogErrorResponseByQuery(LogErrorPageQuery logErrorPageQuery) {
        return super.baseMapper.getLogErrorResponseByQuery(logErrorPageQuery);

    }
}
