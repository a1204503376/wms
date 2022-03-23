
package org.nodes.wms.core.outstock.so.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;

/**
 * 波次划分明细表视图实体类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WellenDetailVO对象", description = "波次划分明细表")
public class WellenDetailVO extends WellenDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 出库单表头
	 */
	@ApiModelProperty("出库单表头")
	private SoHeader soHeader;
	/**
	 * 出库单明细
	 */
	@ApiModelProperty("出库单明细")
	private SoDetail soDetail;
	/**
	 * 波次编码
	 */
	@ApiModelProperty("波次编码")
	private Long wellenNo;
}
