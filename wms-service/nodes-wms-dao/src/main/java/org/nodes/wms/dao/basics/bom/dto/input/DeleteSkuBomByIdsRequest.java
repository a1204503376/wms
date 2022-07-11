package org.nodes.wms.dao.basics.bom.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 物料清单删除请求对象
 */
@Data
public class DeleteSkuBomByIdsRequest implements Serializable {
	private static final long serialVersionUID = 2902897164336347274L;
	private List<Long> ids;
}
