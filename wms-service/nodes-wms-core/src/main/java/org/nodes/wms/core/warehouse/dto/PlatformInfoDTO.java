
package org.nodes.wms.core.warehouse.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.validation.Excel;
import org.nodes.core.tool.validation.GroupA;
import org.nodes.wms.core.warehouse.entity.PlatformInfo;

import javax.validation.constraints.NotBlank;

/**
 * 月台数据传输对象实体类
 *
 * @author liangmei
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformInfoDTO extends PlatformInfo {
	private static final long serialVersionUID = 1L;

	/**
	 * 月台分类(多个用英文逗号分开)
	 */
	@ApiModelProperty("月台分类(多个用英文逗号分开)")
	@NotBlank(message = "月台类型不能为空！",groups = {GroupA.class,Excel.class})
    private String platformTypeDesc;
}
