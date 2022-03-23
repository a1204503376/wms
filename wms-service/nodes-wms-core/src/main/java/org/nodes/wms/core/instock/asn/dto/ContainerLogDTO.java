package org.nodes.wms.core.instock.asn.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.instock.asn.entity.ContainerLog;

/**
 * 清点记录数据传输对象实体类
 *
 * @author NodeX
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContainerLogDTO extends ContainerLog {
	private static final long serialVersionUID = 1L;

	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	private String skuCode;
}
