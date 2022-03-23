package org.nodes.wms.core.instock.asn.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * 按箱收货DTO
 *
 * @Author zx
 * @Date 2020/6/28
 **/
@Data
public class AsnHeaderBoxDTO {

	private String asnBillNo; //单据编码

	private String skuCode; //物品编码

	private String skuName; //物品名称

	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal scanQty; //收货数量

	private String locCode; //库位编码

	private String boxCode; //箱号

	private String wsuName; //计量单位名称

	private String typeCode; //物品类型编码

	private String version; //版本号

	private String remarks; //备注

	private String type; //类型

	private String labelCode; //标签类型名称

	private Long locId; //库位ID

	private Long wspId; //包装ID

//	private Long wspdId; //包装明细ID

	private Long whId; //库房ID

	private Long asnBillId; //单据ID

	private Long skuId; //物品ID

	private AsnDetailSkuLotDTO skuLots; //批属性

	private String sName;

	private String skuSpec;

	private BigDecimal planQty;

	private Long taskId;
}
