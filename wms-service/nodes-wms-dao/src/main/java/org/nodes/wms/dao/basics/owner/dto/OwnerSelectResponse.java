package org.nodes.wms.dao.basics.owner.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 货主下拉选择 返回对象
 */
@Data
public class OwnerSelectResponse implements Serializable {

	private static final long serialVersionUID = -5760472836159389549L;

	/**
	 * 货主主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;

	/**
	 * 货主编码
	 */
	private String ownerCode;

	/**
	 * 货主名称
	 */
	private String ownerName;
}
