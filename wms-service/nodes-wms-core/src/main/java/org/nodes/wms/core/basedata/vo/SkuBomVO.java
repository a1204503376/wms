package org.nodes.wms.core.basedata.vo;

import org.nodes.wms.dao.basics.bom.entites.SkuBom;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 物料清单视图实体类
 *
 * @author NodeX
 * @since 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuBomVO对象", description = "物料清单")
public class SkuBomVO extends SkuBom {
	private static final long serialVersionUID = 1L;

}
