
package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.springframework.context.annotation.Primary;

/**
 * 包装ID Mapper 接口
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Primary
public interface SkuPackageMapper extends BaseMapper<SkuPackage> {

}
