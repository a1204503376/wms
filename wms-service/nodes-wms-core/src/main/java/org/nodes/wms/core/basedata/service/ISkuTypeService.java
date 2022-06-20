package org.nodes.wms.core.basedata.service;

import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.dto.SkuTypeDTO;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.core.basedata.excel.SkuTypeExcel;
import org.nodes.wms.core.basedata.vo.SkuTypeVO;
import org.springblade.core.mp.base.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author wanglei
 * @program 物品分类服务接口
 * @description 物品分类服务接口
 * @create 20191128
 */
public interface ISkuTypeService extends BaseService<SkuType> {

	/**
	 * 树形结构
	 *
	 * @param skuType 查询条件
	 * @return 树形列表
	 */
	List<SkuTypeVO> tree(SkuType skuType);
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
	List<DataVerify> validExcel(List<SkuTypeExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);
	/**
	 * 修改物品分类
	 *
	 * @return
	 */
	boolean updateById(SkuTypeDTO skuTypeVO);

	/**
	 * 增加
	 *
	 * @return SkuTypeVO
	 */
	boolean save(SkuTypeDTO skuTypeVO);

	/**
	 * 新增或修改物品分类
	 *
	 * @param skuType
	 * @return
	 */
	boolean saveOrUpdate(SkuTypeDTO skuType);
}
