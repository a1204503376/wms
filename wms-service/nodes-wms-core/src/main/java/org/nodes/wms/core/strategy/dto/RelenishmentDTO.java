
package org.nodes.wms.core.strategy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.Relenishment;

import java.util.List;

/**
 * 补货策略数据传输对象实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RelenishmentDTO extends Relenishment {
	private static final long serialVersionUID = 1L;

	/**
	 *补货策略明细集合
	 */
	@ApiModelProperty("补货策略明细集合")
	private List<RelenishmentDetailDTO> relenishmentDetailList;

	/**
	 * 补货策略明细集合(客户端删除项）
	 */
	@ApiModelProperty("补货策略明细集合(客户端删除项）")
	private List<RelenishmentDetailDTO> relenishmentDetailDeletedList;

}
