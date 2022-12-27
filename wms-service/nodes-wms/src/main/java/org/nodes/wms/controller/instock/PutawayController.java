package org.nodes.wms.controller.instock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.putaway.PutawayBiz;
import org.nodes.wms.dao.putaway.dto.input.PutawayPageQuery;
import org.nodes.wms.dao.putaway.dto.output.PutawayLogResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 上级记录API
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "putaway")
public class PutawayController {

	private final PutawayBiz putawayBiz;

	/**
	 * 上架记录：分页
	 */
	@PostMapping("/page")
	public R<IPage<PutawayLogResponse>> page(Query query, @RequestBody PutawayPageQuery putawayPageQuery) {
		return R.data(putawayBiz.getPutawayLogPage(query, putawayPageQuery));
	}

	@PostMapping("/export")
	public void export(@RequestBody PutawayPageQuery putawayPageQuery, HttpServletResponse response){
		putawayBiz.exportPutawayLog(putawayPageQuery, response);
	}
}
