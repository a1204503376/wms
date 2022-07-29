package org.nodes.wms.dao.outstock.so.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 出库单查看明细、编辑、日志、分配等请求类
 **/
@Data
public class SoBillIdAndSoDetailIdRequest implements Serializable {

	private static final long serialVersionUID = 2481413361739964125L;

	/**
	 * 发货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 发货单明细id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;
}
