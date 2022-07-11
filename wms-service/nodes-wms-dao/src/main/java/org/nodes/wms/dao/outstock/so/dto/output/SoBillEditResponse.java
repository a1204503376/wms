package org.nodes.wms.dao.outstock.so.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 出库单编辑信息响应类
 **/
@Data
public class SoBillEditResponse implements Serializable {

	private static final long serialVersionUID = -2262857697950582572L;

	/**
	 * 出库单头表
	 */
	private SoHeaderEditResponse soHeader;

	/**
	 * 出库单明细
	 */
	private List<SoDetailEditResponse> soDetailList;
}
