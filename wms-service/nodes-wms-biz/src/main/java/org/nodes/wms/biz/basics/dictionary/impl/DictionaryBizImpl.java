package org.nodes.wms.biz.basics.dictionary.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.dictionary.DictionaryBiz;
import org.nodes.wms.dao.application.dto.output.DictionaryResponse;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典
 */
@Service
@RequiredArgsConstructor
public class DictionaryBizImpl implements DictionaryBiz {

	private final IDictService dictService;

	@Override
	public List<DictionaryResponse> getDictionaryResponseList(String code) {
		AssertUtil.notEmpty(code, "字典码不允许为空");
		List<Dict> dictionaryList = dictService.findSonDictByDictCode(code);
		return Func.copy(dictionaryList, DictionaryResponse.class);
	}

	@Override
	public Dict findZoneTypeOfAutoStore() {
		return dictService.findByDictValue(DictCodeConstant.ZONE_TYPE, "自动化存储区");
	}


}
