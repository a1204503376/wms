package org.nodes.wms.dao.instock.asn.dto.output;

import lombok.Data;
import lombok.ToString;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;

import java.util.Date;
import java.util.List;

/**
 * Asn 单详细信息
 *
 * @author 彭永程
 * @date 2022-04-22 14:03
 **/
@Data
public class AsnDetailResponse {

	/**
	 * 主键
	 */
	private Long asnBillId;

	/**
	 * Asn单编码
	 */
	private String asnBillNo;

	/**
	 * 单据类型
	 */
	private Integer createType;

	/**
	 * 供应商编码
	 */
	private String sCode;

	/**
	 * 供应商名称
	 */
	private String sName;

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
	private Long createUser;

	/**
	 * Asn单明细集合
	 */
	private List<AsnDetail>  asnDetails;

	/**
	 * 收货单头表信息集合
	 */
	private List<ReceiveHeader> receiveHeaders;

	/**
	 * 库房编码
	 */
	private String whCode;

	/**
	 * 库房名称
	 */
	private String whName;
}
