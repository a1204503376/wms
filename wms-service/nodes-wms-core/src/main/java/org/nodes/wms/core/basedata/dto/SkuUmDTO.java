package org.nodes.wms.core.basedata.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkuUmDTO extends SkuUm {
	private static final long serialVersionUID = 1L;
}
