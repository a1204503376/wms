package org.nodes.wms.pdaController.instock.receive;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.instock.InStockBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.location.dto.input.LocationPdaByPcsRequest;
import org.nodes.wms.dao.basics.location.dto.output.LocationPdaByPcsResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.instock.receive.dto.input.*;
import org.nodes.wms.dao.instock.receive.dto.output.PdaByPcsReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailByReceiveIdPdaResponse;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailPdaResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderPdaResponse;
import org.nodes.wms.dao.stock.dto.input.StockPdaByPcsRequest;
import org.nodes.wms.dao.stock.dto.output.StockPdaByPcsResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 按件收货API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/receiveByPcs")
public class ReceiveByPcsController {
	private final ReceiveBiz receiveBiz;
	private final InStockBiz inStockBiz;
	private final LocationBiz locationBiz;
	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;

	/**
	 * PDA按件收货:收货管理查询
	 */
	@PostMapping("/list")
	public R<Page<ReceiveHeaderPdaResponse>> list(@RequestBody ReceivePdaQuery receivePdaQuery, Query query) {
		Page<ReceiveHeaderPdaResponse> pages = receiveBiz.getReceiveListByReceiveNo(receivePdaQuery, query);
		return R.data(pages);
	}

	/**
	 * PDA按件收货:收货详情查询
	 *
	 * @param receiveDetailPdaQuery 按单收货详情页面查询条件
	 * @return 按单收货详情集合
	 */
	@PostMapping("/findDetailListByReceiveId")
	public R<List<DetailReceiveDetailPdaResponse>> findDetailListByReceiveId(@RequestBody ReceiveDetailPdaQuery receiveDetailPdaQuery) {
		List<DetailReceiveDetailPdaResponse> listByReceiveId = receiveBiz.getDetailListByReceiveId(receiveDetailPdaQuery);
		return R.data(listByReceiveId);
	}

	/**
	 * PDA按件收货:收货单详情明细查询
	 *
	 * @param receiveDetailByReceiveIdPdaQuery 请求参数
	 * @return 当前收货单详情，以及他是否是序列号管理 isSn
	 */
	@PostMapping("findDetailByReceiveDetailId")
	public R<ReceiveDetailByReceiveIdPdaResponse> findDetailByReceiveDetailId(@RequestBody ReceiveDetailByReceiveIdPdaQuery receiveDetailByReceiveIdPdaQuery) {
		ReceiveDetailByReceiveIdPdaResponse detail = receiveBiz.selectDetailByReceiveDetailId(receiveDetailByReceiveIdPdaQuery);
		return R.data(detail);
	}

	/**
	 * PDA按件收货:按件收货
	 *
	 * @param pdaByPieceReceiveQuery PDA按件收货请求参数
	 * @return 是否成功
	 */
	@ApiLog("PDA按件收货")
	@PostMapping("receiptByPcs")
	public R<PdaByPcsReceiveResponse> receiptByPcs(@RequestBody PdaByPieceReceiveRequest pdaByPieceReceiveQuery) {
		return R.data(inStockBiz.receiptByPcs(pdaByPieceReceiveQuery));
	}

	/**
	 * PDA按件收货:库位查询
	 *
	 * @param request 请求对象包含参数-库房id-库位编码
	 * @return 库位
	 */
	@PostMapping("findThisLocationByLocCode")
	public R<LocationPdaByPcsResponse> findThisLocationByLocCode(@RequestBody LocationPdaByPcsRequest request) {
		Location location = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		LocationPdaByPcsResponse locationPdaByPcsResponse = BeanUtil.copy(location, LocationPdaByPcsResponse.class);
		return R.data(locationPdaByPcsResponse);
	}

	/**
	 * PDA按件收货:查询库存
	 *
	 * @param request 根据箱码查询库存
	 * @return 库位信息
	 */
	@PostMapping("findThisStockByBoxCode")
	public R<List<StockPdaByPcsResponse>> findThisStockByBoxCode(@RequestBody StockPdaByPcsRequest request) {
		List<Stock> stockList = stockQueryBiz.findStockByBoxCode(request.getBoxCode());
		List<StockPdaByPcsResponse> responseList = BeanUtil.copy(stockList, StockPdaByPcsResponse.class);
		return R.data(responseList);
	}

	/**
	 * PDA按件收货:查询已经存在的序列号集合
	 *
	 * @param request 收货单序列号集合
	 * @return 已经存在的序列号集合
	 */
	@PostMapping("getSerialNumberList")
	public R<List<String>> getSerialNumberList(@RequestBody ReceiveSerialNoListRequest request) {
		List<Serial> serialList = stockQueryBiz.findSerialBySerialNo(request.getSerialNumberList());
		List<String> serialNumberList = serialList.stream()
			.map(Serial::getSerialNumber)
			.distinct()
			.collect(Collectors.toList());
		return R.data(serialNumberList);
	}


}
