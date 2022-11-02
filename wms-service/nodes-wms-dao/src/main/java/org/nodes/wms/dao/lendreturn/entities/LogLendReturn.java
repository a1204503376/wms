package org.nodes.wms.dao.lendreturn.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.lendreturn.enums.LendReturnTypeEnum;

import java.math.BigDecimal;

/**
 * 借出归还日志表
 */
@Data
@TableName("log_lend_return")
public class LogLendReturn extends SkuLotBaseEntity {

	private static final long serialVersionUID = 8582461649474119326L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
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
	private Long woId;

	/**
	 * 库房id
	 */
	private Long whId;
}
