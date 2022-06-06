package org.nodes.wms.dao.basics.bom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomExcelResponse;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;
import org.nodes.wms.dao.basics.bom.entites.SkuBom;

import java.util.HashMap;
import java.util.List;

/**
 * 物料清单 Dao层
 */
public interface WmsSkuBomDao {
	/**
	 * 分页查询
	 * @param page 底部分页参数
	 * @param skuBomPageQuery 查询条件
	 * @return 分页数据
	 */
	Page<WmsSkuBomResponse> selectPage(IPage<?> page, @Param("query") WmsSkuBomPageQuery skuBomPageQuery);

	/**
	 * 根据条件查询对应的数据
	 * @param params 查询条件
	 * @return WmsSkuBom集合
	 */
	List<WmsSkuBomExcelResponse> getWmsSkuBomList(HashMap<String, Object> params);

	/**
	 * 根据物料清单id获取物料清单实体
	 * @param id 物料清单id
	 * @return SkuBom 物料清单实体
	 */
    SkuBom getSkuBomById(Long id);
}
