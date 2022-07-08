package org.nodes.wms.biz.basics.bom;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.bom.dto.input.DeleteSkuBomByIdsRequest;
import org.nodes.wms.dao.basics.bom.dto.input.SkuBomAddOrEditRequest;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomExcelResponse;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;
import org.nodes.wms.dao.basics.bom.entites.SkuBom;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 物料清单Biz
 */
public interface WmsSkuBomBiz {
	/**
	 * 分页查询
	 *
	 * @param query           底部分页参数
	 * @param skuBomPageQuery 查询条件
	 * @return 分页数据
	 */
	Page<WmsSkuBomResponse> getSkuBomPage(Query query, WmsSkuBomPageQuery skuBomPageQuery);


	/**
	 * 根据条件导出对应的数据
	 *
	 * @param params   导出条件
	 * @param response 响应对象
	 */
	void exportExcel(HashMap<String, Object> params, HttpServletResponse response);

	/**
	 * 根据物料清单id获取物料清单实体
	 *
	 * @param id 物料清单id
	 * @return SkuBom 物料清单实体
	 */
	SkuBom findSkuBomById(Long id);

	/**
	 * 新增或保存
	 *
	 * @param skuBomAddOrEditRequest: 新增或修改dto对象
	 * @return SkuBom
	 */
	SkuBom save(SkuBomAddOrEditRequest skuBomAddOrEditRequest);

	/**
	 * 物品清单管理-删除
	 *
	 * @param request 物料清单删除请求对象
	 * @return 是否成功
	 */
	Boolean deleteSkuBomByIds(DeleteSkuBomByIdsRequest request);

	/**
	 * 导入物料清单Excel
	 *
	 * @param importDataList
	 * @return
	 */
	boolean importExcel(List<WmsSkuBomExcelResponse> importDataList);
}
