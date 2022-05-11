package org.nodes.wms.dao.basics.suppliers.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应商下拉选择 返回对象
 */
@Data
public class SupplierSelectResponse implements Serializable {

	private static final long serialVersionUID = -8749267029129892588L;
	/**
	 * 供应商主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 供应商编码
	 */
	private String code;

	/**
	 * 供应商名称
	 */
	private String name;
}
