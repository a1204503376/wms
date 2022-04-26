package org.nodes.wms.dao.instock.asn.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.time.LocalDateTime;

/**
 * ASN单据头表 实体
 */
@Data
@TableName(value = "asn_header")
public class AsnHeader extends TenantEntity {

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
	private Integer asnBillState;
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
	private String billKey;
	/**
	 * 上位系统单编号
	 */
	private String orderNo;
	/**
	 * 上位系统最后更新时间
	 */
	private LocalDateTime lastUpdateDate;
	/**
	 * 上位系统订单创建时间
	 */
	private LocalDateTime preCreateDate;
	/**
	 * 上位系统单据类型
	 */
	private String billType;
	/**
	 * 预计到货时间
	 */
	private LocalDateTime scheduledArrivalDate;
	/**
	 * 实际到货时间
	 */
	private LocalDateTime actualArrivalDate;
	/**
	 * 单据完成时间
	 */
	private LocalDateTime finishDate;
	/**
	 * 入库方式
	 */
	private Integer instoreType;
	/**
	 * 运单编号
	 */
	private String shipNo;
	/**
	 * 供应商编码
	 */
	private String sCode;
	/**
	 * 供应商名称
	 */
	private String sName;
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
	 * 出库单编码
	 */
	private String soBillNo;
	/**
	 * WMS备注
	 */
	private String asnBillRemark;
	/**
	 * 同步状态
	 */
	private Integer syncState;
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
	/**
	 * 过账人员
	 */
	private Long postUser;
	/**
	 * 过账时间
	 */
	private LocalDateTime postTime;
	/**
	 * 过账方式
	 */
	private Integer postState;
}
