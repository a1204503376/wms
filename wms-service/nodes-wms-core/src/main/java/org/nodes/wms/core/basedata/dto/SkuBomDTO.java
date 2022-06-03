package org.nodes.wms.core.basedata.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.bom.entites.SkuBom;

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
