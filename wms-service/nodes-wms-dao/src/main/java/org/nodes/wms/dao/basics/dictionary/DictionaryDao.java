package org.nodes.wms.dao.basics.dictionary;

import org.nodes.wms.dao.basics.dictionary.entities.Dict;

import java.util.List;

/**
 * 字典 DAO接口
 */
public interface DictionaryDao {

	/**
	 * 根据字典码查询字典集合
	 *
	 * @param code 字典码
	 * @return List<DictionaryResponse>
	 */
	List<Dict> listByCode(String code);
}
