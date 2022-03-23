package org.nodes.wms.core.instock.asn.vo;

import lombok.Data;

/**
 * @author pengwei
 * @program WmsCore
 * @description 按箱拣货提交返回值
 * @since 2020-10-20
 */
@Data
public class AsnByOrderSubmitVO {

	private Long asnDetailId;
	private String message;
}
