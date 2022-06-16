package org.nodes.wms.core.basedata.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.springblade.core.tool.node.INode;

import java.util.ArrayList;
import java.util.List;

/**
 *@program 物品分类Vo
 *@description 物品分类Vo
 *@author wanglei
 *@create 20191128
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "物品分类对象", description = "物品分类视图对象")
public class SkuTypeVO extends SkuType implements INode {

	/**
	 * 物料分类Id（树状结构）
	 */
	@Override
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getId() {
		return this.getSkuTypeId();
	}

	@Override
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getParentId() {
		return this.getTypePreId();
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 上位物品分类ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	public long parentId;

	/**
	 * 物料分类子节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<INode> children;

	/**
	 * 是否有子节点
	 * @return
	 */
	public boolean hasChildren;

	/**
	 * 获取物料分类子节点
	 */
	@Override
	public List<INode> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		return this.children;
	}

	/**
	 * 上级物品分类名称
	 */
	private String parentName;
	/**
	 * 货主名称
	 */
	private String ownerName;


}
