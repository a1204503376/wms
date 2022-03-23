
package org.nodes.wms.core.instock.asn.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.instock.asn.entity.AsnLpnDetail;

/**
 * 入库容器明细 视图实体类
 *
 * @author zhonglianshuai
 * @since 2020-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AsnLpnDetailVO对象", description = "AsnLpnDetailVO对象")
public class AsnLpnDetailVO extends AsnLpnDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 容器状态描述
	 */
	private String lpnStatusDesc;
}
