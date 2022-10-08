package org.nodes.wms.dao.task.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springblade.core.tenant.mp.TenantEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * WMS系统任务处理
 *
 * @author nodesc
 */
@Data
@TableName("tsk_task")
public class WmsTask extends TenantEntity {

	/**
	 * 任务ID
	 */
	@TableId(value = "task_id", type = IdType.ASSIGN_ID)
	private Long taskId;

	/**
	 * 关联单据ID
	 */
	private Long billId;

	/**
	 * 关联明细ID
	 */
	private Long billDetailId;

	/**
	 * 单据编码
	 */
	private String billNo;

	/**
	 * 任务类型
	 */
	private WmsTaskTypeEnum taskTypeCd;

	/**
	 * 任务执行方式
	 */
	private WmsTaskProcTypeEnum taskProcType;

	/**
	 * 任务状态
	 */
	private WmsTaskStateEnum taskState;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 数量
	 */
	private BigDecimal taskQty;

	/**
	 * 实际数量
	 */
	private BigDecimal scanQty;

	/**
	 * 计量单位编码
	 */
	private String umCode;

	/**
	 * 批次号
	 */
	private String lot;

	/**
	 * 来源库位id
	 */
	private Long fromLocId;

	/**
	 * 来源库位编码
	 */
	private String fromLocCode;

	/**
	 * 目标库位id
	 */
	private Long toLocId;

	/**
	 * 目标库位code
	 */
	private String toLocCode;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * LPN编码
	 */
	private String lpnCode;

	/**
	 * 工作任务包ID
	 */
	private Long ttpId;

	/**
	 * 库房ID
	 */
	private Long whId;

	/**
	 * 工作区ID
	 */
	private Long wwaId;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户编码
	 */
	private String userCode;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 任务分组编码
	 */
	private String taskGroup;

	/**
	 * 任务描述
	 */
	private String taskRemark;

	/**
	 * 任务分派时间
	 */
	private LocalDateTime allotTime;

	/**
	 * 任务优先时间
	 */
	private LocalDateTime confirmDate;

	/**
	 * 任务开始执行的时间
	 */
	private LocalDateTime beginTime;

	/**
	 * 任务关闭时间
	 */
	private LocalDateTime closeTime;

	/**
	 * 任务说明
	 */
	private String remark;

	/**
	 * 用于区分C1,C2箱码的标志
	 * 1:C1
	 * 2:C2
	 */
	private Integer cBifurcate;

	@Version
	private Integer version;
}
