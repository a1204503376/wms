package org.nodes.wms.core.billing.mapper;

import org.nodes.wms.core.billing.entity.BillingItem;
import org.nodes.wms.core.billing.vo.BillingItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 计费项目 Mapper 接口
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Primary
public interface BillingItemMapper extends BaseMapper<BillingItem> {

}
