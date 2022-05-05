package org.nodes.wms.biz.basics.dictionary.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.dictionary.DictionaryBiz;
import org.nodes.wms.dao.application.dto.output.DictionaryResponse;
import org.nodes.wms.dao.basics.dictionary.DictionaryDao;
import org.nodes.wms.dao.basics.dictionary.entities.Dict;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典
 */
@Service
@RequiredArgsConstructor
public class DictionaryBizImpl implements DictionaryBiz {

	private final DictionaryDao dictionaryDao;

	@Override
	public List<DictionaryResponse> getDictionaryResponseList(String code) {
		AssertUtil.notEmpty(code, "字典码不允许为空");
		List<Dict> dictionaryList = dictionaryDao.listByCode(code);
		return Func.copy(dictionaryList, DictionaryResponse.class);
	}
}
