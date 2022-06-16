
package org.nodes.wms.core.basedata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.dto.SkuUmDTO;
import org.nodes.wms.dao.basics.sku.dto.input.SkuUmAddOrEditRequest;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.core.basedata.excel.SkuUmExcel;
import org.nodes.wms.core.basedata.vo.SkuUmVO;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 服务类
 *
 * @author NodeX
 * @since 2019-12-05
 */
public interface ISkuUmService extends BaseService<SkuUm> {
	/**
	 * 模糊分页查询
	 *
	 * @param page  分页信息
	 * @param skuUm 查询条件
	 * @return 计量单位分页信息
	 */
	IPage<SkuUmVO> selectPage(IPage<SkuUm> page,SkuUm skuUm);

	/**
	 * 模糊列表查询
	 *
	 * @param skuUm 查询条件
	 * @return 计量单位列表信息
	 */
	List<SkuUmVO> selectList(SkuUm skuUm);
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
	List<DataVerify> validExcel(List<SkuUmExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);

	String convert(Long wspId, int skuLevel, BigDecimal qty);

	/**
	 * 计量单位新增或修改api
	 * @param skuUmAddOrEditRequest
	 * @return
	 */
	String addOrEdit(SkuUmAddOrEditRequest skuUmAddOrEditRequest);
}
