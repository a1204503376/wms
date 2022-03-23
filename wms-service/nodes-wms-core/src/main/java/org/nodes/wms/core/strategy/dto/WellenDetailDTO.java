package org.nodes.wms.core.strategy.dto;

import org.nodes.wms.core.strategy.entity.WellenDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 波次策略明细数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WellenDetailDTO extends WellenDetail {
	private static final long serialVersionUID = 1L;

}
