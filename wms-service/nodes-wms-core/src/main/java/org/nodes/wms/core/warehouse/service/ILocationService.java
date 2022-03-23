
package org.nodes.wms.core.warehouse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.warehouse.dto.LocationDTO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.excel.LocationExcel;
import org.nodes.wms.core.warehouse.vo.LocationVO;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 服务类
 *
 * @author NodeX
 * @since 2019-12-11
 */
public interface ILocationService extends BaseService<Location> {

	/**
	 * 打印库位标签
	 *
	 * @param idList 库位id集合
	 * @return 是否成功
	 */
	boolean print(List<Long> idList);

	/**
	 * 查询库位列表
	 *
	 * @param location 查询条件实体类
	 * @return 库位集合
	 */
	List<LocationVO> selectList(Location location);

	/**
	 * 更新库位绑定的盘点单号
	 *
	 * @param locId       库位ID
	 * @param countBillNo 盘点单号
	 * @return 是否成功
	 */
	boolean updateCountBillNo(Long locId, String countBillNo);

	/**
	 * 更新库位绑定的盘点单号
	 *
	 * @param locIdList   库位ID集合
	 * @param countBillNo 盘点单号
	 * @return 是否成功
	 */
	boolean updateCountBillNo(Collection<Long> locIdList, String countBillNo);

	/**
	 * 根据id锁定库位
	 *
	 * @param locIds
	 * @param lockFlag 锁定状态
	 * @return
	 */
	boolean lockById(String locIds, String lockFlag);

	/**
	 * 模糊分页查询
	 *
	 * @param page  分页信息
	 * @param location 查询条件
	 * @return 库位分页信息
	 */
	IPage<LocationVO> selectPage(IPage<Location> page, Location location);


	/**
	 * Excel 导出(导出当前查询条件)
	 *
	 * @param params 查询条件
	 * @param response
	 */
	void exportExcel(HashMap<String, Object> params, HttpServletResponse response);

	/**
	 * 验证Excel数据
	 *
	 * @param dataList 待验证的数据
	 */
	List<DataVerify> validExcel(List<LocationExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);
}
