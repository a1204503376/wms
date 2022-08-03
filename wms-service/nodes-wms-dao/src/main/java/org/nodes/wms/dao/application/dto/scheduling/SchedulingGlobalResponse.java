package org.nodes.wms.dao.application.dto.scheduling;

import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;

/**
 * 调度系统全局返回结果对象
 **/
@Data
public class SchedulingGlobalResponse {

	private Boolean flagException;

	private String msg;

	private SchedulingResponse schedulingResponse;

	public static SchedulingGlobalResponse success(SchedulingResponse scheduling) {
		SchedulingGlobalResponse schedulingGlobal = new SchedulingGlobalResponse();
		schedulingGlobal.setSchedulingResponse(scheduling);
		schedulingGlobal.setFlagException(Boolean.FALSE);
		return schedulingGlobal;
	}

	public static SchedulingGlobalResponse error(Exception e) {
		SchedulingGlobalResponse schedulingGlobal = new SchedulingGlobalResponse();
		schedulingGlobal.setFlagException(Boolean.TRUE);
		schedulingGlobal.setMsg(e.getMessage());
		return schedulingGlobal;
	}

	public static boolean hasException(SchedulingGlobalResponse schedulingGlobal) {
		Assert.notNull(schedulingGlobal, "请求调度系统异常对象为空");
		return BooleanUtils.isTrue(schedulingGlobal.getFlagException());
	}
}
