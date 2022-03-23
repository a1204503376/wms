package org.nodes.core.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;

/**
 * TopMenuSetting
 *
 * @author Nodes
 */
@Data
@TableName("blade_top_menu_setting")
public class TopMenuSetting implements Serializable {

	/**
	 * 主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 顶部菜单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long topMenuId;

	/**
	 * 菜单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long menuId;

}
