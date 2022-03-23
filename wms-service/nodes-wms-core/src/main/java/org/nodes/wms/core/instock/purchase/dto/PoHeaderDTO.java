package org.nodes.wms.core.instock.purchase.dto;

import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.instock.purchase.entity.PoHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 采购单头表数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PoHeaderDTO extends PoHeader {
	private static final long serialVersionUID = 1L;

	/**
	 * 采购订单明细列表
	 */
	@ApiModelProperty("采购订单明细列表")
	private List<PoDetailDTO> detailList = new ArrayList<>();
}
