package org.nodes.wms.core.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.system.entity.PrintTemplate;

/**
 * 打印模板数据传输对象实体类
 *
 * @author NodeX
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PrintTemplateDTO extends PrintTemplate {
	private static final long serialVersionUID = 1L;

	/**
	 * 客户ID
	 */
	@ApiModelProperty("客户ID")
	private Long peId;

}
