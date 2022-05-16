package org.nodes.wms.dao.basics.bom.entites;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物料清单
 */
@Data
@TableName("wms_sku_bom")
public class WmsSkuBom extends TenantEntity implements Serializable {
	private static final long serialVersionUID = -7884571229295209020L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 货主ID
	 */
	private Long woId;
	/**
	 * 货主编码
	 */
	private String ownerCode;
	/**
	 * 货主名称
	 */
	private String ownerName;
	/**
	 * 物品ID
	 */
	private Long skuId;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 物品名称
	 */
	private String skuName;
	/**
	 * 主单位编码
	 */
	private String wsuCode;
	/**
	 * 主单位名称
	 */
	private String wsuName;
	/**
	 * 组合物品ID
	 */
	private Long joinSkuId;
	/**
	 * 组合物品编码
	 */
	private String joinSkuCode;
	/**
	 * 组合物品名称
	 */
	private String joinSkuName;
	/**
	 * 组合数量
	 */
	private BigDecimal qty;
	/**
	 * 组合单位编码
	 */
	private String joinWsuCode;
	/**
	 * 组合单位名称
	 */
	private String joinWsuName;
	/**
	 * 备注
	 */
	private String remark;

}
