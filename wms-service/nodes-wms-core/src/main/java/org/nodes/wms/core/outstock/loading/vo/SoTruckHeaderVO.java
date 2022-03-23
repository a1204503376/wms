
package org.nodes.wms.core.outstock.loading.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.vo.StockVO;
import org.nodes.wms.core.outstock.loading.entity.SoTruckHeader;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;

import java.util.List;

/**
 * 车次头表视图实体类
 *
 * @author chz
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SoTruckHeaderVO对象", description = "车次头表")
public class SoTruckHeaderVO extends SoTruckHeader {
	private static final long serialVersionUID = 1L;
	/**
	 *根据LPN查询出来的库存信息列表
	 */
	@ApiModelProperty(value = "根据LPN查询出来的库存信息列表")
	private List<StockVO> stockVOList;
	/**
	 * 发货明细头表数据
	 */
	@ApiModelProperty(value = "发货明细头表数据")
	private SoHeaderVO soHeaderVO;

	/**
	 * 车次状态描述
	 */
	@ApiModelProperty("车次状态描述")
	private String truckStateDesc;

	/**
	 * 装车明细集合
	 */
	@ApiModelProperty("装车明细集合")
	private List<SoTruckDetailVO> truckDetailList;

}
