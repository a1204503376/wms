package org.nodes.wms.core.strategy.factory.outstock;

import lombok.Getter;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.entity.OutstockDetail;
import org.nodes.wms.core.strategy.factory.IFunctionCode;
import org.springblade.core.tool.utils.Func;
import org.springframework.util.comparator.Comparators;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * @program: WmsCore
 * @description: 执行代码基类
 * @author: pengwei
 * @create: 2020-12-16 14:16
 **/
@Getter
public class BaseFunction implements IFunctionCode {

	OutstockDetail outstockDetail;

	SkuOutstock skuOutstock;

	@Override
	public List<Stock> execute(List<Stock> stockListAll, BigDecimal planQty) {
		return stockListAll;
	}

	@Override
	public void setOutstockDetail(OutstockDetail outstockDetail) {
		this.outstockDetail = outstockDetail;
	}

	@Override
	public void setSkuOutstock(SkuOutstock skuOutstock) {
		this.skuOutstock = skuOutstock;
	}

	/**
	 * 根据物品出库设置排序
	 *
	 * @param stockList 库存列表
	 */
	protected Comparator<Stock> sort(Comparator<Stock> comparator, List<Stock> stockList) {
		if (Func.isEmpty(comparator)) {
			comparator = Comparators.comparable();
		}
		if (Func.isNotEmpty(this.skuOutstock.getTurnoverItem1())) {
			this.turnoverTypeSort(comparator, this.skuOutstock.getTurnoverType1(),
				this.skuOutstock.getTurnoverItem1());
		}
		if (Func.isNotEmpty(this.skuOutstock.getTurnoverItem2())) {
			this.turnoverTypeSort(comparator, this.skuOutstock.getTurnoverType2(),
				this.skuOutstock.getTurnoverItem2());
		}
		if (Func.isNotEmpty(skuOutstock.getTurnoverItem3())) {
			this.turnoverTypeSort(comparator, this.skuOutstock.getTurnoverType3(),
				this.skuOutstock.getTurnoverItem3());
		}
		return comparator;
	}

	protected String turnoverTypeSort(Comparator<Stock> comparator, int turnoverType, int turnoverItem) {
		switch (turnoverType) {
			case 1:
				return this.turnoverItemSort(comparator, turnoverItem, Comparator.naturalOrder());
			case 2:
				return this.turnoverItemSort(comparator, turnoverItem, Comparator.reverseOrder());
			default:
				return "";
		}
	}

	protected String turnoverItemSort(Comparator<Stock> comparator, int turnoverItem, Comparator order) {
		String orderStr = order.equals(Comparator.reverseOrder()) ? " desc" : "asc";
		if (turnoverItem == 0) {
			comparator.thenComparing(Stock::getLotNumber, order);
			return "lot_number " + orderStr;
		} else {
			switch (turnoverItem) {
				case 1:
					comparator.thenComparing(Stock::getSkuLot1, order);
					return "sku_lot1" + orderStr;
				case 2:
					comparator.thenComparing(Stock::getSkuLot2, order);
					return "sku_lot2" + orderStr;
				case 3:
					comparator.thenComparing(Stock::getSkuLot3, order);
					return "sku_lot3" + orderStr;
				case 4:
					comparator.thenComparing(Stock::getSkuLot4, order);
					return "sku_lot4" + orderStr;
				case 5:
					comparator.thenComparing(Stock::getSkuLot5, order);
					return "sku_lot5" + orderStr;
				case 6:
					comparator.thenComparing(Stock::getSkuLot6, order);
					return "sku_lot6" + orderStr;
				case 7:
					comparator.thenComparing(Stock::getSkuLot7, order);
					return "sku_lot7" + orderStr;
				case 8:
					comparator.thenComparing(Stock::getSkuLot8, order);
					return "sku_lot8" + orderStr;
				case 9:
					comparator.thenComparing(Stock::getSkuLot9, order);
					return "sku_lot9" + orderStr;
				case 10:
					comparator.thenComparing(Stock::getSkuLot10, order);
					return "sku_lot10" + orderStr;
				case 11:
					comparator.thenComparing(Stock::getSkuLot11, order);
					return "sku_lot11" + orderStr;
				case 12:
					comparator.thenComparing(Stock::getSkuLot12, order);
					return "sku_lot12" + orderStr;
				case 13:
					comparator.thenComparing(Stock::getSkuLot13, order);
					return "sku_lot13" + orderStr;
				case 14:
					comparator.thenComparing(Stock::getSkuLot14, order);
					return "sku_lot14" + orderStr;
				case 15:
					comparator.thenComparing(Stock::getSkuLot15, order);
					return "sku_lot15" + orderStr;
				case 16:
					comparator.thenComparing(Stock::getSkuLot16, order);
					return "sku_lot16" + orderStr;
				case 17:
					comparator.thenComparing(Stock::getSkuLot17, order);
					return "sku_lot17" + orderStr;
				case 18:
					comparator.thenComparing(Stock::getSkuLot18, order);
					return "sku_lot18" + orderStr;
				case 19:
					comparator.thenComparing(Stock::getSkuLot19, order);
					return "sku_lot19" + orderStr;
				case 20:
					comparator.thenComparing(Stock::getSkuLot20, order);
					return "sku_lot20" + orderStr;
				case 21:
					comparator.thenComparing(Stock::getSkuLot21, order);
					return "sku_lot21" + orderStr;
				case 22:
					comparator.thenComparing(Stock::getSkuLot22, order);
					return "sku_lot22" + orderStr;
				case 23:
					comparator.thenComparing(Stock::getSkuLot23, order);
					return "sku_lot23" + orderStr;
				case 24:
					comparator.thenComparing(Stock::getSkuLot24, order);
					return "sku_lot24" + orderStr;
				case 25:
					comparator.thenComparing(Stock::getSkuLot25, order);
					return "sku_lot25" + orderStr;
				case 26:
					comparator.thenComparing(Stock::getSkuLot26, order);
					return "sku_lot26" + orderStr;
				case 27:
					comparator.thenComparing(Stock::getSkuLot27, order);
					return "sku_lot27" + orderStr;
				case 28:
					comparator.thenComparing(Stock::getSkuLot28, order);
					return "sku_lot28" + orderStr;
				case 29:
					comparator.thenComparing(Stock::getSkuLot29, order);
					return "sku_lot29" + orderStr;
				case 30:
					comparator.thenComparing(Stock::getSkuLot30, order);
					return "sku_lot30" + orderStr;
				default:
					return "";
			}
		}
	}

	protected String getTurnoverItemValue(Stock stock) {

		String fieldName1 = this.getTurnoverItemField(this.skuOutstock.getTurnoverItem1());
		String fieldName2 = this.getTurnoverItemField(this.skuOutstock.getTurnoverItem2());
		String fieldName3 = this.getTurnoverItemField(this.skuOutstock.getTurnoverItem3());

		Object obj1 = NodesUtil.getFieldValue(stock, fieldName1);
		Object obj2 = NodesUtil.getFieldValue(stock, fieldName2);
		Object obj3 = NodesUtil.getFieldValue(stock, fieldName3);

		return String.format("%s %s %s", Func.toStr(obj1), Func.toStr(obj2), Func.toStr(obj3));
	}

	private String getTurnoverItemField(Integer turnoverItem) {
		if (Func.isEmpty(turnoverItem)) {
			return StringPool.EMPTY;
		}
		return turnoverItem == 0 ? "lotNumber" : ("skuLot" + turnoverItem);
	}
}
