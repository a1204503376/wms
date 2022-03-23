
package org.nodes.wms.core.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.core.common.entity.Contacts;

/**
 * 视图实体类
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ContactsVO对象", description = "ContactsVO对象")
public class ContactsVO extends Contacts {
	private static final long serialVersionUID = 1L;

	/**
	 * 添加默认标记（Boolean类型）
	 *
	 * @author wangjw
	 * @since 2020-1-20
	 */
	@ApiModelProperty("默认标记（Boolean类型）")
	public boolean defaultFlagBoolean;

	@ApiModelProperty("默认标记（Boolean类型）")
	public String defaultFlagBooleanDesc;
}
