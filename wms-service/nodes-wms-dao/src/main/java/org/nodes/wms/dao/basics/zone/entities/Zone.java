package org.nodes.wms.dao.basics.zone.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 库区实体类
 */
@Data
@TableName("wms_zone")
public class Zone extends TenantEntity {

	private static final long serialVersionUID = -2452913967272954380L;

	/**
	 * 库区id
	 */
	@TableId(value = "zone_id", type = IdType.ASSIGN_ID)
	private Long zoneId;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 库区编码
	 */
	private String zoneCode;

	/**
	 * 库区名称
	 */
	private String zoneName;

	/**
	 * 库区类型
	 */
	private Integer zoneType;

}
