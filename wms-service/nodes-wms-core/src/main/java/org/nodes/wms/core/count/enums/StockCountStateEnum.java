package org.nodes.wms.core.count.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springblade.core.tool.utils.StringPool;

/**
 * @author pengwei
 * @program WmsCore
 * @description 任务执行方式枚举
 * @create 20200214
 */
@Getter
@AllArgsConstructor
public enum StockCountStateEnum {

	CREATE("等待分配任务", 10),
	COUNTING("正在盘点", 20),
	COUNT_COMPLATED("盘点完成", 30),
	CREATE_COUNT_DIFF("生成差异", 31),
	INVALID("作废", 40),
	UPLOADED("已上传", 50),
	AUDIT_PASS("审核通过", 60),
	AUDIT_UN_PASS("审核未通过", 70),
	ADJUSTMENT("已调整", 80),
	;

	private String name;
	private Integer index;

	public static String valueOf(Integer index){
		switch(index) {
			case 10:
				return CREATE.name;
			case 20:
				return COUNTING.name;
			case 30:
				return COUNT_COMPLATED.name;
			case 31:
				return CREATE_COUNT_DIFF.name;
			case 40:
				return INVALID.name;
			case 50:
				return UPLOADED.name;
			case 60:
				return AUDIT_PASS.name;
			case 70:
				return AUDIT_UN_PASS.name;
			case 80:
				return ADJUSTMENT.name;
			default:
				return StringPool.EMPTY;
		}
	}
}
