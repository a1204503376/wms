package org.nodes.wms.core.warehouse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.warehouse.dto.WarehouseDTO;
import org.nodes.wms.core.warehouse.excel.WarehouseExcel;
import org.nodes.wms.core.warehouse.vo.WarehouseVO;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.base.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 仓库 服务类
 *
 * @author Wangjw
 * @since 2019-12-06
 */
public interface IWarehouseService extends BaseService<Warehouse> {
	/**
	 * 新增仓库
	 *
	 * @param whDTO WarehouseDTO 对象
	 * @return 是否成功
	 */
	boolean save(WarehouseDTO whDTO);

	/**
	 * 修改仓库
	 *
	 * @param whDTO WarehouseDTO 对象
	 * @return 是否成功
	 */
	boolean updateById(WarehouseDTO whDTO);

	/**
	 * 新增/修改 库房
	 *
	 * @param warehouseDTO 库房信息
	 * @return 是否成功
	 */
	boolean saveOrUpdate(WarehouseDTO warehouseDTO);

	/**
	 * 保存上传的数据
	 *
	 * @param list   验证结果集合
	 * @param userId 用户ID
	 * @return 是否成功
	 */
	boolean uploadSave(List<DataVerify> list, Long userId);

	/**
	 * 模糊列表查询
	 *
	 * @param warehouse 查询条件
	 * @return 仓库列表信息
	 */
	List<WarehouseVO> selectList(Warehouse warehouse);

	/**
	 * 模糊分页查询
	 *
	 * @param page      分页信息
	 * @param warehouse 查询条件
	 * @return 仓库分页信息
	 */
	IPage<WarehouseVO> selectPage(IPage<Warehouse> page, Warehouse warehouse);


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
	List<DataVerify> validExcel(List<WarehouseExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);

	/**
	 * 重写removeByIds
	 */
	boolean removeByIds(List<Long> ids);

}
