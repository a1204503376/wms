package org.nodes.wms.core.instock.asn.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wuhongjie
 * @date 2022/2/21 16:42
 */
@Data
public class PutawayLpnItemVo implements Serializable {
	@NotBlank(message = "托盘号不能为空！")
	private String lpnCode;
	@Min(value = 0,message = "上架数量必须大于0！")
	private BigDecimal qty;
	@NotBlank(message = "原库位不能为空！")
	private String locCode;

	private String zoneCode;
}
