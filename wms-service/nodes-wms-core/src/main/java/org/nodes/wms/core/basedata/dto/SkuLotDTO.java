package org.nodes.wms.core.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkuLotDTO extends SkuLot {
	/**
	 * 货主编码
	 */
	@ApiModelProperty("货主编码")
	@NotBlank(message = "货主编码不能为空！",groups = {Excel.class})
	private String ownerCode;
	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;

	/**
	 * 文件名
	 */
	@ApiModelProperty("文件名")
	private String fileName;
}
