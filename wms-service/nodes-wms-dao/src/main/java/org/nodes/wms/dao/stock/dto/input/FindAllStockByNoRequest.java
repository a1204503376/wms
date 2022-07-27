package org.nodes.wms.dao.stock.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * Pda根据编码查询库存-请求对象
 */
@Data
public class FindAllStockByNoRequest implements Serializable {
	private static final long serialVersionUID = -5586308651480809877L;
	private String no;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * PDA扫码之后，符合相对应的条码的数据，然后只根据这一个字段去查询
	 */
	private String type;
}
