package org.nodes.wms.dao.stock.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 序列号日志类型
 *
 * @author nodesc
 */
@Getter
@RequiredArgsConstructor
public enum SerialLogTypeEnum {
	NEW(0, "序列号新增"),
	UPDATE(1, "序列号更新"),
	DELETE(2, "序列号删除");


	@EnumValue
	private final Integer code;

	@JsonValue
	private final String desc;
}
