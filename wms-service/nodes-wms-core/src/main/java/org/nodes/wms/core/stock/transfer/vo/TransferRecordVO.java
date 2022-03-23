
package org.nodes.wms.core.stock.transfer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;

/**
 * 移动记录表视图实体类
 *
 * @author wangjw
 * @since 2020-02-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TransferRecordVO对象", description = "移动记录表")
public class TransferRecordVO extends TransferRecord {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("移动类型名称")
	private String transferTypeName;

	@ApiModelProperty("物品编码")
	private String skuCode;

	@ApiModelProperty("物品名称")
	private String skuName;

	@ApiModelProperty("包装名称")
	private String wspName;

	@ApiModelProperty("包装层级描述")
	private String skuLevelName;

	@ApiModelProperty("货主名称")
	private String ownerName;

	@ApiModelProperty("用户编码")
	private String userCode;

	@ApiModelProperty("用户名称")
	private String userName;
}
