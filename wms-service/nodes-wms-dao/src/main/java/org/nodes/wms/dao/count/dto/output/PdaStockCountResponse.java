package org.nodes.wms.dao.count.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * PDA显示盘点单搜索结果对象
 */
@Data
public class PdaStockCountResponse implements Serializable {

	private static final long serialVersionUID = -6620575369115340810L;
	/**
	 * 盘点单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long countBillId;

	/**
	 * 盘点单编码
	 */
	private String countBillNo;

	/**
	 * 单据创建人
	 */
	private String creator;

	/**
	 * 盘点单编码
	 */
	private Date createTime;
}
