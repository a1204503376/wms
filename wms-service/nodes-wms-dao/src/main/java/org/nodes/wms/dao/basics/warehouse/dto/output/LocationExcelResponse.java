package org.nodes.wms.dao.basics.warehouse.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 服务端导出返回对象
 **/
@Data
public class LocationExcelResponse extends LocationPageResponse implements Serializable {

	private static final long serialVersionUID = 4243892585338173448L;

	/**
	 * 库区类型名称
	 */
	@ExcelProperty("库位类型")
	private String locTypeDesc;

	/**
	 * 库位种类名称
	 */
	@ExcelProperty("库位种类")
	private String locCategoryDesc;

	/**
	 * 库位处理名称
	 */
	@ExcelProperty("库位处理")
	private String locHandlingDesc;

	/**
	 * abc分类名称
	 */
	@ExcelProperty("ABC分类")
	private String abcDesc;

	/**
	 * 容器类型名称
	 */
	@ExcelProperty("容器类型")
	private String lpnTypeDesc;
}
