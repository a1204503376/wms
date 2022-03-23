package org.nodes.wms.core.strategy.mapper;

import org.nodes.wms.core.strategy.entity.WellenDetail;
import org.nodes.wms.core.strategy.vo.WellenDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 波次策略明细 Mapper 接口
 *
 * @author NodeX
 * @since 2021-05-26
 */
@Primary
@Repository("stWellenDetailMapper")
public interface WellenDetailMapper extends BaseMapper<WellenDetail> {

}
