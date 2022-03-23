package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.strategy.entity.Wellen;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 波次策略视图实体类
 *
 * @author NodeX
 * @since 2021-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WellenVO对象", description = "波次策略")
public class WellenVO extends Wellen {
	private static final long serialVersionUID = 1L;

	/**
	 * 明细集合
	 */
	private List<WellenDetailVO> detailList;
}
