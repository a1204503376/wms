package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 出库单查看详细间或日志记录响应类
 **/
@Data
public class LogSoPickForSoDetailResponse implements Serializable {

	private static final long serialVersionUID = -2352392524517922769L;

	/**
	 * 业务发生时间
	 */
	private Date procTime;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 波次编码
	 */
	private String wellenNo;

	/**
	 * 拣货量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal pickRealQty;

	/**
	 * 包装名称
	 */
	private String wspName;

	/**
	 * 计量单位名称
	 */
	private String wsuName;

	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 型号
	 */
	private String skuLot2;
	/**
	 * 专属客户
	 */
	private String skuLot4;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 货主名称
	 */
	private String ownerName;
}
