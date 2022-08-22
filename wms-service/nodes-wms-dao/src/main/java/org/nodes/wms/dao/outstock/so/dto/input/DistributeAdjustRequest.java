package org.nodes.wms.dao.outstock.so.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分配调整查询请求类
 **/
@Data
public class DistributeAdjustRequest implements Serializable {

	private static final long serialVersionUID = -7408431182391187023L;

	/**
	 * 物品id
	 */
	@NotNull(message = "参数 skuId 不能为空")
	private Long skuId;

	/**
	 * 生产批次
	 */
	private String skuLot1;

	/**
	 * 专用客户
	 */
	private String skuLot4;
}
