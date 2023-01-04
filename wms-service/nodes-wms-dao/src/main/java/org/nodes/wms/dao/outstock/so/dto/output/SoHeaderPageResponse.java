package org.nodes.wms.dao.outstock.so.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 发货单头表分页响应类
 **/
@Data
public class SoHeaderPageResponse implements Serializable {

	private static final long serialVersionUID = -748521511328808636L;

	/**
	 * 发货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 发货单编码
	 */
	private String soBillNo;

	/**
	 * 上游编码
	 */
	private String orderNo;

	/**
	 * 单据类型名称
	 */
	private String billTypeName;

	/**
	 * 单据状态
	 */
	private SoBillStateEnum soBillState;

	/**
	 * 客户编码
	 */
	private String customerCode;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 发货单备注
	 */
	private String soBillRemark;
	/**
	 * 物流编码
	 */
	private String expressCode;
	/**
	 * 收货人地址
	 */
	private String address;
}
