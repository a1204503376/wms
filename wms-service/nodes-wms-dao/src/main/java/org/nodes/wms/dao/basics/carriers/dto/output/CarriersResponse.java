package org.nodes.wms.dao.basics.carriers.dto.output;

import lombok.Data;

import java.util.Date;

/**
 * 承运商返回前端视图类
 **/
@Data
public class CarriersResponse {
	/**
	 * 承运商ID
	 */
	private Long id;
	/**
	 * 承运商编码
	 */
	private String code;
	/**
	 * 承运商名称
	 */
	private String name;
	/**
	 * 承运商简称
	 */
	private String  simpleName;
	/**
	 * 货主ID
	 */
	private String  woId;
	/**
	 * 备注
	 */
	private String  remark;
	/**
	 * 创建人
	 */
	private String createUser;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private String updateUser;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 业务状态
	 */
	private Integer status;
}