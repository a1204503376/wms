package org.nodes.wms.core.strategy.dto;

import org.nodes.wms.core.strategy.entity.Wellen;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 波次策略数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WellenDTO extends Wellen {
	private static final long serialVersionUID = 1L;
	/**
	 * 明细集合
	 */
	private List<WellenDetailDTO> detailList;
}
