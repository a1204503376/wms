package org.nodes.wms.dao.lendreturn.dto.output;

import lombok.Data;
import org.nodes.wms.dao.lendreturn.enums.LendReturnTypeEnum;
import org.nodes.wms.dao.stock.dto.SkuLotDto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 借出归还操作记录请求对象
 */
@Data
public class LendReturnResponse extends SkuLotDto {

	/**
	 * 借出归还类别
	 */
	private LendReturnTypeEnum type;

	/**
	 * 借出归还人姓名
	 */
	private String lendReturnName;

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
	 * 序列号
	 */
	private String snCode;

	/**
	 * 单据编码
	 */
	private String billNo;

	/**
	 * 创建时间
	 */
	private Date createTime;
}
