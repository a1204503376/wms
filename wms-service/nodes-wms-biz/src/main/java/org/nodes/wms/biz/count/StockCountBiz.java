package org.nodes.wms.biz.count;

import org.nodes.wms.dao.count.dto.input.AutoLocationBoxQty;
import org.nodes.wms.dao.count.dto.input.GenerateCountReport;
import org.nodes.wms.dao.count.dto.input.PdaStockCountDetailBySkuSpecRequest;
import org.nodes.wms.dao.count.dto.input.StockCountRequest;
import org.nodes.wms.dao.count.dto.output.*;

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
	 * 根据箱号和库位ID查询返回给PDA使用的箱内物品数量对象集合
	 */
	List<PdaBoxSkuQtyResponse> getPdaBoxSkuQtyResponseList(String boxCode, String locId);

	/**
	 * 根据箱号询返回给PDA使用的箱内物品数量对象集合
	 */
	List<PdaSkuQtyResponse> getPdaSkuQtyResponseList(String boxCode);

	/**
	 * 根据用户手动所提交的数据 生成盘点记录 /人工区
	 *
	 * @param countReportList 用户手动所提交的数据集合
	 */
	void generateCountReport(List<GenerateCountReport> countReportList);

	/**
	 * 据用户手动所提交的数据  生成盘点记录  /自动区
	 *
	 * @param beChangedList 用户改变后所提交的数据集合
	 * @param defaultList   用户未改变的数据集合
	 */
	void generateCountReportByAutoLocation(List<AutoLocationBoxQty> beChangedList, List<AutoLocationBoxQty> defaultList);

	/**
	 * 根据规格型号查询能创建盘点单的明细
	 *
	 * @param request 包含规格型号
	 * @return 能创建盘点单的明细
	 */
	List<PdaStockCountDetailBySkuSpecResponse> findStockCountDetailBySkuSpec(PdaStockCountDetailBySkuSpecRequest request);

}
