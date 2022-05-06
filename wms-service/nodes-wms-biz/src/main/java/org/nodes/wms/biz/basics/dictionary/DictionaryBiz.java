package org.nodes.wms.biz.basics.dictionary;

import org.nodes.wms.dao.application.dto.output.DictionaryResponse;

import java.util.List;

/**
 * 字典 业务层接口
 */
public interface DictionaryBiz {

	/**
	 * 根据字典码获取对应的字典集合
	 * @param code 字典码
	 * @return List<DictionaryResponse>
	 */
	List<DictionaryResponse> getDictionaryResponseList(String code);
}