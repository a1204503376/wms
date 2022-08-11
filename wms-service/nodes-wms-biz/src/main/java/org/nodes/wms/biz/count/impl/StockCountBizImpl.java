package org.nodes.wms.biz.count.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.count.CountReportBiz;
import org.nodes.wms.biz.count.StockCountBiz;
import org.nodes.wms.biz.count.modular.CountReportFactory;
import org.nodes.wms.biz.count.modular.StockCountFactory;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.count.CountDetailDao;
import org.nodes.wms.dao.count.CountHeaderDao;
import org.nodes.wms.dao.count.dto.input.GenerateCountReport;
import org.nodes.wms.dao.count.dto.input.StockCountDetailRequest;
import org.nodes.wms.dao.count.dto.input.StockCountRequest;
import org.nodes.wms.dao.count.dto.output.*;
import org.nodes.wms.dao.count.entity.CountDetail;
import org.nodes.wms.dao.count.entity.CountHeader;
import org.nodes.wms.dao.count.entity.CountReport;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private final LocationBiz locationBiz;
	private final CountReportFactory countReportFactory;
	private final CountReportBiz countReportBiz;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(StockCountRequest stockCountRequest) {
		CountHeader countHeader;
		if (Func.notNull(stockCountRequest.getCountBillId())) {
			CountHeader countHeaderById = countHeaderDao.getById(stockCountRequest.getCountBillId());
			AssertUtil.notNull(countHeaderById, "盘点单为空，ID:{}", stockCountRequest.getCountBillId());

			boolean countDetailIdFlag = stockCountRequest.getStockCountDetailRequestList().stream()
				.anyMatch(d -> Func.isNull(d.getCountDetailId()));
			if (countDetailIdFlag) {
				throw ExceptionUtil.mpe("编辑盘点时明细主键ID不允许为空");
			}

			countHeader = stockCountFactory.getCountHeader(stockCountRequest, countHeaderById);
			// 每次编辑删除旧的明细
			List<Long> countDetailIdList = NodesUtil.toList(stockCountRequest.getStockCountDetailRequestList(), StockCountDetailRequest::getCountDetailId);
			boolean deleteByIdFlag = countDetailDao.deleteByIds(countDetailIdList);
			if (Boolean.FALSE == deleteByIdFlag) {
				throw ExceptionUtil.mpe("删除盘点单明细失败，参数：{}", countDetailIdList);
			}
		} else {
			countHeader = stockCountFactory.createCountHeader(stockCountRequest);
		}

		List<CountDetail> countDetail = stockCountFactory.createCountDetail(stockCountRequest);

		countHeaderDao.saveOrUpdate(countHeader);
		countDetailDao.saveBatch(countDetail);
	}

	@Override
	public List<PdaStockCountResponse> getPdaStockCountResponseList(String countBillNo) {
		AssertUtil.notEmpty(countBillNo, "盘点单号为空");
		List<CountHeader> countHeaderList = countHeaderDao.selectByCountBillNo(countBillNo);
		return Func.copy(countHeaderList, PdaStockCountResponse.class);
	}

	@Override
	public List<PdaStockCountDetailResponse> getPdaStockCountDetailResponseList(Long countBillId) {
		List<CountDetail> countDetailList = countDetailDao.selectByCountBillId(countBillId);
		// 统计箱内物品数量
		List<PdaStockCountDetailResponse> responseList = BeanUtil.copy(countDetailList, PdaStockCountDetailResponse.class);
		responseList.forEach(response -> {
			List<PdaBoxQtyResponse> pdaBoxQtyResponseList = stockQueryBiz.getStockCountByLocCode(response.getLocCode());
			response.setPdaBoxQtyResponseList(pdaBoxQtyResponseList);
			Location location = locationBiz.findByLocId(response.getLocId());
			response.setIsPickLocation(locationBiz.isPickLocation(location));
		});
		return responseList;
	}

	@Override
	public List<PdaBoxSkuQtyResponse> getPdaBoxSkuQtyResponseList(String boxCode, String locId) {
		return null;
	}

	@Override
	public List<PdaSkuQtyResponse> getPdaSkuQtyResponseList(String boxCode) {
		AssertUtil.notEmpty(boxCode, "修改差异数量根据箱码，查询数据失败，箱码为空");
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(boxCode);
		AssertUtil.notNull(stockList, "修改差异数量根据箱码，查询数据失败,根据箱码查询出的库存集合为空");
		return BeanUtil.copy(stockList, PdaSkuQtyResponse.class);
	}

	@Override
	public void generateCountReport(List<GenerateCountReport> countReportList) {
		AssertUtil.notNull(countReportList, "生成盘点差异报告失败，用户提交的数据为空");
		countReportList.forEach(generateCountReport -> {
			Stock stock = stockQueryBiz.findStockById(generateCountReport.getStockId());
			CountDetail countDetail = countDetailDao.selectCountDetailByCode(stock.getLocCode(), stock.getBoxCode());
			CountReport countReport = countReportFactory.createCountReport(generateCountReport, stock, countDetail);
			countReportBiz.insertCountReport(countReport);
		});
	}


}
