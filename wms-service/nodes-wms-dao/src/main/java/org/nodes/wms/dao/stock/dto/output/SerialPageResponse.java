package org.nodes.wms.dao.stock.dto.output;

/**
 * 序列号响应类
 **/

import lombok.Data;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;

import java.io.Serializable;
@Data
public class SerialPageResponse implements Serializable {

	private static final long serialVersionUID = -843444599029741997L;

	/**
	 * 序列号id
	 */
	private Long serialId;

	/**
	 * 序列号
	 */
	private String serialNumber;

	/**
	 * 序列号状态
	 */
	private SerialStateEnum serialState;

	/**
	 * 入库次数
	 */
	private String instockNumber;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * lpn编码
	 */
	private String lpnCode;
}
