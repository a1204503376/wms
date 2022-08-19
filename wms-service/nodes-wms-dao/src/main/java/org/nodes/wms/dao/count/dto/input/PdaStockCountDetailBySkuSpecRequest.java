package org.nodes.wms.dao.count.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;

import java.io.Serializable;
import java.util.List;

/**
 * PDA显示盘点单搜索请求对象
 */
@Data
public class PdaStockCountDetailBySkuSpecRequest extends SkuLot implements Serializable {

	private static final long serialVersionUID = -491831578862395846L;
	/**
	 * 变动类型
	 */
	private List<Integer> changeType;

	/**
	 * 盘点单编码
	 */
	private String countBillNo;

	/**
	 * 盘点方式(0:按物品盘点, 1:按库位盘点)
	 */
	private Integer countBy;

	/**
	 * 差异报告
	 */
	private List<CountRecordView> countReportVOList;

	/**
	 * 盘点标签
	 */
	private Integer countTag;

	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 库区ID数组
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private List<Long> zoneId;
}
