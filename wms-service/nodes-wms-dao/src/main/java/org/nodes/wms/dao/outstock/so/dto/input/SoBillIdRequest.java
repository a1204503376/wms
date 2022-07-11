package org.nodes.wms.dao.outstock.so.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 出库单查看明细、编辑、日志等请求类
 **/
@Data
public class SoBillIdRequest implements Serializable {

	private static final long serialVersionUID = -6913232426127670821L;

	@NotNull(message = "出库单id不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
}
