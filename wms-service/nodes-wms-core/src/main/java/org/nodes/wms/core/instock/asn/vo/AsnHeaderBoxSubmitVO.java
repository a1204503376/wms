package org.nodes.wms.core.instock.asn.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.instock.asn.dto.AsnDetailSkuLotDTO;

import java.math.BigDecimal;

/**
 * 按箱收货提交VO
 * @Author zx
 * @Date 2020/6/23
 **/
@Data
public class AsnHeaderBoxSubmitVO {

	@ApiModelProperty("单据编码")
	private String asnBillNo; //单据编码

	@ApiModelProperty("物品编码")
	private String skuCode; //物品编码

	@ApiModelProperty("物品名称")
	private String skuName; //物品名称

	@ApiModelProperty("收货数量")
	private BigDecimal scanQty; //收货数量

	@ApiModelProperty("库位编码")
	private String locCode; //库位编码

	@ApiModelProperty("箱号")
	private String boxCode; //箱号

	@ApiModelProperty("计量单位名称")
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

	@ApiModelProperty("任务id")
	private Long taskId;
}
