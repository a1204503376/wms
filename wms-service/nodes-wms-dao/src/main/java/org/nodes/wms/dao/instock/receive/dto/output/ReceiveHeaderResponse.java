package org.nodes.wms.dao.instock.receive.dto.output;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.nodes.wms.dao.instock.receive.enums.ReceiveBillStateEnum;

import java.io.Serializable;
import java.util.Date;
/**
 * 收货单头表 返回前端视图类
 **/
@Data
public class ReceiveHeaderResponse implements Serializable {
	private static final long serialVersionUID = -5518471031357656151L;
	/**
	 * 收货单主键id
	 */
	@ExcelIgnore
	private Long receiveId;
	/**
	 * 收货单编码
	 */
	@ExcelProperty("收货单编码")
	private String receiveNo;
	/**
	 * ASN单据编码
	 */
	@ExcelProperty("ASN单据编码")
	private String asnBillNo;
	/**
	 * 库房编码
	 */
	@ExcelProperty("库房编码")
	private String whCode;
	/**
	 * 单据状态枚举
	 */
	@ExcelIgnore
	private ReceiveBillStateEnum billState;
	/**
	 * 单据状态描述
	 */
	@ExcelProperty("单据状态")
	private String billStateDesc;

	/**
	 * 货主编码
	 */
	@ExcelProperty("货主编码")
	private Long ownerCode;
	/**
	 * 入库方式
	 */
	@ExcelProperty("入库方式")
	private Integer inStoreType;
	/**
	 * WMS备注
	 */
	@ExcelProperty("WMS备注")
	private String remark;

	/**
	 * 供应商编码
	 */
	@ExcelProperty("供应商编码")
	private String supplierCode;
	/**
	 * 供应商名称
	 */
	@ExcelProperty("供应商名称")
	private String supplierName;
	/**
	 * 单据类型
	 */
	@ExcelProperty("单据类型")
	private String billTypeCd;
	/**
	 * 上位系统单编号
	 */
	@ExcelProperty("上位系统单编号")
	private String externalOrderNo;

	/**
	 * 上位系统订单创建时间
	 */
	@ExcelProperty("上位系统订单创建时间")
	private Date externalPreCreateDate;

	/**
	 * 上位系统单据创建人
	 */
	@ExcelProperty("上位系统单据创建人")
	private String externalCreateUser;
	/**
	 * 创建人
	 */
	@ExcelProperty("创建人")
	private String createUser;

	/**
	 * 创建时间
	 */
	@ExcelProperty("创建时间")
	private Date createTime;
	/**
	 * 更新人
	 */
	@ExcelProperty("更新人")
	private String updateUser;
	/**
	 * 更新时间
	 */
	@ExcelProperty("更新时间")
	private Date updateTime;
}
