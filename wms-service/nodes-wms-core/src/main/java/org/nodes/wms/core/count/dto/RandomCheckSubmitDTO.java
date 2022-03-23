package org.nodes.wms.core.count.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.entity.SkuLotBaseEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class RandomCheckSubmitDTO {
	@ApiModelProperty("库位")
	@NotNull
	private String locCode;

	@ApiModelProperty("容器编码")
	private String lpnCode;

	@ApiModelProperty("任务id")
	private String taskId;

	@ApiModelProperty("盘点单号")
	private String countNo;

	@ApiModelProperty("盘点的物料列表")
	private List<RandomCheckItem> items = new ArrayList<>();

	@Data
	public static class RandomCheckItem extends SkuLotBaseEntity {
		private BigDecimal qty;
		private String skuCode;
		private String skuName;
		private String umName;
		private String lpnCode;
	}
}
