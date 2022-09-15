package org.nodes.wms.dao.instock.receive;

import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;

import java.util.List;

/**
 * lpn表Dao接口
 */
public interface ReceiveDetailLpnDao {

	/**
	 * 根据箱码获取lpn实体集合
	 *
	 * @param boxCode 箱码
	 */
	List<ReceiveDetailLpn> getReceiveDetailLpnListByBoxCode(String boxCode);

	/**
	 * 根据id获取lpn实体
	 * @param receiveDetailLpnId id
	 * @return lpn实体
	 */
    ReceiveDetailLpn selectReceiveDetailLpnById(Long receiveDetailLpnId);

	/**
	 * 修改lpn
 	 * @param lpn lpn实体
	 */
	void updateReceiveDetailLpn(ReceiveDetailLpn lpn);

	/**
	 * 根据收货明细id查询收货单LPN明细
	 *
	 * @param receiveDetailId 收货明细id
	 * @return ReceiveDetailLpn 收货单LPN明细实体
	 */
    ReceiveDetailLpn selectByReceiveDetailId(Long receiveDetailId);

	/**
	 * 撤销收货时更新receive_detail_lpn
	 *
	 * @param receiveDetailLpn 收货单LPN明细
	 * @return true：更新成功，false: 更新失败
	 */
	boolean updateForCancelReceive(ReceiveDetailLpn receiveDetailLpn);
}
