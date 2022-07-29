package org.nodes.wms.dao.instock.asn.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.nodes.wms.dao.instock.asn.enums.InStorageTypeEnum;
import org.springblade.core.tenant.mp.TenantEntity;

import java.util.Date;

/**
 * ASN单据头表 实体
 */
@Data
@TableName(value = "asn_header")
public class AsnHeader extends TenantEntity {

	private static final long serialVersionUID = -7111744736263197174L;

	/**
	 * 主键
	 */
	@TableId(value = "asn_bill_id", type = IdType.ASSIGN_ID)
	private Long asnBillId;

	/**
	 * 库房ID
	 */
	private Long whId;

	/**
	 * 库房编码
	 */
	private String whCode;

	/**
	 * 货主ID
	 */
	private Long woId;

	/**
	 * 货主编码
	 */
	private String ownerCode;

	/**
	 * 单据编码
	 */
	private String asnBillNo;

	/**
	 * 单据状态
	 */
	private AsnBillStateEnum asnBillState;

	/**
	 * 组合单据ID
	 */
	private Long billGroupId;

	/**
	 * 单据种类编码
	 */
	private String billTypeCd;

	/**
	 * 上位系统单据唯一标识
	 */
	private String externalBillKey;

	/**
	 * 上位系统单编号
	 */
	private String externalOrderNo;

	/**
	 * 上位系统最后更新时间
	 */
	private Date externalLastUpdateDate;

	/**
	 * 上位系统订单创建时间
	 */
	private Date externalPreCreateDate;

	/**
	 * 上位系统单据类型
	 */
	private String externalBillType;

	/**
	 * 上位系统单据创建人
	 */
	private String externalCreateUser;

	/**
	 * 预计到货时间
	 */
	private Date scheduledArrivalDate;

	/**
	 * 实际到货时间
	 */
	private Date actualArrivalDate;

	/**
	 * 单据完成时间
	 */
	private Date finishDate;

	/**
	 * 入库方式
	 */
	private InStorageTypeEnum instoreType;

	/**
	 * 供应商id
	 */
	private Long supplierId;

	/**
	 * 供应商编码
	 */
	private String supplierCode;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 供应商地址
	 */
	private String sAddress;

	/**
	 * 供应商联系人
	 */
	private String contact;

	/**
	 * 供应商联系电话
	 */
	private String phone;

	/**
	 * WMS备注
	 */
	private String asnBillRemark;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 部门编码
	 */
	private String deptCode;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 创建类型
	 */
	private Integer createType;
}
