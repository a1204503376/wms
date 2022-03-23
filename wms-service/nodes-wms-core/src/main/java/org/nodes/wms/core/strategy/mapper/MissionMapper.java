package org.nodes.wms.core.strategy.mapper;

import org.nodes.wms.core.strategy.entity.Mission;
import org.nodes.wms.core.strategy.vo.MissionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author NodeX
 * @since 2021-05-24
 */
@Primary
public interface MissionMapper extends BaseMapper<Mission> {

}
