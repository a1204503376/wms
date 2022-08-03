package org.nodes.wms.dao.application.dto.scheduling;

import lombok.Data;

/**
 * 调度系统返回对象
 **/
@Data
public class SchedulingResponse {

	/**
	 * 状态码
	 */
	private Integer code;

	/**
	 * 返回内容
	 */
	private String msg;

	/**
	 * 数据对象
	 */
	private Object data;

	/**
	 * 判断是否返回失败
	 * @return true：失败，false：成功
	 */
	public boolean hasFailed(){
		return code != 0;
	}
}
