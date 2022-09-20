package org.nodes.wms.core.warehouse.enums;

import org.springblade.core.tool.utils.Func;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 虚拟库区类型枚举
 * @since 2020-09-02
 */
public enum ZoneVirtualTypeEnum {

	MOVE(70, "移动暂存区"),
	PACK(72, "包装暂存区"),
	STAGE(73, "入库暂存区"),
	PICK(74, "出库暂存区"),
	QC(76, "入库质检区");

	private Integer index;
	private String name;

	ZoneVirtualTypeEnum(Integer index, String name) {
		this.index = index;
		this.name = name;
	}

	/**
	 * 判断是否为虚拟库位
	 *
	 * @param locCode 库位编码
	 * @return true:虚拟库位, false:不是虚拟库位
	 */
	public static boolean isVirtual(String locCode) {
		if (Func.isEmpty(locCode)) {
			return false;
		}
		List<String> virtualList = new ArrayList() {{
			add(ZoneVirtualTypeEnum.MOVE.toString().toLowerCase());
			add(ZoneVirtualTypeEnum.PACK.toString().toLowerCase());
			add(ZoneVirtualTypeEnum.STAGE.toString().toLowerCase());
			add(ZoneVirtualTypeEnum.PICK.toString().toLowerCase());
		}};
		return virtualList.stream().filter(u -> locCode.toLowerCase().startsWith(u)).count() > 0;
	}

	public static List<Integer> list() {
		List<Integer> list = new ArrayList() {{
			add(ZoneVirtualTypeEnum.MOVE.getIndex());
			add(ZoneVirtualTypeEnum.PACK.getIndex());
			add(ZoneVirtualTypeEnum.STAGE.getIndex());
			add(ZoneVirtualTypeEnum.PICK.getIndex());
		}};
		return list;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
