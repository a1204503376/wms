package org.nodes.wms.dao.receive.header.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * 收货单头表 返回前端视图类
 **/
@Data
public class HeaderResponse implements Serializable {
	/**
	 * 收货单主键id
	 */
	private Long receiveId;
	/**
	 * 收货单编码
	 */
	private String receiveNo;
	/**
	 * ASN单据编码
	 */
	private String asnBillNo;
	/**
	 * 库房编码
	 */
	private String whCode;

	/**
	 * 单据状态
	 */
	private Integer billState;
	/**
	 * 货主编码
	 */
	private Long ownerCode;
	/**
	 * 入库方式
	 */
	private Integer instoreType;
	/**
	 * WMS备注
	 */
	private String remark;

	/**
	 * 供应商编码
	 */
	private String sCode;
	/**
	 * 供应商名称
	 */
	private String sName;
	/**
	 * 单据类型
	 */
	private String billTypeCd;
	/**
	 * 上位系统单编号
	 */
	private String externalOrderNo;

	/**
	 * 上位系统订单创建时间
	 */
	private Date externalPreCreateDate;

	/**
	 * 上位系统单据创建人
	 */
	private String externalCreateUser;
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
