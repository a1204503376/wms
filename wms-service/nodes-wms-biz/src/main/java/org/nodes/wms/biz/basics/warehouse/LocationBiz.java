package org.nodes.wms.biz.basics.warehouse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import org.nodes.wms.dao.basics.location.dto.input.LocationAddOrEditRequest;
import org.nodes.wms.dao.basics.location.dto.input.LocationExcelRequest;
import org.nodes.wms.dao.basics.location.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.location.dto.input.LocationSelectQuery;
import org.nodes.wms.dao.basics.location.dto.output.LocationDetailResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationEditResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationPageResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客户管理业务层接口
 */
public interface LocationBiz {
	/**
	 * 获取库位下拉列表最近十条数据
	 *
	 * @param locationSelectQuery 前端传传入查询条件
	 * @return 库位下拉列表集合
	 */
	List<LocationSelectResponse> getLocationSelectResponseTop10List(LocationSelectQuery locationSelectQuery);

	/**
	 * 导入
	 *
	 * @param locationDataList: 导入数据集合
	 * @return true: 导入成功，false：导入失败
	 */
	boolean importData(List<LocationExcelRequest> locationDataList);

	/**
	 * 分页
	 *
	 * @param query              页参数
	 * @param locationPageQuery: 分页查询条件
	 * @return IPage<LocationPageResponse>
	 */
	Page<LocationPageResponse> page(Query query, LocationPageQuery locationPageQuery);

	/**
	 * 导出
	 *
	 * @param locationPageQuery: 条件
	 * @param response:          响应信息
	 */
	void exportExcel(LocationPageQuery locationPageQuery, HttpServletResponse response);

	/**
	 * 新增库位
	 *
	 * @param locationAddOrEditRequest: 新增请求对象
	 * @return Location
	 */
	Location add(LocationAddOrEditRequest locationAddOrEditRequest);

	/**
	 * 根据id查找库位信息
	 *
	 * @param locId: 库位id
	 * @return LocationPageResponse
	 */
	LocationEditResponse findLocationById(Long locId);

	/**
	 * 编辑
	 *
	 * @param locationAddOrEditRequest: 编辑请求对象
	 * @return Location
	 */
	Location edit(LocationAddOrEditRequest locationAddOrEditRequest);

	/**
	 * 查看详情
	 *
	 * @param locId: 库位id
	 * @return LocationDetailResponse
	 */
	LocationDetailResponse getLocationDetailById(Long locId);

	/**
	 * 批量删除
	 *
	 * @param idList: 库位id集合
	 * @return true: 删除成功, false: 删除失败
	 */
	boolean remove(List<Long> idList);

	/**
	 * 返回所有的库房的入库集货区
	 *
	 * @return List<Location>
	 */
	List<Location> getAllStage();

	/**
	 * 返回所有的库房的入库检验区
	 *
	 * @return List<Location>
	 */
	List<Location> getAllQc();

	/**
	 * 返回所有的库房的出库暂存区
	 *
	 * @return List<Location>
	 */
	List<Location> getAllPickTo();

	/**
	 * 获取库位总数量
	 *
	 * @return int
	 */
	int countAll();

	/**
	 * 根据库位编码获取库位
	 * @param locCode 库位编码
	 * @return 库位信息
	 */
	Location findLocationByLocCode(String locCode);

}
