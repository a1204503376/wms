package org.nodes.wms.biz.count.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.count.CountReportBiz;
import org.nodes.wms.biz.count.StockCountBiz;
import org.nodes.wms.biz.count.modular.CountRecordFactory;
import org.nodes.wms.biz.count.modular.CountReportFactory;
import org.nodes.wms.biz.count.modular.StockCountFactory;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.dao.count.CountDetailDao;
import org.nodes.wms.dao.count.CountHeaderDao;
import org.nodes.wms.dao.count.CountRecordDao;
import org.nodes.wms.dao.count.dto.input.*;
import org.nodes.wms.dao.count.dto.output.*;
import org.nodes.wms.dao.count.entity.CountDetail;
import org.nodes.wms.dao.count.entity.CountHeader;
import org.nodes.wms.dao.count.entity.CountRecord;
import org.nodes.wms.dao.count.enums.CountDetailStateEnum;
import org.nodes.wms.dao.count.enums.StockCountStateEnum;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
	private final StockQueryBiz stockQueryBiz;
	private final LocationBiz locationBiz;
	private final CountReportFactory countReportFactory;
	private final CountRecordFactory countRecordFactory;
	private final CountReportBiz countReportBiz;
	private final SkuBiz skuBiz;
	private final CountRecordDao countRecordDao;

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
			List<PdaBoxQtyResponse> pdaBoxQtyResponseList = stockQueryBiz.getStockCountByLocCode(response.getLocCode(), response.getBoxCode(), null);
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
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void generateCountReport(List<GenerateCountReport> countReportList) {
		AssertUtil.notNull(countReportList, "生成盘点单记录失败，用户提交的数据为空");
		countReportList.forEach(generateCountReport -> {

			if (Func.isNotEmpty(generateCountReport.getStockId())) {
				Stock stock = stockQueryBiz.findStockById(generateCountReport.getStockId());
				CountDetail countDetail = countDetailDao.selectCountDetailByCode(generateCountReport.getLocCode(), generateCountReport.getBoxCode());
				CountRecord countRecord = countRecordFactory.createCountRecord(generateCountReport, stock, countDetail);
				SkuUm um = skuBiz.findSkuUmByUmCode(stock.getWsuCode());
				countRecord.setWsuName(um.getWsuName());
				countRecordDao.insert(countRecord);
				countDetailDao.updateCountDetailStateByCountDetailId(countRecord.getCountBillId(), CountDetailStateEnum.COUNTED);
				if (!countDetailDao.getCountDetailStateByCountBillId(countDetail.getCountBillId())) {
					countHeaderDao.updateCountHeaderStateByCountBillId(countDetail.getCountBillId(), StockCountStateEnum.COUNT_COMPLETED);
				}
			} else {
				List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(generateCountReport.getBoxCode());
				for (Stock stocks : stockList) {
					CountDetail countDetail = countDetailDao.selectCountDetailByCode(generateCountReport.getLocCode(), generateCountReport.getBoxCode());
					CountRecord countRecord = countRecordFactory.createCountRecord(generateCountReport, stocks, countDetail);
					SkuUm um = skuBiz.findSkuUmByUmCode(stocks.getWsuCode());
					countRecord.setWsuName(um.getWsuName());
					countRecordDao.insert(countRecord);
					countDetailDao.updateCountDetailStateByCountDetailId(countDetail.getCountDetailId(), CountDetailStateEnum.COUNTED);
					if (!countDetailDao.getCountDetailStateByCountBillId(countDetail.getCountBillId())) {
						countHeaderDao.updateCountHeaderStateByCountBillId(countDetail.getCountBillId(), StockCountStateEnum.COUNT_COMPLETED);
					}
				}
			}


		});
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void generateCountReportByAutoLocation(List<AutoLocationBoxQty> beChangedList, List<AutoLocationBoxQty> defaultList) {
		for (int i = 0; i < beChangedList.size(); i++) {
			List<PdaBoxQtyResponse> stockCount = stockQueryBiz.getStockCountByLocCode(defaultList.get(i).getLocCode(), defaultList.get(i).getBoxCode(), defaultList.get(i).getSkuCode());
			AssertUtil.notNull(stockCount, "生成盘点单记录失败,根据箱码获取库存失败");

			CountDetail countDetail = countDetailDao.selectCountDetailByCode(defaultList.get(i).getLocCode(), defaultList.get(i).getBoxCode());
			CountRecord countRecord = countRecordFactory.createCountReport(stockCount.get(0), countDetail, beChangedList.get(i));
			countRecord.setLpnCode(beChangedList.get(i).getBoxCode());
			countRecord.setLocCode(beChangedList.get(i).getLocCode());
			Location location = locationBiz.findLocationByLocCode(stockCount.get(0).getWhId(), beChangedList.get(i).getLocCode());
			countRecord.setLocId(location.getLocId());
			SkuUm um = skuBiz.findSkuUmByUmCode(countRecord.getWsuName());
			countRecord.setWsuName(um.getWsuName());
			countRecordDao.insert(countRecord);
			countDetailDao.updateCountDetailStateByCountDetailId(countDetail.getCountDetailId(), CountDetailStateEnum.COUNTED);
			if (!countDetailDao.getCountDetailStateByCountBillId(countDetail.getCountBillId())) {
				countHeaderDao.updateCountHeaderStateByCountBillId(countDetail.getCountBillId(), StockCountStateEnum.COUNT_COMPLETED);
			}

		}
	}

	@Override
	public List<PdaStockCountDetailBySkuSpecResponse> findStockCountDetailBySkuSpec(PdaStockCountDetailBySkuSpecRequest request) {
		return countDetailDao.getStockCountDetailBySkuSpec(request);
	}
}
