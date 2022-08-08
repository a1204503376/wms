package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 序列号分页查询请求类
 **/
@Data
public class SerialLogPageResponse implements Serializable {

	private static final long serialVersionUID = 2229008480859821396L;

	/**
	 * 序列号id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wlsnlId;

	/**
	 * 序列号
	 */
	private String serialNumber;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 库位
	 */
	private String locCode;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * LPN编码
	 */
	private String lpnCode;

	/**
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 入库日期
	 */
	private Date createTime;

	/**
	 * 序列号状态
	 */
	private SerialStateEnum serialState;
}
