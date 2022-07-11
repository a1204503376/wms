package org.nodes.wms.dao.outstock.so.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 发货单头表实体类
 */
@Data
@TableName("so_header")
public class SoHeader extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2407858613422203284L;

	/**
	 * 发货单ID
	 */
	@TableId(value = "so_bill_id", type = IdType.ASSIGN_ID)
	private Long soBillId;

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
	private String soBillNo;

	/**
	 * 单据状态
	 */
	private Integer soBillState;

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
	 * 上位系统单据编号
	 */
	private String orderNo;

	/**
	 * 上位系统备注
	 */
	private String soRemark;

	/**
	 * 上位系统最后更新时间
	 */
	private Date lastUpdateDate;

	/**
	 * 上位系统订单创建时间
	 */
	private Date preCreateDate;

	/**
	 * 预计发货日期
	 */
	private Date scheduledOutstockTime;

	/**
	 * 单据完成时间
	 */
	private Date finishDate;

	/**
	 * 发货方式
	 */
	private Integer transportCode;

	/**
	 * 出库方式
	 */
	private Integer outstockType;

	/**
	 * 发货完成时间
	 */
	private Date transportDate;

	/**
	 * 客户编号
	 */
	private String customerCode;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 收货人地址
	 */
	private String address;

	/**
	 * 收获联系人
	 */
	private String contact;

	/**
	 * 收货人联系电话
	 */
	private String phone;

	/**
	 * 客户地址经度
	 */
	private BigDecimal cXCoor;

	/**
	 * 客户地址纬度
	 */
	private BigDecimal cYCoor;

	/**
	 * 物流编码
	 */
	private String expressCode;

	/**
	 * 物流名称
	 */
	private String expressName;

	/**
	 * 物流电话
	 */
	private String expressPhone;

	/**
	 * 物流地址
	 */
	private String expressAddress;

	/**
	 * 物流地址经度
	 */
	private BigDecimal expressX;

	/**
	 * 物流地址纬度
	 */
	private BigDecimal expressY;

	/**
	 * 发货单备注
	 */
	private String soBillRemark;

	/**
	 * 单据创建人员名称
	 */
	private String billCreator;

	/**
	 * 同步状态
	 */
	private Integer syncState;

	/**
	 * 部门id
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
	private Date postTime;

	/**
	 * 过账方式
	 */
	private Integer postState;

	/**
	 * 发运状态
	 */
	private Integer shipState;

}
