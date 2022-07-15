package org.nodes.wms.dao.instock.receive.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * pc收货头表返回前端dto
 */
@Data
public class ReceiveByPcResponse implements Serializable {
	private static final long serialVersionUID = 93643663826103813L;
	/**
	 * 收货单主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveId;
	/**
	 * 收货单编码
	 */
	private String receiveNo;
	/**
	 * 上位系统单编号
	 */
	private String externalOrderNo;
	/**
	 * 库房编码
	 */
	private String whCode;
}
