package org.nodes.wms.biz.count;

import org.nodes.wms.dao.count.dto.input.StockCountRequest;
import org.nodes.wms.dao.count.dto.output.PdaBoxSkuQtyResponse;
import org.nodes.wms.dao.count.dto.output.PdaStockCountDetailResponse;
import org.nodes.wms.dao.count.dto.output.PdaStockCountResponse;

import java.util.List;

/**
 * 盘点 业务类
 */
public interface StockCountBiz {

	/**
	 * 编辑盘点单保存
	 */
	void save(StockCountRequest stockCountRequest);

	/**
	 * 根据盘点单号查询返回给PDA使用的盘点单集合
	 */
	List<PdaStockCountResponse> getPdaStockCountResponseList(String countBillNo);

	/**
	 * 根据盘点单ID查询返回给PDA使用的盘点单明细集合
	 */
	List<PdaStockCountDetailResponse> getPdaStockCountDetailResponseList(Long countBillId);

	/**
	 *根据箱号和库位ID查询返回给PDA使用的箱内物品数量对象集合
	 */
	List<PdaBoxSkuQtyResponse> getPdaBoxSkuQtyResponseList(String boxCode,String locId);

}
