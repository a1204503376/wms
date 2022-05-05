package org.nodes.wms.dao.basics.crudcolumn.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 列显隐请求对象
 */
@Data
public class CrudColumnRequest implements Serializable {

	private static final long serialVersionUID = -4498483317943633731L;

	/**
	 * 菜单id
	 */
	private Long menuId;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 排序
	 */
	private Integer order;

	private Integer sort;
	/**
	 * 字段名
	 */
	private String prop;
	/**
	 * 列名
	 */
	private String label;
	/**
	 * 别名
	 */
	private String aliasName;
	/**
	 * 是否隐藏，true：是  false：否
	 */
	private Boolean hide;
	/**
	 * 是否冻结，true：是  false：否
	 */
	private Boolean fixed;
	/**
	 * 宽度
	 */
	private Integer width;
	/**
	 * 对齐方式 left/center/right
	 */
	private String align;

	public Integer getSort() {
		return this.order;
	}
}
