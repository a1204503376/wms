package org.nodes.wms.dao.outstock.so.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerSelectResponse;

import java.io.Serializable;

/**
 * 出库单编辑头表响应类
 **/
@Data
public class SoHeaderEditResponse implements Serializable {

	private static final long serialVersionUID = 5266375176744911934L;

	/**
	 * 出库单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 出库单编码
	 */
	private String soBillNo;

	/**
	 * 单据类型名称
	 */
	private String billTypeCd;

	/**
	 * 所属库房
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 所属货主
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;

	/**
	 * 客户
	 */
	private CustomerSelectResponse customer;

	/**
	 * 出库方式
	 */
	private Integer outstockType;

	/**
	 * 发货方式
	 */
	private Integer transportCode;

	/**
	 * 备注
	 */
	private String soBillRemark;
}
