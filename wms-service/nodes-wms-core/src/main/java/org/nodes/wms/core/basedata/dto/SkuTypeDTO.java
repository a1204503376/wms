package org.nodes.wms.core.basedata.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.sku.entities.SkuType;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkuTypeDTO extends SkuType {
	private static final long serialVersionUID = 1L;

	/**
	 * 物料分类Id（树状结构）
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getId() {
		return this.getSkuTypeId();
	}

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
	 * 上级物品分类名称
	 */
	private String parentName;
	/**
	 * 货主名称
	 */
	private String ownerName;
	/**
	 * 货主编码
	 */
	private String woCode;

}
