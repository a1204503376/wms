package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.springframework.context.annotation.Primary;

/**
 * 单据类型 Mapper 接口
 *
 * @author NodeX
 * @since 2019-12-24
 */
@Primary
public interface BillTypeMapper extends BaseMapper<BillType> {


}
