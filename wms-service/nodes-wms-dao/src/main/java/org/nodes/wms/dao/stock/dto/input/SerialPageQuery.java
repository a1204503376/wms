package org.nodes.wms.dao.stock.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 序列号分页查询请求类
 **/
@Data
public class SerialPageQuery implements Serializable {

	private static final long serialVersionUID = -7202101531640611213L;

	/**
	 * 库存id
	 */
	@NotNull(message = "序列号分页：库存id不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
}
