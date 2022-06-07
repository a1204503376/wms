package org.nodes.wms.dao.basics.warehouse;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationPageResponse;
import org.nodes.wms.dao.basics.warehouse.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.warehouse.entites.Location;

import java.util.List;

/**
 * 库位管理 Dao接口
 */
public interface LocationDao {
	List<LocationSelectResponse> listTop10ByCode(String code);

	/**
	 * 导入
	 *
	 * @param locationList: 库位数据集合
	 * @return true: 导入成功，false: 导入失败
	 */
    boolean importData(List<Location> locationList);

	/**
	 * 根据库位编码
	 *
	 * @param locCode:
	 * @return true: 存在 ，false: 不存在
	 */
	boolean isExistLocationCode(String locCode);

	/**
	 * 分页
	 *
	 * @param page               分页参数
	 * @param locationPageQuery: 分页查询条件
	 * @return Page<LocationPageResponse>
	 */
	Page<LocationPageResponse> selectPage(IPage<?> page, LocationPageQuery locationPageQuery);
}
