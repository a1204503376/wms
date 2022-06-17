
package org.nodes.wms.core.basedata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.dto.SkuPackageDTO;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.core.basedata.excel.SkuPackageExcel;
import org.springblade.core.mp.base.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 包装ID 服务类
 *
 * @author NodeX
 * @since 2019-12-10
 */
public interface ISkuPackageService extends BaseService<SkuPackage> {
	/**
	 * 分页查询
	 *
	 * @param page       分页信息
	 * @param skuPackage 查询条件
	 * @return 分页数据
	 */
	IPage<SkuPackage> selectPage(IPage<SkuPackage> page, SkuPackageDTO skuPackage);

	/**
	 * 列表查询
	 *
	 * @param skuPackage 分页信息
	 * @return 列表数据
	 */
	List<SkuPackage> selectList(SkuPackageDTO skuPackage);

	/**
	 * 根据id修改包装信息
	 *
	 * @param entity 包装信息
	 * @return 是否成功
	 */
	boolean updateById(SkuPackageDTO entity);

	/**
	 * 保存包装信息
	 *
	 * @param entity 包装信息
	 * @return 是否成功
	 */
	boolean save(SkuPackageDTO entity);

	/**
	 * 保存或者更新包装信息
	 *
	 * @param entity 包装信息
	 * @return 是否成功
	 */
	boolean saveOrUpdate(SkuPackageDTO entity);

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
	List<DataVerify> validExcel(List<SkuPackageExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);

	/**
	 * 删除包装类型
	 * @param idList
	 * @return 是否删除成功
	 */
	boolean removeByIds(Collection<? extends Serializable> idList);
}
