package org.nodes.wms.core.outstock.sales.dto;

import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.outstock.sales.entity.SalesHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售单主表
数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SalesHeaderDTO extends SalesHeader {
	private static final long serialVersionUID = 1L;

	/**
	 * 销售单明细
	 */
	@ApiModelProperty("销售单明细")
	private List<SalesDetailDTO> detailList = new ArrayList<>();
}
