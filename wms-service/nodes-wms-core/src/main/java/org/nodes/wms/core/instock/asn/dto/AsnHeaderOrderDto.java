package org.nodes.wms.core.instock.asn.dto;

import lombok.Data;
import org.nodes.wms.core.instock.asn.vo.AsnDetailVO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AsnHeaderOrderDto {
	/**
	 * 订单编号
	 */
	@NotBlank(message = "订单编号不能为空！")
	private String asnBillNo;
	/**
	 * 库房id
	 */
	@NotNull(message = "库房id不能为空")
	private Long whId;
	/**
	 * 库位
	 */
	@NotBlank(message = "库位不能为空！")
	private String locCode;
	/**
	 * 容器编码
	 */
//	@NotBlank(message = "容器编码不能为空！")
	private String lpnCode;
	/**
	 * 明细id和数量键值对
	 */
	@NotEmpty(message = "明细信息不能为空！")
	private List<AsnDetailVO> asnDetailMap;
}
