package org.nodes.wms.dao.instock.asn.dto.output;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 返回对象-Asn单集合属性ReceiveHeader
 *
 * @author 彭永程
 * @date 2022-04-27 10:25
 **/
@Data
@EqualsAndHashCode
public class AsnPropertyReceiveResponse implements Serializable {

	private static final long serialVersionUID = -2138640940190251249L;

	/**
	 * 收货单主键id
	 */
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
