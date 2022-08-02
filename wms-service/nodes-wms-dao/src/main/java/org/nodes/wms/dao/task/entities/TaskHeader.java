package org.nodes.wms.dao.task.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nodes.wms.dao.task.enums.ProcTypeEnum;
import org.nodes.wms.dao.task.enums.TaskStateEnum;
import org.nodes.wms.dao.task.enums.TypeCdEnum;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务头表
 */
@Data
@Deprecated
public class TaskHeader extends TenantEntity implements Serializable {
	private static final long serialVersionUID = -7346133644795089970L;
	/**
	 * 任务主键ID
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 任务类别(1上架2拣货3盘点)
	 */
	private TypeCdEnum typeCd;
	/**
	 * 任务执行方式
	 */
	private ProcTypeEnum procType;
	/**
	 * 库房id
	 */
	private Long whId;
	/**
	 * 任务状态(0=正常1=关闭2=已取消3=任务异常)
	 */
	private TaskStateEnum taskState;
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
	 * 任务执行结束时间
	 */
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private Date endTime;
	/**
	 * 备注
	 */
	private String remark;

}
