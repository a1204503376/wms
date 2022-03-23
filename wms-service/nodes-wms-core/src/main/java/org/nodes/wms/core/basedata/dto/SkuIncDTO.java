
package org.nodes.wms.core.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.basedata.entity.SkuInc;

/**
 * 物品供应商关联数据传输对象实体类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuIncDTO extends SkuInc {

	@ApiModelProperty(value = "供应商编码")
	private String enterpriseCode;
	@ApiModelProperty(value = "供应商名称")
	private String enterpriseName;
	@ApiModelProperty(value = "供应商包装名称")
	private String enterpriseWspName;

	@ApiModelProperty(value = "关连物品编码")
	private String replaceSkuCode;
	@ApiModelProperty(value = "关连物品名称")
	private String replaceSkuName;

	@ApiModelProperty(value = "关连物品货主编码")
	private String replaceOwnerCode;
	@ApiModelProperty(value = "关连物品货主名称")
	private String replaceOwnerName;


}
