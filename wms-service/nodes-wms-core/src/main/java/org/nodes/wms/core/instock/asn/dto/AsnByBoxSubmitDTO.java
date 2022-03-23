package org.nodes.wms.core.instock.asn.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 按箱收货提交接口参数
 * @since 2020-10-20
 */
@Data
public class AsnByBoxSubmitDTO {

	@ApiModelProperty("收货单ID")
	private Long asnBillId;

	@ApiModelProperty("收货单号")
	private String asnBillNo;

	@ApiModelProperty("收货明细ID")
	private Long asnDetailId;

	@ApiModelProperty("箱号")
	private String boxCode;

	@NotNull
	@ApiModelProperty("库位编码")
	private String locCode;

	@ApiModelProperty("待收箱号列表")
	private List<String> boxCodes = new ArrayList<>();

	@ApiModelProperty("收货数量")
	@Range(min = 1)
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal scanQty;

	@NotNull
	@ApiModelProperty("物品编码")
	private String skuCode;

	@ApiModelProperty("库房ID")
	private Long whId;

	@NotNull
	@ApiModelProperty("单位名称")
	private String wsuName;

	@ApiModelProperty("批属性")
	private AsnDetailSkuLotDTO skuLots;

	@ApiModelProperty("容器编码")
	private String lpnCode;
}
