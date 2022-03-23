
package org.nodes.wms.core.relenishment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.springframework.context.annotation.Primary;

/**
 * 补货表头 Mapper 接口
 *
 * @author whj
 * @since 2019-12-24
 */
@Primary
public interface RelDetailMapper extends BaseMapper<RelDetail> {

}
