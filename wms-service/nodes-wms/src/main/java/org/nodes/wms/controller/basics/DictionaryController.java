package org.nodes.wms.controller.basics;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "字典管理", tags = "字典管理接口")
public class DictionaryController {

	private final DictionaryBiz dictionaryBiz;

	@ApiOperation(value = "获取字典值和名称", notes = "传入code")
	@GetMapping("/getListByCode")
	public R<List<DictionaryResponse>> getListByCode(String code) {
		return R.data(dictionaryBiz.getDictionaryResponseList(code));
	}
}
