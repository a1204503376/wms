package org.nodes.wms.core.strategy.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.putway.entities.InstockConfigLot;

@Data
@EqualsAndHashCode(callSuper = true)
public class InstockConfigLotDTO extends InstockConfigLot {
	private static final long serialVersionUID = 1L;

}
