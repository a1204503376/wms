package org.nodes.wms.dao.application.dto.output;

import lombok.Data;

/**
 * 字典 返回对象
 * 使用场景：
 * 1.前端下拉框使用
 */
@Data
public class DictionaryResponse {

	/**
	 * 字典值
	 */
	private Integer dictKey;

	/**
	 * 字典名称
	 */
	private String dictValue;
}
