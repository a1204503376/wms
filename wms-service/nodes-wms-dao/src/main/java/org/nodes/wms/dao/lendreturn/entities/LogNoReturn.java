package org.nodes.wms.dao.lendreturn.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;

import java.math.BigDecimal;

/**
 * 未归还日志表
 */
@Data
@TableName("log_no_return")
public class LogNoReturn  extends SkuLotBaseEntity {

	private static final long serialVersionUID = 4296291828718668979L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 借出人姓名
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
	 * 借出数量
	 */
	private BigDecimal lendQty;

	/**
	 * 归还数量
	 */
	private BigDecimal returnQty;

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
	 * 多个，英文逗号分割
	 */
	private String snCode;

	/**
	 * 货主
	 */
	private String woId;

	/**
	 * 库房id
	 */
	private Long whId;
}
