package org.nodes.wms.dao.count.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * PDA显示盘点单搜索结果对象
 */
@Data
public class PdaStockCountDetailBySkuSpecResponse extends SkuLot implements Serializable {

	private static final long serialVersionUID = -6620575369115340810L;

	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 库位ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
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
	 * 盘点单编码
	 */
	private String countBillNo;

	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	/**
	 * 上次盘点时间
	 */
	private LocalDateTime lastLocCountDate;

	/**
	 * 盘点单状态
	 */
	private Integer countBillState;

	/**
	 * 盘点单状态描述
	 */
	private String countBillStateName;
}
