package org.nodes.wms.dao.outstock.soPickPlan.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询拣货计划dto
 **/
@Data
public class FindPickPlanBySoBillIdAndBoxCodeRequest implements Serializable {
	/**
	 * 发货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 箱号
	 */
	private String boxCode;

}
