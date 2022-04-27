package org.nodes.wms.dao.instock.asn.dto.output;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Asn 单详细信息
 *
 * @author 彭永程
 * @date 2022-04-22 14:03
 **/
@Data
public class AsnDetailResponse implements Serializable {

	private static final long serialVersionUID = -9097169466236888793L;
	/**
	 * 主键
	 */
	@TableId
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
	private List<AsnPropertyDetailResponse>  asnDetails;

	/**
	 * 收货单头表信息集合
	 */
	private List<AsnPropertyReceiveResponse> receiveHeaders;

	/**
	 * 库房编码
	 */
	private String whCode;

	/**
	 * 库房名称
	 */
	private String whName;
}
