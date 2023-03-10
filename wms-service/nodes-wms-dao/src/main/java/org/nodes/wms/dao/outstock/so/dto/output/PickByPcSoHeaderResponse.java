package org.nodes.wms.dao.outstock.so.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * pc拣货头表返回前端dto
 */
@Data
public class PickByPcSoHeaderResponse implements Serializable {
	private static final long serialVersionUID = 5165070909448961923L;
	/**
	 * 发货单编码
	 */
	private String soBillNo;
	/**
	 * 上位系统单编号
	 */
	private String orderNo;
	/**
	 * 库房编码
	 */
	private String whCode;
	/**
	 * 库房id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
}
