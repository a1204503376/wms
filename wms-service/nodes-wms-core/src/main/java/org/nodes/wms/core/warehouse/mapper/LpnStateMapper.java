package org.nodes.wms.core.warehouse.mapper;

import org.nodes.wms.core.warehouse.entity.LpnState;
import org.nodes.wms.core.warehouse.vo.LpnStateVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 容器状态表 Mapper 接口
 *
 * @author NodeX
 * @since 2021-10-12
 */
@Primary
public interface LpnStateMapper extends BaseMapper<LpnState> {

}
