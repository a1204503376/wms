package org.nodes.wms.core.instock.asn.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 按托批量上架提交
 */
@Data
public class PutawayByTranSubmitVO implements Serializable {

	@NotBlank(message = "目标库位编码不能为空！")
	@ApiModelProperty("目标库位编码")
	private String targetLocCode;

	@NotNull(message = "库房ID不能为空！")
	@ApiModelProperty("库房ID")
	private Long whId;

	@ApiModelProperty("托盘信息列表")
	@Valid
	@NotNull(message = "扫描托盘信息不能为空！")
	private List<PutawayLpnItemVo> lpnItems;
}
