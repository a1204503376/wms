package org.nodes.wms.dao.basics.suppliers.dto.input;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 供应商单创建请求对象
 */
@Data
public class AddSupplierRequest implements Serializable {

	private static final long serialVersionUID = -743345718924445728L;

	/**
	 * 供应商编码
	 */
	@NotNull(message = "供应商编码不能为空")
	private String code;

	/**
	 * 供应商名称
	 */
	@NotNull(message = "供应商名称不能为空")
	private String name;

	/**
	 * 供应商简称
	 */
	private String simpleName;

	/**
	 * 货主id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 是否启用(1:启用,-1:未启用)
	 */
	private Integer status;
}
