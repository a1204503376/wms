package org.nodes.wms.core.strategy.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.putaway.entities.StInstockConfigLot;

@Data
@EqualsAndHashCode(callSuper = true)
public class InstockConfigLotDTO extends StInstockConfigLot {
	private static final long serialVersionUID = 1L;

}
