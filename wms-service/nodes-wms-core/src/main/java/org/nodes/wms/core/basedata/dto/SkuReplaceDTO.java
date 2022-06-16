
package org.nodes.wms.core.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.sku.entities.SkuReplace;

/**
 * 物品替代数据传输对象实体类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuReplaceDTO extends SkuReplace {
	@ApiModelProperty(value = "被替代物品编码")
	private String replaceSkuCode;
	@ApiModelProperty(value = "被替代物品名称")
	private String replaceSkuName;
	@ApiModelProperty(value = "被替代物品包装名称")
	private String replaceWspName;
	@ApiModelProperty(value = "被替代物品单位编码")
	private String replaceWsuCode;
	@ApiModelProperty(value = "被替代物品单位名称")
	private String replaceWsuName;
	@ApiModelProperty(value = "被替代物品货主编码")
	private String replaceOwnerCode;
	@ApiModelProperty(value = "被替代物品货主名称")
	private String replaceOwnerName;

	@ApiModelProperty(value = "替代物品编码")
	private String replaceWsrepSkuCode;
	@ApiModelProperty(value = "替代物品名称")
	private String replaceWsrepSkuName;
	@ApiModelProperty(value = "替代物品包装名称")
	private String replaceWsrepWspName;
	@ApiModelProperty(value = "替代物品单位编码")
	private String replaceWsrepWsuCode;
	@ApiModelProperty(value = "替代物品单位名称")
	private String replaceWsrepWsuName;
	@ApiModelProperty(value = "替代物品货主编码")
	private String replaceWsrepOwnerCode;
	@ApiModelProperty(value = "替代物品货主名称")
	private String replaceWsrepOwnerName;
}
