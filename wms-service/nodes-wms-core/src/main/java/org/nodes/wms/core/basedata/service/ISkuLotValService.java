
package org.nodes.wms.core.basedata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.dto.SkuLotValDTO;
import org.nodes.wms.core.basedata.entity.SkuLotVal;
import org.nodes.wms.core.basedata.excel.SkuLotValExcel;
import org.nodes.wms.core.basedata.vo.SkuLotValVO;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 批属性验证 服务类
 *
 * @author chenhz
 * @since 2019-12-18
 */
public interface ISkuLotValService extends BaseService<SkuLotVal> {

	/**
	 * 按条件查询批属性验证
	 *
	 * @param skuLotVal 查询条件
	 * @return 批属性验证-列表
	 */
	List<SkuLotValVO> selectList(SkuLotVal skuLotVal);

	/**
	 * 按条件查询批属性验证
	 *
	 * @param page 分页信息
	 * @param skuLotVal 查询条件
	 * @return 批属性验证-分页
	 */
	IPage<SkuLotValVO> selectPage(IPage<SkuLotVal> page, SkuLotVal skuLotVal);
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
	List<DataVerify> validExcel(List<SkuLotValExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);

	/**
	 * 重写save方法，并做名称是否存在的验证
	 * @param skuLotVal
	 */
	boolean saveOrUpdate(SkuLotVal skuLotVal);
}
