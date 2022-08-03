package org.nodes.wms.controller.basics;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.basics.dictionary.DictionaryBiz;
import org.nodes.wms.dao.application.dto.output.DictionaryResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统字典 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.DICTIONARY_URL)
public class DictionaryController {

	private final DictionaryBiz dictionaryBiz;

	@GetMapping("/getListByCode")
	public R<List<DictionaryResponse>> getListByCode(String code) {
		return R.data(dictionaryBiz.getDictionaryResponseList(code));
	}
}
