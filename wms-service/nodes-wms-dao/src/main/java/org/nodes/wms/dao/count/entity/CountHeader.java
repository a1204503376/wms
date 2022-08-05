package org.nodes.wms.dao.count.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.count.enums.CountByEnum;
import org.nodes.wms.dao.count.enums.StockCountStateEnum;
import org.springblade.core.mp.base.BaseEntity;

import java.time.LocalDateTime;

/**
 * 盘点单头表实体类
 */
@Data
@TableName("wms_count_header")
public class CountHeader extends BaseEntity {

	private static final long serialVersionUID = -4205987111403180448L;
	/**
	 * 盘点单id
	 */
	@TableId(value = "count_bill_id", type = IdType.ASSIGN_ID)
	private Long countBillId;
	/**
	 * 盘点单编码
	 */
	private String countBillNo;
	/**
	 * 库房ID
	 */
	private Long whId;
	/**
	 * 盘点单状态
	 */
	private StockCountStateEnum countBillState;

	/**
	 * 盘点方式
	 */
	private CountByEnum countBy;
	/**
	 * 备注
	 */
	private String countRemark;
	/**
	 * 盘点标签
	 */
	private Integer countTag;

	/**
	 * 盘点模式
	 */
	private String mode;
	/**
	 * 单据创建人员名称
	 */
	private String creator;
	/**
	 * 同步状态
	 */
	private Integer syncState;
	/**
	 * 单据创建时间
	 */
	private LocalDateTime procTime;


}
