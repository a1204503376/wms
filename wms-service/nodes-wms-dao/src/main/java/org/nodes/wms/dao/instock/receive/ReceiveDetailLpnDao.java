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
}
