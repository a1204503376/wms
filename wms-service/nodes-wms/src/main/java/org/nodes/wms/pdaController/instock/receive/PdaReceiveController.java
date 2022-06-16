package org.nodes.wms.pdaController.instock.receive;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.dao.instock.receive.dto.input.*;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailPdaResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderPdaResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 收货管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/receive")
public class PdaReceiveController {
	private final ReceiveBiz receiveBiz;

	/**
	 * PDA收货管理查询
	 */
	@PostMapping("/list")
	public R<List<ReceiveHeaderPdaResponse>> list(@RequestBody ReceivePdaQuery receivePdaQuery) {
		List<ReceiveHeaderPdaResponse> pages = receiveBiz.getReceiveListByReceiveNo(receivePdaQuery);
		return R.data(pages);
	}

	/**
	 * @param receiveDetailPdaQuery 按单收货页面二查询条件
	 * @return 可以收货的明细列表
	 */
	@PostMapping("/findDetailListByReceiveId")
	public R<List<DetailReceiveDetailPdaResponse>> findDetailListByReceiveId(@RequestBody ReceiveDetailPdaQuery receiveDetailPdaQuery) {
		List<DetailReceiveDetailPdaResponse> listByReceiveId = receiveBiz.getDetailListByReceiveId(receiveDetailPdaQuery);
		return R.data(listByReceiveId);
	}
}
