package org.nodes.wms.core.instock.asn.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wuhongjie
 * @date 2022/2/21 16:42
 */
@Data
public class LpnItemVo implements Serializable {
	private String lpnCode;
	private BigDecimal qty;
	private String um;
}
