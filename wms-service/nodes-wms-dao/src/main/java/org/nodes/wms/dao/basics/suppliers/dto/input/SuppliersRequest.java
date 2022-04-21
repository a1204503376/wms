package org.nodes.wms.dao.basics.suppliers.dto.input;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

/**
 * 供应商单创建请求对象
 */
@Data
public class SuppliersRequest {
	/**
	 * 供应商id
	 */
	private Long id;

	/**
	 * 供应商编码
	 */
	private String code;

	/**
	 * 供应商名称
	 */
	private String name;

	/**
	 * 供应商简称
	 */
	private String simpleName;

	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 是否启用(0:启用,-1:未启用)
	 */
	private Integer status;

	/**
	 * 租户id
	 */
	private Long tenantId;

	/**
	 * 创建部门
	 */
	private Long createDept;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新人
	 */
	private Long updateUser;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 删除标记(0:有效，1:删除)
	 */
	private Integer isDeleted;
}
