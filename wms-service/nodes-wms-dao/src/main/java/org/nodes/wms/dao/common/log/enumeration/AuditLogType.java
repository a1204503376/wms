package org.nodes.wms.dao.common.log.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nodes.core.tool.enums.IPairs;
import org.nodes.wms.dao.application.dto.output.AuditLogTypeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 审计日志类型
 */
@Getter
@AllArgsConstructor
public enum AuditLogType implements IPairs<Integer, String, AuditLogType> {

	USER_LOGIN(101, "用户登录"),
	INSTOCK_BILL(102, "入库单单据操作"),
	INSTOCK(103, "入库操作"),
	OUTSTOCK_BILL(104, "出库单单据操作"),
	OUTSTOCK(105, "出库操作"),
	CRON_TASK(106, "定时任务操作"),
	RECEIVE_BILL(107, "收货单单据操作"),
	ASN_BILL(108, "ASN单单据操作"),
	STOCK_TYPE(109, "库存状态操作"),
	AGV_TASK(110, "AGV任务"),
	DISTRIBUTE_STRATEGY(112, "分配"),
	MOVE_STOCK(113, "移动操作"),;

	@EnumValue
	Integer index;
	@JsonValue
	String desc;

	@Override
	public AuditLogType get() {
		return this;
	}

	@Override
	public Integer key() {
		return this.index;
	}

	@Override
	public String value() {
		return this.desc;
	}

	public static List<AuditLogTypeResponse> getList() {
		List<AuditLogTypeResponse> list = new ArrayList<>();
		for (AuditLogType item : values()) {
			AuditLogTypeResponse auditLogTypeResponse = new AuditLogTypeResponse();
			auditLogTypeResponse.setLabel(item.desc);
			auditLogTypeResponse.setValue(item.index);
			list.add(auditLogTypeResponse);
		}
		return list;
	}
}
