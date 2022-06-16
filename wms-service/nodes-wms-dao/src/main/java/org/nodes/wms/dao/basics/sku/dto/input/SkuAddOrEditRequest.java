package org.nodes.wms.dao.basics.sku.dto.input;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.dao.basics.sku.entities.SkuReplace;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 物品新增或编辑dto对象
 **/
@Data
public class SkuAddOrEditRequest implements Serializable {

	private static final long serialVersionUID = -8436007335086048609L;

	/**
	 * 物品ID
	 */
	private Long skuId;

	/**
	 * 货主ID
	 */
	@NotNull(message = "货主id不能为空")
	private Long woId;

	/**
	 * 物品分类ID（默认其它）
	 */
	@NotNull(message = "物品分类id不能为空")
	private Long skuTypeId;

	/**
	 * 包装ID
	 */
	@NotNull(message = "包装id不能为空")
	private Long wspId;

	/**
	 * 物品批属性设置ID
	 */
	@NotNull(message = "批属性id不能为空")
	private Long wslId;

	/**
	 * 物品批属性验证ID
	 */
	@NotNull(message = "批属性验证id不能为空")
	private Long wslvId;

	/**
	 * 物品编码
	 */
	@Length(max = 50, message = "字符长度不能超过50")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuName;

	/**
	 * 物品名称简称
	 */
	@Length(max = 100, message = "字符长度不能超过100")
	private String skuNameS;

	/**
	 * 物品说明
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuRemark;

	/**
	 * 物品条码清单
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuBarcodeList;

	/**
	 * 存货类型
	 */
	private Integer skuStorageType;

	/**
	 * 序列号管理（1：序列号管理  0：非序列号管理）
	 */
	private Integer isSn;

	/**
	 * ABC分类
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
	 * 保质期小时数
	 */
	private Integer qualityHours;

	/**
	 * 临期阈值
	 */
	private Integer attribute3;

	/**
	 * 物品替代集合
	 */
	private List<SkuReplace> skuReplaceList;

	/**
	 * 删除的物品替代id集合
	 */
	private List<Long> skuReplaceIdList;

	/**
	 * 物品供应商关联集合
	 */
	private List<SkuInc> skuIncList;

	/**
	 * 删除的物品供应商id集合
	 */
	private List<Long> skuIncIdList;
}
