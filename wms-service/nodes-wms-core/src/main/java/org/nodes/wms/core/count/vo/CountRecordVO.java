
package org.nodes.wms.core.count.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.count.entity.CountRecord;

import java.util.List;

/**
 * 盘点单记录表视图实体类
 *
 * @author chz
 * @since 2020-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CountRecordVO对象", description = "盘点单记录表")
public class CountRecordVO extends CountRecord {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ExcelProperty("库房")
	@ApiModelProperty(value = "库房名称")
	private String whName;

	/**
	 * 包装名称
	 */
	@ExcelProperty("包装名称")
	@ApiModelProperty(value = "包装名称")
	private String wspName;

	@ApiModelProperty(value = "库区ID")
	private List<Long> zoneId;

	/**
	 * 开始时间
	 */

	@ApiModelProperty(value = "开始时间")
	private String[] startTimeArray;

	/**
	 * 创建时间描述
	 */

	@ApiModelProperty(value = "创建时间描述")
	private String procTimeDesc;

	@ApiModelProperty(value = "层级名称")
	private String skuLevelDesc;

	/**
	 * 盘点记录状态描述
	 */
	@ApiModelProperty(value = "盘点记录状态描述")
	private String recordStateDesc;

	/**
	 * 盘点时间范围（json数组）
	 */
	@ApiModelProperty("盘点时间范围（json数组）")
	private String procTimeArray;
}
