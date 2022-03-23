
package org.nodes.wms.core.warehouse.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.warehouse.entity.Zone;

/**
 * 库区管理 视图实体类
 *
 * @author zhongls
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ZoneVO对象", description = "ZoneVO对象")
public class ZoneVO extends Zone {
	private static final long serialVersionUID = 1L;
	//创建人姓名
	private String createByName;
	//更新人姓名
	private String updateByName;
	//库房名称
	private String whName;
	//是否启用显示
	private String isActiveName;

	/**
	 * 库区类型描述
	 */
	@ApiModelProperty("库区类型描述")
	private String zoneTypeDesc;
}
