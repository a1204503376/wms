package org.nodes.wms.dao.instock.receive.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.enums.IPairs;
import org.nodes.wms.dao.application.dto.output.ReceiveBillStateResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 收货单状态
 **/
@Getter
@RequiredArgsConstructor
public enum ReceiveBillStateEnum
	implements IPairs<Integer,String,ReceiveBillStateEnum> {

	/**
	 *未收货
	 */
	NOT_RECEIPT(10, "未收货"),

	/**
	 * 部分收货
	 */
	PART(20,"部分收货"),

	/**
	 * 全部收货
	 */
	COMPLETED(30, "全部收货"),

	/**
	 * 关闭
	 */
	CLOSURE(40, "关闭"),

	/**
	 * 已取消
	 */
	CANCEL(90, "已取消"),
	;

	@EnumValue
	@JsonValue
	private final Integer code;
	private final String desc;

	@Override
	public ReceiveBillStateEnum get() {
		return this;
	}

	@Override
	public Integer key() {
		return this.code;
	}

	@Override
	public String value() {
		return this.desc;
	}

	public static List<ReceiveBillStateResponse> getList() {
		List<ReceiveBillStateResponse> list = new ArrayList<>();
		for (ReceiveBillStateEnum item : values()) {
			ReceiveBillStateResponse receiveBillStateResponse = new ReceiveBillStateResponse();
			receiveBillStateResponse.setLabel(item.desc);
			receiveBillStateResponse.setValue(item.code);
			list.add(receiveBillStateResponse);
		}
		return list;
	}
}
