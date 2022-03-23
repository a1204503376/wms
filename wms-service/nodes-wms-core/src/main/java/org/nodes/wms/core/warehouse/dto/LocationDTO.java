package org.nodes.wms.core.warehouse.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.warehouse.entity.Location;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationDTO extends Location {
	private static final long serialVersionUID = 1L;

}
