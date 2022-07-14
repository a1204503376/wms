package org.nodes.wms.dao.task.dto.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 分页参数
 **/
@Data
public class TaskDetailPageRequest implements Serializable {

	private static final long serialVersionUID = 2908606289380887487L;
	/**
	 * 单据编码
	 */
	private String billNo;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 库存状态(0正常,1冻结)
	 */
	private Integer stockStatus;
	/**
	 * 托盘号
	 */
	private String lpnCode;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 任务执行者code
	 */
	private String executorUserCode;
	/**
	 * 任务执行开始时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	private Date beginTime;
	/**
	 * 更新时间开始
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private Date updateTimeBegin;
	/**
	 * 更新时间结束
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private Date updateTimeEnd;
}
