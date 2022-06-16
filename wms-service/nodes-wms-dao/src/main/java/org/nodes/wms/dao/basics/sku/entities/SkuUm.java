package org.nodes.wms.dao.basics.sku.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 计量单位实体类
 **/
@Data
@TableName("wms_sku_um")
public class SkuUm extends TenantEntity {

	private static final long serialVersionUID = 5421019012701333555L;

	/**
	 * 计量单位id
	 */
	@TableId(value = "wsu_id", type = IdType.ASSIGN_ID)
	private Long wsuId;

	/**
	 * 计量单位编码
	 */
	private String	wsuCode;

	/**
	 * 计量单位名称
	 */
	private String wsuName;
}
