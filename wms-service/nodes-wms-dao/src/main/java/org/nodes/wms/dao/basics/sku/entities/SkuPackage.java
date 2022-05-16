
package org.nodes.wms.dao.basics.sku.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.math.BigDecimal;

/**
 * 包装实体类

 */
@Data
@TableName("wms_sku_package")
public class SkuPackage extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 包装ID
	 */
	@TableId(value = "wsp_id", type = IdType.ASSIGN_ID)
	private Long wspId;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 包装名称
	 */
	private String wspName;

	/**
	 * 包装规格
	 */
	private String spec;

	/**
	 * 上线包装名称
	 */
	private String onlinePackage;

	/**
	 * 上线包装类型
	 */
	private String packageType;

	/**
	 * 托盘每层箱数
	 */
	private BigDecimal palletBoxLevel;

	/**
	 * 每托盘层数
	 */
	private BigDecimal palletLevel;

	/**
	 * 每托盘重量
	 */
	private BigDecimal lpnWeight;

	/**
	 * 每托盘高度
	 */
	private BigDecimal lpnHeight;

	/**
	 * 每托盘长度
	 */
	private BigDecimal lpnLength;

	/**
	 * 每托盘宽度
	 */
	private BigDecimal lpnWidth;

	/**
	 * 是否默认包装 1表示默认
	 */
	private Integer isDefault;
}
