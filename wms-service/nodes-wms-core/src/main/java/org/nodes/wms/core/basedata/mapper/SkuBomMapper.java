package org.nodes.wms.core.basedata.mapper;

import org.nodes.wms.dao.basics.bom.entites.SkuBom;
import org.nodes.wms.core.basedata.vo.SkuBomVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 物料清单 Mapper 接口
 *
 * @author NodeX
 * @since 2021-05-19
 */
@Primary
public interface SkuBomMapper extends BaseMapper<SkuBom> {

}
