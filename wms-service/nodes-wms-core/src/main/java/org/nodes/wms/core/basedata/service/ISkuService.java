
package org.nodes.wms.core.basedata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.dto.SkuDTO;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.excel.SkuExcel;
import org.nodes.wms.core.basedata.vo.SkuVO;
import org.springblade.core.mp.base.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 物品 服务类
 *
 * @author pengwei
 * @since 2019-12-09
 */
public interface ISkuService extends BaseService<Sku> {
	/**
	 * 分页查询
	 *
	 * @param page 分页信息
	 * @param sku  查询条件
	 * @return 物品分页
	 */
	IPage<Sku> selectPage(IPage<Sku> page, SkuDTO sku);

	/**
	 * 列表查询
	 *
	 * @param sku 查询条件
	 * @return 物品列表
	 */
	List<Sku> selectList(SkuDTO sku);

	/**
	 * 获取物品详细信息
	 *
	 * @param sku 查询条件
	 * @return 物品详细信息
	 */
	SkuVO getDetail(SkuDTO sku);

	boolean save(SkuDTO entity);

	boolean updateById(SkuDTO entity);

	boolean saveOrUpdate(SkuDTO entity);
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
	List<DataVerify> validExcel(List<SkuExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);


}
