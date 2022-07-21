package org.nodes.wms.dao.picking.dto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * 拣货分页查询响应对象
 **/
@Data
public class FindAllPickingResponse implements Serializable {
	private static final long serialVersionUID = 1486941751771336282L;
	/**
	 * 发货单编码
	 */
	private String soBillNo;

	/**
	 * 单据种类编码
	 */
	private String billTypeCd;

}
