package org.nodes.wms.biz.instock.asn.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.enums.IPairs;
import org.nodes.wms.dao.application.dto.output.StateGeneralResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * ASN单状态
 **/
@Getter
@RequiredArgsConstructor
public enum AsnBillStateEnum
	implements IPairs<Integer,String,AsnBillStateEnum> {

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
	 * 已取消
	 */
	CANCEL(90, "已取消"),
	;

	private final Integer code;
	private final String desc;

	@Override
	public AsnBillStateEnum get() {
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

	public static List<StateGeneralResponse> getList() {
		List<StateGeneralResponse> list = new ArrayList<>();
		for (AsnBillStateEnum item : values()) {
			StateGeneralResponse stateGeneralResponse = new StateGeneralResponse();
			stateGeneralResponse.setLabel(item.desc);
			stateGeneralResponse.setValue(item.code);
			list.add(stateGeneralResponse);
		}
		return list;
	}
}
