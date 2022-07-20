package org.nodes.wms.dao.outstock.so.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发货单分配页面信息响应类
 **/
@Data
public class SoBillDistributedResponse implements Serializable {

	private static final long serialVersionUID = 8058813058399076092L;

	/**
	 * 发货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 发货单编码
	 */
	private String soBillNo;

	/**
	 * 上游编码
	 */
	private String orderNo;

	/**
	 * 发货单明细
	 */
	private List<SoDetailForDistResponse> soDetailList;
}
