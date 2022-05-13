
package org.nodes.wms.dao.basics.sku.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.math.BigDecimal;

/**
 * 包装明细实体类
 */
@Data
@TableName("wms_sku_package_detail")
public class SkuPackageDetail extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 包装关系ID
	 */
	@TableId(value = "wspd_id", type = IdType.ASSIGN_ID)
	private Long wspdId;

	/**
	 * 包装ID
	 */
	private Long wspId;

	/**
	 * 层级
	 */
	private Integer skuLevel;

	/**
	 * 计量单位id
	 */
	private Long wsuId;

	/**
	 * 计量单位编码
	 */
	private String wsuCode;

	/**
	 * 计量单位名称
	 */
	private String wsuName;

	/**
	 * 换算倍数
	 */
	private Integer convertQty;

	/**
	 * 重量
	 */
	private BigDecimal lpnWeight;

	/**
	 * 长度
	 */
	private BigDecimal lpnLength;

	/**
	 * 宽度
	 */
	private BigDecimal lpnWidth;

	/**
	 * 高度
	 */
	private BigDecimal lpnHeight;

	/**
	 * RFID筛选值
	 */
	private String filterValue;

	/**
	 * RFID指示符位数
	 */
	private BigDecimal indicatorDigit;

	/**
	 * 物品规格
	 */
	private String skuSpec;
}
