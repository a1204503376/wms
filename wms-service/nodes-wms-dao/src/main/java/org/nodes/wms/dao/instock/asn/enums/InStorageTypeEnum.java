package org.nodes.wms.dao.instock.asn.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.enums.IPairs;
import org.nodes.wms.dao.application.dto.output.StateGeneralResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 入库方式
 */
@Getter
@RequiredArgsConstructor
public enum InStorageTypeEnum
	implements IPairs<Integer, String, InStorageTypeEnum> {

	/**
	 * 常规入库
	 */
	Normal(10, "常规入库"),
	/**
	 * 无单据入库
	 */
	NO_BILL(20, "无单据入库"),
	/**
	 * 越库入库
	 */
	CROSS(30, "越库入库"),
	;

	private final Integer code;
	private final String desc;

	@Override
	public InStorageTypeEnum get() {
		return this;
	}

	@Override
	public Integer key() {
		return this.getCode();
	}

	@Override
	public String value() {
		return this.desc;
	}

	public static List<StateGeneralResponse> getList() {
		List<StateGeneralResponse> list = new ArrayList<>();
		for (InStorageTypeEnum item : values()) {
			StateGeneralResponse stateGeneralResponse = new StateGeneralResponse();
			stateGeneralResponse.setLabel(item.desc);
			stateGeneralResponse.setValue(item.code);
			list.add(stateGeneralResponse);
		}
		return list;
	}
}
