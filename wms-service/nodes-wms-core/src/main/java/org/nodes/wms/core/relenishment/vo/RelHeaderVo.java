package org.nodes.wms.core.relenishment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.relenishment.entity.RelHeader;

import java.util.List;

/**
 * 补货单头表VO实体类
 *
 * @author whj
 * @since 2019-12-13
 */
@Data
@ApiModel(value = "RelHeaderVo对象", description = "补货单头表")
public class RelHeaderVo extends RelHeader {
	private String relBillStateDesc;
	private List<RelDetailVo> relDetailVos;

	@ApiModelProperty(value = "创建用户名称")
	private String createUserName;
}
