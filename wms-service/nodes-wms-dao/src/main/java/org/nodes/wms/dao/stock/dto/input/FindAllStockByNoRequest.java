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
}
