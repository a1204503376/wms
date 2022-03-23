package org.nodes.core.base.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.core.base.entity.NodesCurdColumn;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 列显隐表 Mapper 接口
 *
 * @author wangYunan
 * @since 2021-07-06
 */
@Primary
public interface NodesCurdColumnMapper extends BaseMapper<NodesCurdColumn> {

}
