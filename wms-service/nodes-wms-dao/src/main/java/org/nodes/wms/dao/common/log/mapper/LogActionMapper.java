package org.nodes.wms.dao.common.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.common.log.dto.LogPageQuery;
import org.nodes.wms.dao.common.log.dto.LogResponse;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.springframework.stereotype.Repository;

/**
 * 审计日志mapper
 * @author 王智勇
 */
@Repository
@Mapper
public interface LogActionMapper extends BaseMapper<LogAction> {
    Page<LogResponse> getPage(@Param("query") LogPageQuery logPageQuery, IPage<LogAction> page);
}
