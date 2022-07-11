package org.nodes.wms.dao.outstock.so.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengwei
 * @program WmsCore
 * @description 单据同步状态枚举
 * @since 2020-11-09
 */
@Getter
@AllArgsConstructor
public enum SyncStateEnum {

	DEFAULT(0, "同步完成"),
	DISPOSED(1, "处理完成"),
	UPLOADED(2, "上传完成")
	;

	Integer index;
	String name;
}
