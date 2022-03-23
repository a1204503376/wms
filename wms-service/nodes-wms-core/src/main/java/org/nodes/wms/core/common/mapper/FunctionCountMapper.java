package org.nodes.wms.core.common.mapper;

import org.nodes.wms.core.common.entity.FunctionCount;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Primary;


/**
 * 功能计数	 Mapper 接口
 *
 * @author NodeX
 * @since 2021-07-23
 */
@Primary
public interface FunctionCountMapper extends BaseMapper<FunctionCount> {

}
