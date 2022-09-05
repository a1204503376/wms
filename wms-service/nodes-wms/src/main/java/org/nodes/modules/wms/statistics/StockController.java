package org.nodes.modules.wms.statistics;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.stock.core.dto.StockDTO;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.StockVO;
import org.nodes.wms.core.stock.core.wrapper.StockWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

/**
 * author: pengwei
 * date: 2021/5/19 10:25
 * description: StockController
 */
@RestController("statistics-stock")
@AllArgsConstructor
@RequestMapping("/wms/statistics/stock")
@Api(value = "库存统计", tags = "库存统计接口")
public class StockController extends BladeController {

	private IStockService stockService;

	/**
	 * 临期品预警分页
	 */
	@GetMapping("/near-time/page")
	@ApiOperation(value = "临期品预警分页", notes = "传入stockPack")
	public R<IPage<StockVO>> page(StockDTO stock, Query query) {
		QueryWrapper<Stock> queryWrapper = stockService.getQueryWrapper(stock);
		queryWrapper.isNotNull("valid_time");
		IPage<Stock> pages = stockService.page(Condition.getPage(query), queryWrapper);
		BigDecimal reduce = pages.getRecords().stream().map(Stock::getStockQty)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal reduce1 = pages.getRecords().stream().map(Stock::getPickQty)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal subtract = reduce.subtract(reduce1);
		IPage<StockVO> data = StockWrapper.build().pageVO(pages);
		data.getRecords().forEach(v -> {
			v.setTotalQty(Func.toStr(subtract));
			v.setStockQty(v.getStockQty().subtract(v.getPickQty()));
			LocalDate from = LocalDate.from(v.getLastInTime());
			Period period = Period.between(from, LocalDate.now());
			StringBuilder sb = new StringBuilder();
			if (period.getYears() > 0) {
				sb.append(period.getYears()).append("年");
			}
			if (period.getMonths() > 0) {
				sb.append(period.getMonths()).append("月");
			}
			if (period.getDays() >= 0) {
				sb.append(period.getDays()).append("天");
			}
			v.setKuLing(sb.toString());
		});
		data.getRecords().removeIf(new Predicate<StockVO>() {
			@Override
			public boolean test(StockVO stockVO) {
				String validTime = "";
				if (Func.isEmpty(validTime)) return true;
				Sku sku = SkuCache.getById(stockVO.getSkuId());
				if (!Func.isNull(sku)
					&& Func.isNotEmpty(sku.getQualityHours())
					) {
					int threshold = 0;
					int quality = Integer.valueOf(sku.getQualityHours());
					double i = quality * (Double.valueOf(threshold) / 100);
					Date validTimeDate = DateUtil.parse(validTime, DateUtil.PATTERN_DATE);
					String now = DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATE);
					Date parse = DateUtil.parse(now, DateUtil.PATTERN_DATE);
					if (i == 0L) {
						if (parse.compareTo(validTimeDate) < 0) {
							return true;
						}
						long l = (validTimeDate.getTime() - parse.getTime()) / (1000 * 3600 * 24);
						stockVO.setValidDay(String.valueOf(l));
						return false;
					}
					if (i < 1L) {
						i = 1L;
					}
					int j = (int) i;
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(validTimeDate);
					calendar.add(Calendar.DATE, (-1 * j));
					Date time = calendar.getTime();
					if (parse.compareTo(time) < 0) {
						return true;
					}
					long l = (validTimeDate.getTime() - parse.getTime()) / (1000 * 3600 * 24);
					stockVO.setValidDay(String.valueOf(l));
				}
				return false;
			}
		});
		return R.data(data);
	}
}
