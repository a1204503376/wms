package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 出库复核请求类
 **/
@Data
public class OutStockCheckoutRequest implements Serializable {

	private static final long serialVersionUID = 8775375903192406063L;

	/**
	 * 发货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 已复核的箱码
	 */
	private List<String> boxCodeList;
}
