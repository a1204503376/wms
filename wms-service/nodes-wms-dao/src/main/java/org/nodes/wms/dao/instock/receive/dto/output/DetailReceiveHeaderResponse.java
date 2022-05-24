package org.nodes.wms.dao.instock.receive.dto.output;

import lombok.Data;
import org.nodes.wms.dao.instock.asn.enums.InStorageTypeEnum;
import org.nodes.wms.dao.instock.receive.enums.ReceiveBillStateEnum;

import java.io.Serializable;

/**
 * 查看详情页面收货单头表返回前端视图类
 **/
@Data
public class DetailReceiveHeaderResponse implements Serializable {
	private static final long serialVersionUID = 6877166016829733238L;
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
	 * 单据状态枚举
	 */
	private ReceiveBillStateEnum billState;
	/**
	 * 单据状态描述
	 */
	private String billStateDesc;

	/**
	 * 货主编码
	 */
	private String ownerCode;
	/**
	 * 入库方式
	 */
	private InStorageTypeEnum inStoreType;
	/**
	 * 入库方式描述
	 */
	private String inStoreTypeDesc;
	/**
	 * WMS备注
	 */
	private String remark;

	/**
	 * 供应商编码
	 */
	private String supplierCode;
	/**
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 单据类型
	 */
	private String billTypeCd;
	/**
	 * 单据类型名称
	 */
	private String billTypeName;
}
