package org.nodes.wms.dao.basics.lpntype.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeEnum;
import org.springblade.core.tenant.mp.TenantEntity;

import java.math.BigDecimal;

/**
 *容器实体类对象
 */
@Data
@TableName(value = "wms_lpn_type")
public class LpnType extends TenantEntity {
	private static final long serialVersionUID = 6574893516485818284L;
	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 库房ID
	 */
	private Long whId;
	/**
	 * 容器类型编码
	 */
	private String code;
	/**
	 * 容器类型(1:箱,2:托)
	 */
	private LpnTypeEnum type;
	/**
	 * 容器编码生成规则
	 */
	private String lpnNoRule;
	/**
	 * 容器重量(KG)
	 */
	private BigDecimal weight;
}
