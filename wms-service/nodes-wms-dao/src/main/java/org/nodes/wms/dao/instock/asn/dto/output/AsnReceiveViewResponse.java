package org.nodes.wms.dao.instock.asn.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查看明细-收货单头表返回对象
 **/
@Data
public class AsnReceiveViewResponse implements Serializable {

	private static final long serialVersionUID = -2138640940190251249L;

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
	 * 单据状态
	 */
	private Integer billState;

	/**
	 * WMS备注
	 */
	private String remark;

	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新人
	 */
	private String updateUser;

	/**
	 * 更新时间
	 */
	private Date updateTime;
}
