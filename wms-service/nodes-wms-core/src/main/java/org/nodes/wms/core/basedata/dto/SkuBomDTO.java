package org.nodes.wms.core.basedata.dto;

import org.nodes.wms.core.basedata.entity.SkuBom;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物料清单数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuBomDTO extends SkuBom {
	private static final long serialVersionUID = 1L;

}
