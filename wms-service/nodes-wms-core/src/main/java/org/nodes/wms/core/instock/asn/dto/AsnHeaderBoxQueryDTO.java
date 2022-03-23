package org.nodes.wms.core.instock.asn.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 按箱收货查询VO
 * @Author zx
 * @Date 2020/6/28
 **/
@Data
public class AsnHeaderBoxQueryDTO {

	@ApiModelProperty("单据编号")
	@NotNull(message = "收货单编号不能为空")
	private String asnBillNo; //单据编号

	@ApiModelProperty("物品编码")
	@NotNull(message = "sku编号不能为空")
	private String skuCode; //物品编码

	@ApiModelProperty("物品名称")
	private String skuName; //物品名称

	@ApiModelProperty("计量单位名称")
	@NotNull(message = "单位不能为空")
	private String wsuName; //计量单位名称

	@ApiModelProperty("物品类型编码")
	private String typeCode; //物品类型编码

	@ApiModelProperty("版本号")
	private String version; //版本号

	@ApiModelProperty("备注")
	private String remarks; //备注

	@ApiModelProperty("类型")
	private String type; //类型

	@ApiModelProperty("标签类型名称")
	private String labelCode; //标签类型名称

	@ApiModelProperty("批属性")
	private AsnDetailSkuLotDTO skuLots;

	@ApiModelProperty("数量")
	private BigDecimal qty;

	@ApiModelProperty("打印数量")
	private int printCount;
}
