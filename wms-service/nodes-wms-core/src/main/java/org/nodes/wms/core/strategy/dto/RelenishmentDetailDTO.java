
package org.nodes.wms.core.strategy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.putway.entities.InstockConfig;
import org.nodes.wms.dao.putway.entities.InstockConfigLot;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;

import java.util.List;

/**
 * 补货策略明细数据传输对象实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RelenishmentDetailDTO extends RelenishmentDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 补货策略批属性设定集合
	 */
	@ApiModelProperty("补货策略批属性设定集合")
	private List<InstockConfigLot> instockConfigLotList;

	/**
	 * 补货策略物品明细集合(客户端删除项)
	 */
	@ApiModelProperty("补货策略物品明细集合(客户端删除项)")
	private List<InstockConfigLot> instockConfigLotDeletedList;
	/**
	 * 补货策略执行设定集合
	 */
	@ApiModelProperty("补货策略执行设定集合")
	private List<InstockConfig> instockConfigList;

	/**
	 * 补货策略执行设定集合(客户端删除项)
	 */
	@ApiModelProperty("补货策略执行设定集合(客户端删除项)")
	private List<InstockConfig> instockConfigDeletedList;

}
