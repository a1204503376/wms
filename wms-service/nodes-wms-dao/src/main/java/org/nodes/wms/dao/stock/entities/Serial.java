package org.nodes.wms.dao.stock.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 序列号实体类
 **/
@Data
@TableName("wms_serial")
public class Serial extends TenantEntity {

	private static final long serialVersionUID = -453322099134024067L;

	/**
	 * 序列号id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long serialId;

	/**
	 * 库存id
	 */
	private Long stockId;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 序列号
	 */
	private String serialNumber;

	/**
	 * 序列号状态(0在库)
	 */
	private SerialStateEnum serialState;

	/**
	 * 入库次数
	 */
	private Integer instockNumber;

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
}
