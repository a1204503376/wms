
package org.nodes.wms.core.stock.core.wrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.classgraph.json.Id;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.vo.StockOccupyVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;

/**
 * 库存占用表包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-02-17
 */
public class StockOccupyWrapper extends BaseEntityWrapper<StockOccupy, StockOccupyVO> {

	public static StockOccupyWrapper build() {
		return new StockOccupyWrapper();
	}

	@Override
	public StockOccupyVO entityVO(StockOccupy stockOccupy) {
		StockOccupyVO stockOccupyVO = BeanUtil.copy(stockOccupy, StockOccupyVO.class);
		if (ObjectUtil.isNotEmpty(stockOccupyVO)) {
			//占用类型名称
			stockOccupyVO.setOccupyTypeName(DictCache.getValue("occupy_type", stockOccupyVO.getOccupyType()));
			//库房信息
			Warehouse warehouse = WarehouseCache.getById(stockOccupyVO.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				stockOccupyVO.setWhName(warehouse.getWhName());
			}
		}

		return stockOccupyVO;
	}

	/**
	 * Page转VOPage
	 *
	 * @param page
	 * @return
	 */
	public static IPage<StockOccupyVO> toStockOccupyVOPages(IPage<StockOccupy> page) {
		return page.convert(stockOccupy -> {
			StockOccupyVO stockOccupyVO = StockOccupyWrapper.build().entityVO(stockOccupy);
			return StockOccupyWrapper.translateStockOccupyVO(stockOccupyVO);
		});
	}

	/**
	 * 编码转文字
	 *
	 * @param serialVO
	 * @return
	 */
	public static StockOccupyVO translateStockOccupyVO(StockOccupyVO stockOccupyVO) {

		//占用类型名称
		stockOccupyVO.setOccupyTypeName(DictCache.getValue("occupy_type", stockOccupyVO.getOccupyType()));
		//库房信息
		Warehouse warehouse = WarehouseCache.getById(stockOccupyVO.getWhId());
		if (Func.isNotEmpty(warehouse)) {
			stockOccupyVO.setWhName(warehouse.getWhName());
		}

		return stockOccupyVO;
	}
}
