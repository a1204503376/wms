package org.nodes.wms.biz.putaway.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.udf.UdfEntity;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.putaway.PutawayBiz;
import org.nodes.wms.biz.putaway.modular.PutawayFactory;
import org.nodes.wms.biz.putaway.strategy.TianYiPutawayStrategy;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.biz.task.AgvTask;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putaway.PutawayLogDao;
import org.nodes.wms.dao.putaway.dto.input.CallAgvRequest;
import org.nodes.wms.dao.putaway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.putaway.dto.input.PutawayPageQuery;
import org.nodes.wms.dao.putaway.dto.input.PutwayByBoxRequest;
import org.nodes.wms.dao.putaway.dto.output.BoxDto;
import org.nodes.wms.dao.putaway.dto.output.LocResponse;
import org.nodes.wms.dao.putaway.dto.output.PutawayLogExcelResponse;
import org.nodes.wms.dao.putaway.dto.output.PutawayLogResponse;
import org.nodes.wms.dao.putaway.entities.PutawayLog;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PutawayBizImpl implements PutawayBiz {
    private final LocationBiz locationBiz;
    private final StockBiz stockBiz;
    private final PutawayLogDao putawayLogDao;
    private final StockQueryBiz stockQueryBiz;
    private final PutawayFactory putawayFactory;
    private final AgvTask agvTask;
    private final TianYiPutawayStrategy tianYiPutawayStrategy;
    private final StockManageBiz stockManageBiz;

    @Override
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void putawayByBox(PutwayByBoxRequest request) {
        // ??????????????????????????????????????????????????????????????????????????????
        AssertUtil.notNull(request.getStockId(), "??????????????????,?????????????????????????????????????????????");
        Stock sourceStock = stockQueryBiz.findStockById(request.getStockId());
        AssertUtil.notNull(sourceStock, "??????????????????,?????????????????????");
        Location location = locationBiz.findLocationByLocCode(request.getWhId(), sourceStock.getLocCode());
        boolean stageLocation = locationBiz.isStageLocation(location);
        if (!stageLocation) {
            throw new ServiceException("??????????????????,???????????????????????????????????????");
        }

        // ??????????????????????????????
        if (request.getIsAllLpnPutaway()) {
            // ??????????????????????????????lpn?????????????????????????????????
            AssertUtil.notEmpty(sourceStock.getLpnCode(), "??????????????????,????????????lpn???????????????");
            List<Stock> stockList = stockQueryBiz.findStockByLpnCode(sourceStock.getLpnCode());
            AssertUtil.notNull(stockList, "??????????????????,??????????????????????????????????????????");
            List<Stock> sourceStockList = stockList.stream()
                    .filter(stockPrams -> Func.equals(stockPrams.getBoxCode(), request.getBoxCode()))
                    .collect(Collectors.toList());
            AssertUtil.notNull(sourceStockList, "??????????????????,?????????????????????????????????????????????????????????");
            Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
            boolean pickLocation = locationBiz.isPickLocation(targetLocation);
            if (!pickLocation) {
                throw new ServiceException("????????????????????????????????????????????????/??????????????????");
            }
            UdfEntity udf = locationBiz.judgeBoxTypeOfC(request.getBoxCode(), targetLocation);
            Location sourceLocation = locationBiz.findLocationByLocCode(sourceStockList.get(0).getWhId(), sourceStockList.get(0).getLocCode());
            stockManageBiz.canMoveToBoxType(targetLocation, sourceLocation);
			stockManageBiz.canMoveToLocAuto(targetLocation);
			stockManageBiz.canMoveToBoxType(targetLocation, request.getBoxCode());
            if (locationBiz.isAgvLocation(targetLocation)) {
                //AGV??????????????????
                agvTask.moveStockToSchedule(sourceStockList, targetLocation);
                return;
            }
            stockBiz.moveStockByLpnCode(sourceStockList.get(0).getLpnCode(), sourceStockList.get(0).getLpnCode(),
                    targetLocation, StockLogTypeEnum.INSTOCK_BY_PUTAWAY_PDA, null, null, null, udf);
            // ??????????????????
            sourceStockList.forEach(stock -> {
                PutawayLog putawayLog = putawayFactory.create(request, stock, targetLocation);
                putawayLogDao.save(putawayLog);
            });
            return;
        }

        // ?????????????????????????????????
        Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
        boolean pickLocation = locationBiz.isPickLocation(targetLocation);
        if (!pickLocation) {
            throw new ServiceException("????????????????????????????????????????????????/??????????????????");
        }
        List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
        AssertUtil.notNull(stockList, "?????????????????????????????????????????????????????????");
        Location sourceLocation = locationBiz.findLocationByLocCode(stockList.get(0).getWhId(), stockList.get(0).getLocCode());
        stockManageBiz.canMove(sourceLocation, targetLocation, stockList, request.getBoxCode());
        if (locationBiz.isAgvLocation(targetLocation)) {
            //AGV??????????????????
            agvTask.moveStockToSchedule(stockList, targetLocation);
            return;
        }
        stockBiz.moveStockByBoxCode(request.getBoxCode(), request.getBoxCode(), sourceStock.getLpnCode(),
                targetLocation, StockLogTypeEnum.INSTOCK_BY_PUTAWAY_PDA, null, null, null, null);
        // ??????????????????
        PutawayLog putawayLog = putawayFactory.create(request, sourceStock, targetLocation);
        putawayLogDao.save(putawayLog);
    }


    @Override
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void callAgv(CallAgvRequest request) {
        //????????????????????????
        Location targetLocation = locationBiz.findByLocId(request.getLocId());
        UdfEntity udf = locationBiz.judgeBoxTypeOfC(request.getBoxList().get(0).getBoxCode(), targetLocation);
        List<BoxDto> boxDtoList = request.getBoxList();
        List<Stock> targetStockList = new ArrayList<>();
        for (BoxDto boxDto : boxDtoList) {
            List<Long> stockIdList = boxDto.getStockIdList();
            for (Long stockId : stockIdList) {
                // ??????id??????????????????
                Stock stock = stockQueryBiz.findStockById(stockId);
                //????????????
                BigDecimal qty = stock.getStockBalance();
                //???????????????
                List<Serial> serialList = stockQueryBiz.findSerialByStock(stockId);
                List<String> serialNoList = new ArrayList<>();
                if (Func.isNotEmpty(serialList)) {
                    serialNoList = serialList.stream()
                            .map(Serial::getSerialNumber)
                            .collect(Collectors.toList());
                }
                // ??????????????????
                Stock targetStock = stockBiz.moveStock(stock, serialNoList, qty, targetLocation,
                        StockLogTypeEnum.STOCK_TO_INSTOCK_RECE, null, null, null, udf);
                targetStockList.add(targetStock);
            }
        }
        agvTask.putawayToSchedule(targetStockList);
    }

    @Override
    public List<LocResponse> findLocByLpnType(LpnTypeRequest request) {
        List<LocResponse> locResponseList = new ArrayList<>();
        List<Location> locationList = new ArrayList<>();
        if (request.getLocCode().substring(0, 5).equals("WH1-R")) {
            Location location = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
            locationList.add(location);
        } else {
            // ?????????????????????id??????????????????
            locationList = locationBiz.findLocationByLpnType(request);
        }

        for (Location location : locationList) {
            LocResponse locResponse = new LocResponse();
            locResponse.setLocId(location.getLocId());
            String locCode = location.getLocCode();
            locResponse.setLocCode(locCode);
            //????????????????????????????????? ?????????????????????-??????????????????????????????????????????????????????
            locResponse.setLocCodeView(locCode.substring(9));
            if (locCode.substring(9, 11).equals("34")
                    || locCode.substring(9, 11).equals("33")) {
                locResponse.setIsCBifurcate(1);
            } else {
                locResponse.setIsCBifurcate(2);
            }
            // ??????????????????
            if (request.getLocCode().substring(0, 5).equals("WH1-R")) {
                locResponse.setIsEmpty(true);
            } else {
                locResponse.setIsEmpty(stockBiz.judgeEnableOnLocation(location));
            }

            locResponseList.add(locResponse);
        }
        return locResponseList;

    }

    @Override
    public IPage<PutawayLogResponse> getPutawayLogPage(Query query, PutawayPageQuery putawayPageQuery) {
        IPage<PutawayLog> page = putawayLogDao.getPage(Condition.getPage(query), putawayPageQuery);
        return page.convert(log ->
        {
            return BeanUtil.copy(log, PutawayLogResponse.class);
        });
    }

    @Override
    public void exportPutawayLog(PutawayPageQuery queryParam, HttpServletResponse response) {
        List<PutawayLogExcelResponse> putawayLogList = putawayLogDao.getListForExport(queryParam);
        ExcelUtil.export(response,"", "", putawayLogList, PutawayLogExcelResponse.class);
    }
}
