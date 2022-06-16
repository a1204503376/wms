package org.nodes.wms.dao.basics.sku.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.validation.Excel;
import org.springblade.core.tenant.mp.TenantEntity;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 实体类
 *
 * @author NodeX
 * @since 2019-12-17
 */
@Data
@TableName("wms_sku_package_detail")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SkuPackageDetail对象", description = "SkuPackageDetail对象")
public class SkuPackageDetail extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 包装关系ID
	 */
	@ApiModelProperty(value = "包装关系ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wspd_id", type = IdType.ASSIGN_ID)
	private Long wspdId;
	/**
	 * 包装ID
	 */
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
	/**
	 * 计量单位id
	 */
	@ApiModelProperty(value = "计量单位id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsuId;
	/**
	 * 计量单位编码
	 */
	@ApiModelProperty(value = "计量单位编码")
	@NotBlank(message = "计量单位编码不能为空!", groups = {Excel.class})
	private String wsuCode;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty(value = "计量单位名称")
	private String wsuName;
	/**
	 * 换算倍数
	 */
	@ApiModelProperty(value = "换算倍数")
	private Integer convertQty;
	/**
	 * 重量
	 */
	@ApiModelProperty(value = "重量")
	private BigDecimal lpnWeight;
	/**
	 * 长度
	 */
	@ApiModelProperty(value = "长度")
	private BigDecimal lpnLength;
	/**
	 * 宽度
	 */
	@ApiModelProperty(value = "宽度")
	private BigDecimal lpnWidth;
	/**
	 * 高度
	 */
	@ApiModelProperty(value = "高度")
	private BigDecimal lpnHeight;
	/**
	 * RFID筛选值
	 */
	@ApiModelProperty(value = "RFID筛选值")
	private String filterValue;
	/**
	 * RFID指示符位数
	 */
	@ApiModelProperty(value = "RFID指示符位数")
	private BigDecimal indicatorDigit;
	/**
	 * 物品规格
	 */
	@ApiModelProperty(value = "物品规格")
	private String skuSpec;
}
