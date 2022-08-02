package org.nodes.wms.dao.lendreturn.dto.input;

import lombok.Data;
import org.nodes.wms.dao.lendreturn.enums.LendReturnTypeEnum;
import org.nodes.wms.dao.stock.dto.SkuLotDto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 借出归还记录请求对象
 */
@Data
public class LogLendReturnRequest extends SkuLotDto
	implements Serializable {

	private static final long serialVersionUID = -6662084814810932439L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 借出归还类别
	 */
	private LendReturnTypeEnum type;

	/**
	 * 借出归还人姓名
	 */
	private String lendReturnName;

	/**
	 * 库位id
	 */
	private Long locId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 物品id
	 */
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
	 * 数量
	 */
	private BigDecimal qty;

	/**
	 * 计量单位编码
	 */
	private String wsuCode;

	/**
	 * 计量单位名称
	 */
	private String wsuName;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 序列号
	 */
	private String snCode;

	/**
	 * 单据id
	 */
	private Long billId;

	/**
	 * 单据编码
	 */
	private String billNo;

	/**
	 * 明细id
	 */
	private Long detailId;

	/**
	 * 货主
	 */
	private String woId;

	/**
	 * 库房id
	 */
	private Long whId;
}
