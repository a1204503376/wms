
package org.nodes.wms.core.count.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 盘点单记录表实体类
 *
 * @author chz
 * @since 2020-02-20
 */
@Data
@TableName("wms_count_record")
@ApiModel(value = "CountRecord对象", description = "盘点单记录表")
public class CountRecord extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 盘点单记录ID
	 */
	@ApiModelProperty(value = "盘点单记录ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wcr_id", type = IdType.ASSIGN_ID)
	private Long wcrId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 盘点单ID
	 */
	@ApiModelProperty(value = "盘点单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long countBillId;
	/**
	 * 单据编码
	 */
	@ExcelProperty("盘点单编码")
	@ApiModelProperty(value = "单据编码")
	private String countBillNo;
	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	private Long stockId;
	/**
	 * 库位ID
	 */
	@ApiModelProperty(value = "库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	@ExcelProperty("库位编码")
	@ApiModelProperty(value = "库位编码")
	private String locCode;
	/**
	 * 任务分组编码
	 */
	@ExcelProperty("任务分组编码")
	@ApiModelProperty(value = "任务分组编码")
	private String taskGroup;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ExcelProperty("物品编码")
	@ApiModelProperty(value = "物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ExcelProperty("物品名称")
	@ApiModelProperty(value = "物品名称")
	private String skuName;
	/**
	 * 盘点数量
	 */
	@ExcelProperty("盘点数量")
	@ApiModelProperty(value = "盘点数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal countQty;
	/**
	 * 实际库存量
	 */
	@ExcelProperty("实际库存量")
	@ApiModelProperty(value = "实际库存量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal stockQty;
	/**
	 * 包装ID
	 */
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;
	/**
	 * 计量单位名称
	 */
	@ExcelProperty("计量单位名称")
	@ApiModelProperty(value = "计量单位名称")
	private String wsuName;
	/**
	 * 层级
	 */
	@ExcelProperty("层级")
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
	/**
	 * 换算倍率
	 */
	@ExcelProperty("换算倍率")
	@ApiModelProperty(value = "换算倍率")
	private Integer convertQty;
	/**
	 * 盘点序列号
	 */
	@ExcelProperty("序列号")
	@ApiModelProperty(value = "盘点序列号")
	private String serialNumber;
	/**
	 * 盘点时间
	 */
	@ExcelProperty("盘点时间")
	@ApiModelProperty(value = "盘点时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime procTime;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;
	/**
	 * 用户名称
	 */
	@ExcelProperty("用户名称")
	@ApiModelProperty(value = "用户名称")
	private String userName;
	/**
	 * 盘点状态
	 */
	@ExcelProperty("盘点状态")
	@ApiModelProperty(value = "盘点状态")
	private Integer recordState;
	/**
	 * 批次号
	 */
	@ExcelProperty("批次号")
	@ApiModelProperty(value = "批次号")
	private String lotNumber;
}
