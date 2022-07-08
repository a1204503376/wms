
package org.nodes.wms.core.basedata.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;

/**
 * 物品批属性视图实体类
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuLotVO对象", description = "物品批属性")
public class SkuLotVO extends SkuLot {
	private static final long serialVersionUID = 1L;
	/**
	 * 货主名称
	 */
	private String ownerName;
	/**
	 * 批属性显示个数
	 */
	private  Integer LotLength=0;
}
