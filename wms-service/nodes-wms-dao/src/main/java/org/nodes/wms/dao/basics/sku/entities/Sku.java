package org.nodes.wms.dao.basics.sku.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.math.BigDecimal;

/**
 * 物品实体类
 *
 * @author pengwei
 * @since 2019-12-09
 */
@Data
@TableName("wms_sku")
public class Sku extends TenantEntity {

	private static final long serialVersionUID = -4755521835893759438L;

	/**
	 * 物品ID
	 */
	@TableId(value = "sku_id", type = IdType.ASSIGN_ID)
	private Long skuId;

	/**
	 * 货主ID
	 */
	private Long woId;

	/**
	 * 物品分类ID（默认其它）
	 */
	private Long skuTypeId;

	/**
	 * 包装ID
	 */
	private Long wspId;

	/**
	 * 物品批属性设置ID
	 */
	private Long wslId;

	/**
	 * 物品批属性验证ID
	 */
	private Long wslvId;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 物品名称简称
	 */
	private String skuNameS;

	/**
	 * 物品名称（英文）
	 */
	private String skuNameEn;

	/**
	 * 物品说明
	 */
	private String skuRemark;

	/**
	 * 物品条码清单
	 */
	private String skuBarcodeList;

	/**
	 * 物品名称拼音
	 */
	private String skuNamePy;

	/**
	 * 存货类型
	 */
	private Integer skuStorageType;

	/**
	 * abc分类；对应字典code=abc
	 */
	private Integer abc;

	/**
	 * 体积
	 */
	private BigDecimal skuVolume;

	/**
	 * 净重
	 */
	private BigDecimal skuNetWeight;

	/**
	 * 皮重
	 */
	private BigDecimal skuTareWeight;

	/**
	 * 毛重
	 */
	private BigDecimal skuGrossWeight;

	/**
	 * 温度上限
	 */
	private BigDecimal skuTempUpperLimit;

	/**
	 * 温度下限
	 */
	private BigDecimal skuTempLowerLimit;

	/**
	 * 物品标准价
	 */
	private BigDecimal skuPrice;

	/**
	 * 保质期有无
	 */
	private String qualityType;

	/**
	 * 几天内截至
	 */
	private Integer qualityBy;

	/**
	 * 几天内交货
	 */
	private Integer qualityTransport;

	/**
	 * 几天内最佳
	 */
	private Integer qualityBest;

	/**
	 * 保质期日期类型
	 */
	private Integer qualityDateType;

	/**
	 * 保质期小时数
	 */
	private Integer qualityHours;

	/**
	 * 批次号生成掩码
	 */
	private String skuLotMask;

	/**
	 * 子级物品
	 */
	private String isSublevel;

	/**
	 * 序列号管理（1：序列号管理  0：非序列号管理）
	 */
	private Integer isSn;

	/**
	 * 存货类型
	 */
	private Integer inventoryType;

	/**
	 * 总货架寿命
	 */
	private Integer totalShelf;

	/**
	 * 附件
	 */
	private Integer appendix;

	/**
	 * 安全库存
	 */
	private BigDecimal safeStock;

	/**
	 * 最低储量
	 */
	private BigDecimal minimumReserves;

}
