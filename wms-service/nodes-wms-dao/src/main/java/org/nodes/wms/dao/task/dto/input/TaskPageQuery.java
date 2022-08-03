package org.nodes.wms.dao.task.dto.input;

import lombok.Data;

import java.util.List;

/**
 * 工作任务分页查询条件
 */
@Data
public class TaskPageQuery {
	//任务id、单据编码、类型、状态、物品编码
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 单据编码
	 */
	private String billNo;
	/**
	 * 任务类型
	 */
	private List<String> taskTypeCdList;
	/**
	 * 任务状态
	 */
	private List<String> taskStateList;
	/**
	 * 物品编码
	 */
	private List<Long> skuIdList;
}
