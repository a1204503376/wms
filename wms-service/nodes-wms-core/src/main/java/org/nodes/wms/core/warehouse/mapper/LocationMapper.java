
package org.nodes.wms.core.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.warehouse.entity.Location;
import org.springframework.context.annotation.Primary;

/**
 *  Mapper 接口
 *
 * @author NodeX
 * @since 2019-12-11
 */
@Primary
public interface LocationMapper extends BaseMapper<Location> {

	/**
	 * 根据库位ID更新盘点单编号信息
	 * @param location 对象
	 * @return 是否成功
	 */
	boolean updateLoctionBillNo(Location location);


	/**
	 * 根据盘点单号更新盘点单编号信息
	 * @param countBillNo 对象
	 * @return 是否成功
	 */
	boolean deletLoctionBillNo(String countBillNo);

	/**
	 * 清空锁定状态
	 * @return
	 */
	boolean updateLocLockFlagToNullById(String locId);

}
