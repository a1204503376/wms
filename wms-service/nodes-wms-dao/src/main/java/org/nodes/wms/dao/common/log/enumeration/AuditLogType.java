package org.nodes.wms.dao.common.log.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审计日志类型
 */
@Getter
@AllArgsConstructor
public enum AuditLogType {

	USER_LOGIN(101, "用户登录"),
	INSTOCK_BILL(102, "入库单单据操作"),
	INSTOCK(103, "入库操作"),
	OUTSTOCK_BILL(104, "出库单单据操作"),
	OUTSTOCK(105, "出库操作"),
	CRON_TASK(106,"定时任务操作");

	@EnumValue
	@JsonValue
	Integer index;
	String desc;

}
