package org.nodes.wms.dao.basics.bom.dto.input;

import lombok.Data;

import java.util.List;

/**
 * 物料清单删除请求对象
 */
@Data
public class DeleteSkuBomByIdsRequest {
    private List<Long> ids;
}
