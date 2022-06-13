package org.nodes.wms.dao.crontab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.crontab.entity.Scheme;
import org.springframework.context.annotation.Primary;

/**
 * 任务计划表 Mapper 接口
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Primary
public interface SchemeMapper extends BaseMapper<Scheme> {

}
