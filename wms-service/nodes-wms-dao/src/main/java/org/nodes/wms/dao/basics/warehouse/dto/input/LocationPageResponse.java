package org.nodes.wms.dao.basics.warehouse.dto.input;

import lombok.Data;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeEnum;
import org.nodes.wms.dao.basics.warehouse.enums.AbcEnum;
import org.nodes.wms.dao.basics.warehouse.enums.LocCategoryEnum;
import org.nodes.wms.dao.basics.warehouse.enums.LocHandLingEnum;
import org.nodes.wms.dao.basics.warehouse.enums.LocTypeEnum;

import java.io.Serializable;

/**
 * 库位分页查询数据返回dto
 **/
@Data
public class LocationPageResponse implements Serializable {

	private static final long serialVersionUID = 1943526496365753566L;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 库区名称
	 */
	private String zoneName;

	/**
	 * 库区类型
	 */
	private LocTypeEnum locType;

	/**
	 * 库位种类
	 */
	private LocCategoryEnum locCategory;

	/**
	 * 库位处理
	 */
	private LocHandLingEnum locHandling;

	/**
	 * abc名称
	 */
	private AbcEnum abc;

	/**
	 * 路线顺序
	 */
	private Integer logicAllocation;

	/**
	 * 货架层
	 */
	private String locLevel;

	/**
	 * 货架列
	 */
	private String locColumn;

	/**
	 * 货架排
	 */
	private String locBank;

	/**
	 * 上架顺序
	 */
	private Integer putOrder;

	/**
	 * 适用的容器类型枚举
	 */
	private LpnTypeEnum lpnType;

	/**
	 * 是否启用
	 */
	private Integer status;
}
