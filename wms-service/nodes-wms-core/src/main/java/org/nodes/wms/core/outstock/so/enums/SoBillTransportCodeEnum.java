package org.nodes.wms.core.outstock.so.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wangjw
 * @program WmsCore
 * @description 出库单单据发货方式枚举
 * @create 20200316
 */
@Getter
@AllArgsConstructor
public enum SoBillTransportCodeEnum {
	Delivery("配送", 701),
	ThirdParty("第三方物流", 702),
	SelfTaking("自提", 703);

	private String name;
	private int index;
}
