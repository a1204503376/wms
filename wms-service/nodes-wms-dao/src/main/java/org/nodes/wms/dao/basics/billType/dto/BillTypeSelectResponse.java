package org.nodes.wms.dao.basics.billType.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 单据类型下拉选择 返回对象
 */
@Data
public class BillTypeSelectResponse implements Serializable {

	private static final long serialVersionUID = 8756422790654598576L;

	/**
	 * 单据类型主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billTypeId;

	/**
	 * 单据类型编码
	 */
	private String billTypeCd;

	/**
	 * 单据类型名称
	 */
	private String billTypeName;
}
