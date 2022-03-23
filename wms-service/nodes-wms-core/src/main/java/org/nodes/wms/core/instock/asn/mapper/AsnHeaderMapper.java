package org.nodes.wms.core.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.springblade.core.datascope.annotation.DataAuth;
import org.springblade.core.datascope.enums.DataScopeEnum;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 收货单头表 Mapper 接口
 *
 * @author zx
 * @since 2019-12-12
 */
@Primary
public interface AsnHeaderMapper extends BaseMapper<AsnHeader> {
	/**
	 * 查询已完成订单量
	 *
	 * @param date 日期
	 * @return 订单量
	 */
	@DataAuth(type = DataScopeEnum.OWN_DEPT_CHILD)
	Map<String,Object> selectFinishBillCount(LocalDateTime date);

	/**
	 * 查询已完成订单的物品总数
	 *
	 * @param dateTime 指定日期
	 * @return 物品总数
	 */
	@DataAuth(type = DataScopeEnum.OWN_DEPT_CHILD)
	Map<String,Object> selectFinishSkuCount(LocalDateTime dateTime);
}
