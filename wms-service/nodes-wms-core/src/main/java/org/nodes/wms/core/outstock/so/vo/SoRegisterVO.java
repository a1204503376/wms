package org.nodes.wms.core.outstock.so.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.so.entity.SoRegister;

/**
 * 装车登记视图实体类
 *
 * @author zhongls
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RegisterVO对象", description = "装车登记")
public class SoRegisterVO extends SoRegister {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	private String whName;
}
