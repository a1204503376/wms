
package org.nodes.wms.core.basedata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.dto.SkuLotDTO;
import org.nodes.wms.core.basedata.entity.SkuLot;
import org.nodes.wms.core.basedata.excel.SkuLotExcel;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.basedata.vo.SkuLotVO;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 物品批属性 服务类
 *
 * @author chenhz
 * @since 2019-12-10
 */
public interface ISkuLotService extends BaseService<SkuLot> {
	/**
	 * 根据条件查询批属性
	 *
	 * @param skuLot 查询条件
	 * @return 批属性-列表
	 */
	List<SkuLotVO> selectList(SkuLot skuLot);

	/**
	 * 根据条件查询批属性
	 *
	 * @param page   分页信息
	 * @param skuLot 查询条件
	 * @return 批属性-分页
	 */
	IPage<SkuLotVO> selectPage(IPage<SkuLot> page, SkuLot skuLot);

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
	List<DataVerify> validExcel(List<SkuLotExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);

	/**
	 * 获取批属性配置
	 *
	 * @param skuId 物品id
	 * @return 批属性配置
	 */
	List<SkuLotConfigVO> getConfig(Long skuId);
}
