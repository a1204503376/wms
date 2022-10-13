package org.nodes.wms.dao.tianyi.skubox.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 天宜物品和型号对应箱型标识 分页查询响应类
 **/
@Data
@ColumnWidth(15)
public class SkuBoxPageResponse implements Serializable {

	private static final long serialVersionUID = -8459261115516605547L;

	/**
	 * 识别码
	 */
	@ExcelProperty("识别码")
	private Integer id;

	/**
	 * 物品名称
	 */
	@ExcelProperty("物品名称")
	private String skuName;

	/**
	 * 物品规格
	 */
	@ExcelProperty("物品规格")
	private String skuSpec;

	/**
	 * 创建时间
	 */
	@ExcelProperty("创建时间")
	private Date createTime;
}
