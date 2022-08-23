package org.nodes.wms.dao.tianyi.skubox.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * @author nodesc
 */
@Data
@TableName("tianyi_sku_box")
public class SkuBox extends TenantEntity {

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 物品型号
	 */
	private String skuSpec;
}
