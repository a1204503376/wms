package org.nodes.wms.dao.instock.asn.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Asn 单详细信息
 **/
@Data
public class AsnDetailResponse implements Serializable {

	private static final long serialVersionUID = -9097169466236888793L;
	/**
	 * 主键
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnBillId;

	/**
	 * Asn单编码
	 */
	private String asnBillNo;

	/**
	 * 单据类型
	 */
	private String billTypeName;

	/**
	 * 供应商编码
	 */
	private String supplierCode;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 备注
	 */
	private String asnBillRemark;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * Asn单明细集合
	 */
	private List<AsnPropertyDetailResponse>  asnDetailList;

	/**
	 * 收货单头表信息集合
	 */
	private List<AsnPropertyReceiveResponse> receiveHeaderList;

	/**
	 * 库房编码
	 */
	private String whCode;

	/**
	 * 库房名称
	 */
	private String whName;
}
