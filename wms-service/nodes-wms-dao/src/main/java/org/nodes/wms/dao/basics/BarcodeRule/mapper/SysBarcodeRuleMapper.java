package org.nodes.wms.dao.basics.BarcodeRule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.BarcodeRule.entities.SysBarcodeRule;
import org.springframework.stereotype.Repository;

@Repository("SysBarcodeRuleRepository")
public interface SysBarcodeRuleMapper extends BaseMapper<SysBarcodeRule> {
}
