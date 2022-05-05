package org.nodes.wms.dao.basics.dictionary.impl;

import org.nodes.wms.dao.basics.dictionary.DictionaryDao;
import org.nodes.wms.dao.basics.dictionary.entities.Dict;
import org.nodes.wms.dao.basics.dictionary.mapper.DictionaryMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典 DAO 实现类
 */
@Repository
public class DictionaryDaoImpl
	extends BaseServiceImpl<DictionaryMapper, Dict>
	implements DictionaryDao {

	@Override
	public List<Dict> listByCode(String code) {
		return super.lambdaQuery()
			.select(Dict::getDictKey, Dict::getDictValue)
			.eq(Dict::getCode, code)
			.eq(Dict::getIsSealed, 0)
			.ne(Dict::getParentId, 0)
			.list();
	}
}
