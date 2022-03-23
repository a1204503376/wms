
package org.nodes.wms.core.strategy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.strategy.entity.Outstock;

import java.util.List;

/**
 * 分配策略 数据传输对象实体类
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OutstockDTO extends Outstock {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "分配策略明细信息")
	private List<OutstockDetailDTO> outstockDetailDTO;

	@ApiModelProperty(value = "分配策略明细信息(客户端删除项)")
	private List<OutstockDetailDTO> outstockDetailDeletedList;
}
