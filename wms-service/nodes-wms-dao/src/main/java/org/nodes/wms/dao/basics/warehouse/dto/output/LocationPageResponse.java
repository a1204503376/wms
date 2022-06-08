package org.nodes.wms.dao.basics.warehouse.dto.output;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
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
@ColumnWidth(15)
public class LocationPageResponse implements Serializable {

	private static final long serialVersionUID = 1943526496365753566L;

	/**
	 * 库位编码
	 */
	@ExcelProperty(value = "库位编码", order = Integer.MIN_VALUE)
	private String locCode;

	/**
	 * 库房名称
	 */
	@ExcelProperty(value = "库房", order = Integer.MIN_VALUE)
	private String whName;

	/**
	 * 库区名称
	 */
	@ExcelProperty(value = "库区", order = Integer.MIN_VALUE)
	private String zoneName;

	/**
	 * 库位类型
	 */
	@ExcelIgnore
	private LocTypeEnum locType;

	/**
	 * 库位种类
	 */
	@ExcelIgnore
	private LocCategoryEnum locCategory;

	/**
	 * 库位处理
	 */
	@ExcelIgnore
	private LocHandLingEnum locHandling;

	/**
	 * abc名称
	 */
	@ExcelIgnore
	private AbcEnum abc;

	/**
	 * 路线顺序
	 */
	@ExcelProperty(value = "路线顺序")
	private Integer logicAllocation;

	/**
	 * 货架层
	 */
	@ExcelProperty(value = "货架层")
	private String locLevel;

	/**
	 * 货架列
	 */
	@ExcelProperty(value = "货架列")
	private String locColumn;

	/**
	 * 货架排
	 */
	@ExcelProperty("货架排")
	private String locBank;

	/**
	 * 上架顺序
	 */
	@ExcelProperty("上架顺序")
	private Integer putOrder;

	/**
	 * 适用的容器类型
	 */
	@ExcelIgnore
	private LpnTypeEnum lpnType;

	/**
	 * 是否启用
	 */
	@ExcelProperty("是否启用")
	private Integer status;
}
