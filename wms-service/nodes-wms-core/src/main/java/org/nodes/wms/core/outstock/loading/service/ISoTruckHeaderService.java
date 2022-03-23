
package org.nodes.wms.core.outstock.loading.service;

import org.nodes.wms.core.outstock.loading.entity.SoTruckDetail;
import org.nodes.wms.core.outstock.loading.entity.SoTruckHeader;
import org.nodes.wms.core.outstock.loading.vo.SoTruckDetailPdaVO;
import org.nodes.wms.core.outstock.loading.vo.SoTruckHeaderPdaVO;
import org.nodes.wms.core.outstock.loading.vo.SoTruckHeaderVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 车次头表 服务类
 *
 * @author chz
 * @since 2020-03-03
 */
public interface ISoTruckHeaderService extends BaseService<SoTruckHeader> {

	/**
	 * 确认发货
	 *
	 * @param truckId
	 * @return
	 */
	Boolean confirmSo(String truckId);


	Boolean SaveconfirmSo(String truckId);

	/**
	 * 根据车次编号获取车次信息
	 *
	 * @param truckCode
	 * @return
	 */
	SoTruckHeaderPdaVO getTruckHeader(String truckCode, String driverName, String driverPhone, String plateNumber, String truckHeaderWaybill);


	/**
	 * 根据LPN编码获取概要信息
	 *
	 * @param LpnCode
	 * @return
	 */
	SoTruckDetailPdaVO getSkuStock(String LpnCode);

	/**
	 * 根据LPN编码获取物品库存详细信息列表
	 *
	 * @param LpnCode
	 * @return
	 */
	SoTruckHeaderPdaVO getSkuStockDetailList(String LpnCode);

	/**
	 * 保存装车明细信息
	 *
	 * @param soTruckDetailList
	 * @return
	 */
	Boolean saveSoTruckDetail(List<SoTruckDetail> soTruckDetailList);

	/**
	 * 获取装车详细信息
	 *
	 * @param soTruckHeader 装车查询条件
	 * @return 带装车明细的装车信息
	 */
	SoTruckHeaderVO getDetail(SoTruckHeader soTruckHeader);

}
