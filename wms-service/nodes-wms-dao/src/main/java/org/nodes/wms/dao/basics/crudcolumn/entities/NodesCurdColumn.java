package org.nodes.wms.dao.basics.crudcolumn.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 列显隐 实体类
 */
@Data
@TableName("nodes_curd_column")
public class NodesCurdColumn extends TenantEntity {

	private static final long serialVersionUID = 1798933647310708649L;

	/**
	 * 主键ID
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 菜单id
	 */
	private Long menuId;
	/**
	 * 父级id
	 */
	private Long parentId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 排序
	 */
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
}
