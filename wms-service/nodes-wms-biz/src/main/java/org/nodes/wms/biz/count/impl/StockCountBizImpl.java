package org.nodes.wms.biz.count.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.biz.count.StockCountBiz;
import org.nodes.wms.biz.count.modular.StockCountFactory;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.count.CountDetailDao;
import org.nodes.wms.dao.count.CountHeaderDao;
import org.nodes.wms.dao.count.dto.input.StockCountDetailRequest;
import org.nodes.wms.dao.count.dto.input.StockCountRequest;
import org.nodes.wms.dao.count.dto.output.PdaBoxQtyResponse;
import org.nodes.wms.dao.count.dto.output.PdaBoxSkuQtyResponse;
import org.nodes.wms.dao.count.dto.output.PdaStockCountDetailResponse;
import org.nodes.wms.dao.count.dto.output.PdaStockCountResponse;
import org.nodes.wms.dao.count.entity.CountDetail;
import org.nodes.wms.dao.count.entity.CountHeader;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 盘点 业务类
 */
@Service
@RequiredArgsConstructor
public class StockCountBizImpl implements StockCountBiz {

	private final StockCountFactory stockCountFactory;
	private final CountHeaderDao countHeaderDao;
	private final CountDetailDao countDetailDao;
	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(StockCountRequest stockCountRequest) {
		CountHeader countHeader;
		if (Func.notNull(stockCountRequest.getCountBillId())){
			CountHeader countHeaderById = countHeaderDao.getById(stockCountRequest.getCountBillId());
			AssertUtil.notNull(countHeaderById,"盘点单为空，ID:{}",stockCountRequest.getCountBillId());

			boolean countDetailIdFlag = stockCountRequest.getStockCountDetailRequestList().stream()
				.anyMatch(d -> Func.isNull(d.getCountDetailId()));
			if (countDetailIdFlag){
				throw ExceptionUtil.mpe("编辑盘点时明细主键ID不允许为空");
			}

			countHeader=stockCountFactory.getCountHeader(stockCountRequest,countHeaderById);
			// 每次编辑删除旧的明细
			List<Long> countDetailIdList = NodesUtil.toList(stockCountRequest.getStockCountDetailRequestList(), StockCountDetailRequest::getCountDetailId);
			boolean deleteByIdFlag = countDetailDao.deleteByIds(countDetailIdList);
			if (Boolean.FALSE == deleteByIdFlag){
				throw ExceptionUtil.mpe("删除盘点单明细失败，参数：{}",countDetailIdList);
			}
		}else {
			countHeader = stockCountFactory.createCountHeader(stockCountRequest);
		}

		List<CountDetail> countDetail = stockCountFactory.createCountDetail(stockCountRequest);

		countHeaderDao.saveOrUpdate(countHeader);
		countDetailDao.saveBatch(countDetail);
	}

	@Override
	public List<PdaStockCountResponse> getPdaStockCountResponseList(String countBillNo) {
		AssertUtil.notEmpty(countBillNo,"盘点单号为空");
		List<CountHeader> countHeaderList = countHeaderDao.selectByCountBillNo(countBillNo);
		return Func.copy(countHeaderList, PdaStockCountResponse.class);
	}

	@Override
	public List<PdaStockCountDetailResponse> getPdaStockCountDetailResponseList(Long countBillId) {
		List<CountDetail> countDetailList = countDetailDao.selectByCountBillId(countBillId);
		// 统计箱内物品数量
		List<PdaStockCountDetailResponse> responseList = BeanUtil.copy(countDetailList, PdaStockCountDetailResponse.class);
        responseList.forEach(response ->{
			List<PdaBoxQtyResponse> pdaBoxQtyResponseList= new ArrayList<>();
			PdaBoxQtyResponse pdaBoxQtyResponse = new PdaBoxQtyResponse();
			List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(response.getBoxCode());
			BigDecimal stockBalance = StockUtil.getStockBalance(stockList);
            pdaBoxQtyResponse.setBoxCode(response.getBoxCode());
			pdaBoxQtyResponse.setTotalQty(stockBalance);
			pdaBoxQtyResponseList.add(pdaBoxQtyResponse);
			response.setPdaBoxQtyResponseList(pdaBoxQtyResponseList);
		});
		return responseList;
	}

	@Override
	public List<PdaBoxSkuQtyResponse> getPdaBoxSkuQtyResponseList(String boxCode, String locId) {
		return null;
	}


}
