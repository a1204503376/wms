package org.nodes.wms.dao.task.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.nodes.wms.dao.task.enums.TaskDetailStatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 任务详情
 */
@Data
public class TaskDetail extends BaseSkuLotEntity {
	/**
	 * 任务明细id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 任务明细状态
	 */
	private TaskDetailStatusEnum taskDetailStatus;
	/**
	 * 任务头表id
	 */
	private Long taskHeaderId;
	/**
	 * 单据编码
	 */
	private String billNo;
	/**
	 * 单据行号
	 */
	private String billLineNo;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 层级
	 */
	private Integer skuLevel;
	/**
	 * 计量单位编码
	 */
	private String umCode;
	/**
	 * 计量单位名称
	 */
	private String umName;
	/**
	 * 计划数量
	 */
	private BigDecimal planQty;
	/**
	 * 实际任务执行数量
	 */
	private BigDecimal scanQty;
	/**
	 * 原库存id
	 */
	private Long stockId;
	/**
	 * 库存状态(0正常,1冻结)
	 */
	private StockStatusEnum stockStatus;
	/**
	 * 原库位id
	 */
	private Long locId;
	/**
	 * 目标库位id
	 */
	private Long targetLocId;
	/**
	 * 目标库位编码
	 */
	private String targetLocCode;
	/**
	 * 托盘号
	 */
	private String lpnCode;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 序列号
	 */
	private String snCode;
	/**
	 * 库房ID
	 */
	private Long whId;
	/**
	 * 货主ID
	 */
	private Long woId;
	/**
	 * 任务来源
	 */
	private String source;
	/**
	 * 任务执行者id
	 */
	private String executorUserId;
	/**
	 * 任务执行者code
	 */
	private String executorUserCode;
	/**
	 * 任务执行开始时间
	 */
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private Date beginTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 版本号
	 * 需要使用@Version注解
	 * 支持的数据类型只有:int,Integer,long,Long,Date,Timestamp,LocalDateTime
	 * 整数类型下 newVersion = oldVersion + 1
	 * newVersion 会回写到 entity 中
	 * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
	 * 在 update(entity, wrapper) 方法下, wrapper 不能复用!!!
	 */
	@Version
	private Integer version;
}
