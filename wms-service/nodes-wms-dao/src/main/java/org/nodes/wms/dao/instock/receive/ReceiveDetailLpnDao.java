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
}
