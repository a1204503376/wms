package org.nodes.wms.dao.basics.bom.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.bom.WmsSkuBomDao;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomExcelResponse;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;
import org.nodes.wms.dao.basics.bom.entites.SkuBom;
import org.nodes.wms.dao.basics.bom.mapper.WmsSkuBomMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 物料清单Dao实现
 */
@Service
@RequiredArgsConstructor
public class WmsSkuBomDaoImpl extends BaseServiceImpl<WmsSkuBomMapper, SkuBom> implements WmsSkuBomDao {
	private final WmsSkuBomMapper skuBomMapper;
	/**
	 * 分页查询
	 * @param page 底部分页参数
	 * @param skuBomPageQuery 查询条件
	 * @return 分页数据
	 */
	@Override
	public Page<WmsSkuBomResponse> selectPage(IPage<?> page, WmsSkuBomPageQuery skuBomPageQuery) {
		return skuBomMapper.getPage(page,skuBomPageQuery);
	}
	/**
	 * 根据条件查询对应的数据
	 * @param params 查询条件
	 * @return WmsSkuBom集合
	 */
	@Override
	public List<WmsSkuBomExcelResponse> getWmsSkuBomList(HashMap<String, Object> params) {
		return skuBomMapper.getWmsSkuBomList(params);
	}
}
