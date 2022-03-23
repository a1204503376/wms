package org.nodes.wms.core.billing.mapper;

import org.nodes.wms.core.billing.entity.BillingRuleDetail;
import org.nodes.wms.core.billing.vo.BillingRuleDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 计费规则明细 Mapper 接口
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Primary
public interface BillingRuleDetailMapper extends BaseMapper<BillingRuleDetail> {

}
