
package org.nodes.wms.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.system.entity.BarcodeRule;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *  条码规则Mapper 接口
 *
 * @author NodeX
 * @since 2019-12-16
 */
@Primary
public interface BarcodeRuleMapper extends BaseMapper<BarcodeRule> {

}
