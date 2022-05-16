package org.nodes.wms.dao.basics.sku.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 物品分类实体类
 **/

@Data
@TableName("wms_sku_type")
public class SkuType {

	/**
	 * 物品分类id
	 */
	@TableId(value = "skuTypeId", type = IdType.ASSIGN_ID)
	private Long skuTypeId;

	/**
	 * 上位物品分类id
	 */
	private String typePreId;

	/**
	 * 物品分类名称
	 */
	private String typeName;

	/**
	 * 物品分类编码
	 */
	private String typeCode;

	/**
	 * 货主id
	 */
	private String woId;

	/**
	 * 绩效系数
	 */
	private String gradeNum;

	/**
	 * 物品分类路径
	 */
	private String typePath;

	/**
	 * 物品分类备注
	 */
	private String typeRemark;

	/**
	 * 数据类型
	 */
	private String dataType;

	/**
	 * 排序
	 */
	private String sort;
}
