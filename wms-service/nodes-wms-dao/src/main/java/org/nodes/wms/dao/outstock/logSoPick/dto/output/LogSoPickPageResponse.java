package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 拣货记录日志分页响应类
 **/
@Data
public class LogSoPickPageResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = -3534787471846284616L;

	/**
	 * 拣货记录id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long lsopId;

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
	 * 单据类型
	 */
	private String billTypeName;

	/**
	 * 行号
	 */
	private String soLineNo;

	/**
	 * 物品id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * 拣货量
	 */
	private BigDecimal pickRealQty;

	/**
	 * 计量单位编码
	 */
	private String wsuCode;

	/**
	 * 计量单位
	 */
	private String wsuName;

	/**
	 * 序列号
	 */
	private String snCode;

	/**
	 * lpn编码
	 */
	private String lpnCode;

	/**
	 * 拣货时间
	 */
	private Date createTime;

	/**
	 * 拣货人
	 */
	private String createUserName;

	/**
	 * 撤销拣货记录id
	 */
	private String cancelLogId;
}
