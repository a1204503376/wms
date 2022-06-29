package org.nodes.wms.dao.stock;

import org.nodes.wms.dao.stock.entities.Serial;

import java.util.List;

/**
 * 库存序列号Dao接口
 **/
public interface SerialDao {

	/**
	 * 根据序列号编码获取在库的序列号信息
	 *
	 * @param serialNoList 序列号编码
	 * @return 在库的序列号
	 */
	List<Serial>  getSerialBySerialNo(List<String> serialNoList);
}
