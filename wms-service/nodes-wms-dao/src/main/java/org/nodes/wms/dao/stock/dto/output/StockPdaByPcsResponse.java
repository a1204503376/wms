package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 根据箱码查询库存
 **/
@Data
public class StockPdaByPcsResponse implements Serializable {
	private static final long serialVersionUID = 6944206036296901150L;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 库位ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
}
