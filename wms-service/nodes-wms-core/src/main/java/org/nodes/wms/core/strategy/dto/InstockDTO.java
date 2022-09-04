
package org.nodes.wms.core.strategy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.putaway.entities.StInstock;

import java.util.List;

/**
 * 上架策略数据传输对象实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InstockDTO extends StInstock {
	private static final long serialVersionUID = 1L;

	/**
	 *上架策略明细集合
	 */
	@ApiModelProperty("上架策略明细集合")
	private List<InstockDetailDTO> instockDetailList;

	/**
	 * 上架策略明细集合(客户端删除项）
	 */
	@ApiModelProperty("上架策略明细集合(客户端删除项）")
	private List<InstockDetailDTO> instockDetailDeletedList;

}
