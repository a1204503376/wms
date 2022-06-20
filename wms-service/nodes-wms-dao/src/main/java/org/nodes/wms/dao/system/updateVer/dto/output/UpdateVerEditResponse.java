package org.nodes.wms.dao.system.updateVer.dto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统版本编辑返回dto类
 **/
@Data
public class UpdateVerEditResponse implements Serializable {

	private static final long serialVersionUID = -2303360250213794931L;

	/**
	 * 系统版本id
	 */
	private Long suvId;
	/**
	 * 版本号数值
	 */
	private Integer verNum;
	/**
	 * 版本号名称
	 */
	private String verName;
	/**
	 * 模块类型
	 */
	private String moduleType;
	/**
	 * 更新地址
	 */
	private String updateUrl;
	/**
	 * 更新备注
	 */
	private String updateRemark;
	/**
	 * 更新类型
	 */
	private String updateType;
}
