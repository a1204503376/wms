package org.nodes.wms.core.outstock.so.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 保存拣货计划参数DTO实体类
 * @since 2020-11-04
 */
@Data
public class SavePickPlanDTO {

	/**
	 * 待保存的拣货计划明细
	 */
	@ApiModelProperty("待保存的拣货计划明细")
	private List<SavePickPlanDetailDTO> detailList;

	/**
	 * 是否为波次分配结果
	 */
	@ApiModelProperty("是否为波次分配结果")
	private Boolean isWellen;
}
