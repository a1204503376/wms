package org.nodes.wms.dao.crontab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.springframework.context.annotation.Primary;

/**
 * 任务表 Mapper 接口
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Primary
public interface CrontabTaskMapper extends BaseMapper<CrontabTask> {

}
