
package org.nodes.wms.core.outstock.so.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据传输对象实体类
 *
 * @author NodeX
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SoHeaderDTO extends SoHeader {
	private static final long serialVersionUID = 1L;
	/**
	 * 实际发货时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd"
	)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "实际发货时间")
	private Date outStockDateTime;

	/**
	 * 波次ID
	 */
	@ApiModelProperty("波次ID")
	private Long wellenId;

	/**
	 * 波次编号
	 */
	@ApiModelProperty("波次编号")
	private Long wellenNo;

	/**
	 * 单据类型名称
	 */
	@ApiModelProperty("单据类型名称")
	private String billTypeName;

	/**
	 * 出库单明细列表
	 */
	@ApiModelProperty("出库单明细列表")
	private List<SoDetailDTO> detailList =new ArrayList<>();
}
