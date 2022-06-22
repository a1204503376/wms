package org.nodes.wms.pdaController.instock.receive;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.dao.instock.receive.dto.input.*;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailByReceiveIdPdaResponse;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailPdaResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderPdaResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 收货管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/receiveByPcs")
public class ReceiveByPcsController {
	private final ReceiveBiz receiveBiz;

	/**
	 * PDA收货管理查询
	 */
	@ApiLog("PDA收货管理查询")
	@PostMapping("/list")
	public R<Page<ReceiveHeaderPdaResponse>> list(@RequestBody ReceivePdaQuery receivePdaQuery, Query query) {
		Page<ReceiveHeaderPdaResponse> pages = receiveBiz.getReceiveListByReceiveNo(receivePdaQuery,query);
		return R.data(pages);
	}

	/**
	 * @param receiveDetailPdaQuery 按单收货详情页面查询条件
	 * @return
	 */
	@PostMapping("/findDetailListByReceiveId")
	public R<List<DetailReceiveDetailPdaResponse>> findDetailListByReceiveId(@RequestBody ReceiveDetailPdaQuery receiveDetailPdaQuery) {
		List<DetailReceiveDetailPdaResponse> listByReceiveId = receiveBiz.getDetailListByReceiveId(receiveDetailPdaQuery);
		return R.data(listByReceiveId);
	}

	/**
	 * @param receiveDetailByReceiveIdPdaQuery 请求参数
	 * @return 当前收货单详情，以及他是否是序列号管理 isSn
	 */
	@PostMapping("findDetailByReceiveId")
	public R<ReceiveDetailByReceiveIdPdaResponse> findDetailByReceiveId(@RequestBody ReceiveDetailByReceiveIdPdaQuery receiveDetailByReceiveIdPdaQuery)
	{
		return null;
	}

	/**
	 * @param pdaByPieceReceiveQuery PDA按件收货请求参数
	 * @return 是否成功
	 */
	@PostMapping("pdaByPieceReceive")
	public R pdaByPieceReceive(@RequestBody PdaByPieceReceiveQuery pdaByPieceReceiveQuery){
		return null;
	}

}
