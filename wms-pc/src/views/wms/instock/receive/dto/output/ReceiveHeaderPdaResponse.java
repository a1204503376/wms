package org.nodes.wms.dao.instock.receive.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 收货单列表页面收货单头表 返回前端视图类
 **/
@Data
public class ReceiveHeaderPdaResponse implements Serializable {
	private static final long serialVersionUID = 7402283739484471313L;
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
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 单据类型名称
	 */
	private String billTypeName;
}
