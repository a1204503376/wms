
package org.nodes.wms.core.count.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.wms.core.count.entity.CountReport;

import java.math.BigDecimal;

/**
 * 盘点差异报告表视图实体类
 *
 * @author chz
 * @since 2020-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CountReportVO对象", description = "盘点差异报告表")
public class CountReportVO extends CountReport {
	private static final long serialVersionUID = 1L;

	/**
	 * 差异值
	 */
	@ApiModelProperty(value = "差异值")
	@ExcelProperty("差异数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal differentNum;

}
