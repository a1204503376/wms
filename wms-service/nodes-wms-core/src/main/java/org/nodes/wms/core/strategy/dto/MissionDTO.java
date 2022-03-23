package org.nodes.wms.core.strategy.dto;

import org.nodes.wms.core.strategy.entity.Mission;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MissionDTO extends Mission {
	private static final long serialVersionUID = 1L;

}
