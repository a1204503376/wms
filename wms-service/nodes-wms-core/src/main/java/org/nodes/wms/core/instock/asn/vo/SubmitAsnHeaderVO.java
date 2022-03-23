package org.nodes.wms.core.instock.asn.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pengwei
 * @program WmsCore
 * @description 按件收货，提交入库VO类
 * @since 2020-12-07
 */
@Data
public class SubmitAsnHeaderVO {

	/**
	 * 收货进度
	 */
	@ApiModelProperty("收货进度")
	private AsnDetailMinVO asnDetail;
	/**
	 * 上架任务模式
	 */
	@ApiModelProperty("上架任务模式")
	private Boolean isAutoPutaway;
}
