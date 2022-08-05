package org.nodes.wms.dao.count.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.count.enums.CountDetailStateEnum;
import org.springblade.core.mp.base.BaseEntity;

import java.time.LocalDateTime;

/**
 * 盘点单明细表
 */
@Data
@TableName("wms_count_detail")
public class CountDetail extends BaseEntity {

	private static final long serialVersionUID = -7300301286679301984L;

	/**
	 * 盘点单明细ID
	 */
	@TableId(value = "count_detail_id", type = IdType.ASSIGN_ID)
	private Long countDetailId;

	/**
	 * 盘点状态
	 */
	private CountDetailStateEnum countDetailState;
	/**
	 * 盘点单ID
	 */
	private Long countBillId;
	/**
	 * 单据编码
	 */
	private String countBillNo;
	/**
	 * 库位ID
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 货位状态
	 */
	private Integer locState;
	/**
	 * 物品ID
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
	 * 任务分组编码
	 */
	private String taskGroup;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 盘点时间
	 */
	private LocalDateTime procTime;
}
