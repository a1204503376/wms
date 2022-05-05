package org.nodes.wms.dao.basics.suppliers.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ASN单分页结果
 */
@Data
public class SupplierPageResponse implements Serializable {

	private static final long serialVersionUID = -8182397831756613115L;

	/**
	 * 供应商id
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

	/**
	 * 供应商简称
	 */
	private String simpleName;

	/**
	 * 货主
	 */
	private String ownerName;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 是否启用(0:启用,-1:未启用)
	 */
	private Integer status;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;
}
