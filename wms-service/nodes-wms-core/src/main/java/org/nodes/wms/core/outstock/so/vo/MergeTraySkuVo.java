package org.nodes.wms.core.outstock.so.vo;

import lombok.Data;
import org.nodes.core.tool.entity.SkuLotBaseEntity;

import java.math.BigDecimal;

@Data
public class MergeTraySkuVo extends SkuLotBaseEntity {
	private String skuId;
	private String skuCode;
	private String skuName;
	private String address;
	private String soBillId;
	private String cCode;
	private String cName;
	private Long wslId;
	private Long wslvId;
	private BigDecimal qty;
	private String lpnCode;
}
