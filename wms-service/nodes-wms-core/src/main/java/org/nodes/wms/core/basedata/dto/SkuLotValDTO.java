
package org.nodes.wms.core.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.basedata.entity.SkuLotVal;

import javax.validation.constraints.NotBlank;

/**
 * 批属性验证数据传输对象实体类
 *
 * @author chenhz
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuLotValDTO extends SkuLotVal {
	@ApiModelProperty("货主编码")
	@NotBlank(message = "货主编码不能为空！",groups = {Excel.class})
	private String ownerCode;

}
