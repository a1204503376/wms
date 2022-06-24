package org.nodes.wms.dao.instock.receive.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.instock.receive.enums.ReceiveHeaderStateEnum;
import org.springblade.core.tenant.mp.TenantEntity;

import java.util.Date;

/**
 * 收货单表 实体类
 **/
@Data
@TableName("receive_header")
public class ReceiveHeader extends TenantEntity {

	private static final long serialVersionUID = 6132977341273179994L;
	/**
	 * 收货单主键id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long receiveId;

	/**
	 * 收货单编码
	 */
	private String receiveNo;

	/**
	 * ASN单主键id
	 */
	private Long asnBillId;

	/**
	 * ASN单据编码
	 */
	private String asnBillNo;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 库房编码
	 */
	private String whCode;

	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 货主编码
	 */
	private String ownerCode;

	/**
	 * 单据状态
	 */
	private ReceiveHeaderStateEnum billState;

	/**
	 * 单据种类编码
	 */
	private String billTypeCd;

	/**
	 * 入库方式
	 */
	private Integer inStoreType;
	/**
	 * 供应商id
	 */
	private long supplierId;

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
	private String supplierAddress;

	/**
	 * 部门id
	 */
	private Long deptId;

	/**
	 * 部门名称
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
	 * 组合单据id
	 */
	private Long billGroupId;

	/**
	 * 预计到货时间
	 */
	private Date scheduledArrivalDate;

	/**
	 * 实际到货时间
	 */
	private Date actualArrivalDate;

	/**
	 * 单据完成实际
	 */
	private Date finishDate;

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
	 * WMS备注
	 */
	private String remark;
	/**
	 * 租户id
	 */
	private String tenantId;
}
