package org.nodes.wms.dao.task.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.nodes.wms.dao.task.enums.ProcTypeEnum;
import org.nodes.wms.dao.task.enums.TaskDetailStatusEnum;
import org.nodes.wms.dao.task.enums.TaskStateEnum;
import org.nodes.wms.dao.task.enums.TypeCdEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分页响应对象
 */
@Data
public class TaskDetailPageResponse extends BaseSkuLot implements Serializable {
	private static final long serialVersionUID = 4224020284017575638L;

	/**
	 * 任务类别(1上架2拣货3盘点)
	 */
	private TypeCdEnum typeCd;
	/**
	 * 任务执行方式
	 */
	private ProcTypeEnum procType;
	/**
	 * 任务状态(0=正常1=关闭2=已取消3=任务异常)
	 */
	private TaskStateEnum taskState;

	/**
	 * 任务明细状态
	 */
	private TaskDetailStatusEnum taskDetailStatus;
	/**
	 * 任务头表id
	 */
	private Long taskHeaderId;
	/**
	 * 单据id
	 */
	private Long billId;
	/**
	 * 单据编码
	 */
	private String billNo;
	/**
	 * 单据明细id
	 */
	private Long billDetailId;
	/**
	 * 单据行号
	 */
	private String billLineNo;
	/**
	 * 物品ID
	 */
	private Long skuId;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 包装ID
	 */
	private Long wspId;
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
	 * 货主名称
	 */
	private String ownerName;
	/**
	 * 库房名称
	 */
	private String whName;
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

}
