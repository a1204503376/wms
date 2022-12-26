package org.nodes.wms.biz.basics.skuType.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.skuType.SkuTypeBiz;
import org.nodes.wms.biz.basics.skuType.modular.SkuTypeFactory;
import org.nodes.wms.dao.basics.skuType.SkuTypeDao;
import org.nodes.wms.dao.basics.skuType.dto.input.SkuTypeAddOrEditRequest;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 物品分类业务接口实现类
 **/
@Service
@RequiredArgsConstructor
public class SkuTypeBizImpl implements SkuTypeBiz {

	private final SkuTypeDao skuTypeDao;

	private final SkuTypeFactory skuTypeFactory;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SkuType save(SkuTypeAddOrEditRequest skuTypeAddOrEditRequest) {
		SkuType skuType = skuTypeFactory.createSkuType(skuTypeAddOrEditRequest);
		SkuType skuTypeResult = skuTypeDao.saveSkuType(skuType);

		// 如果上位分类为空，则设置分类路径为 #+当前分类id
		if (Func.isEmpty(skuTypeResult.getTypePreId())) {
			skuType.setTypePath("#" + Func.toStr(skuType.getSkuTypeId()));
		} else {
			SkuType skuTypePer = skuTypeDao.getSkuTypeById(skuTypeResult.getTypePreId());
			if (Func.isNotEmpty(skuTypePer)) {
				skuType.setTypePath(skuTypePer.getTypePath() + "#" + Func.toStr(skuType.getSkuTypeId()));
			} else {
				skuType.setTypePath("#" + Func.toStr(skuType.getSkuTypeId()));
			}
		}
		skuTypeDao.updateByTypeId(skuType);
		return skuType;
	}

    @Override
    public SkuType findById(Long id) {
        return skuTypeDao.getSkuTypeById(id);
    }

	@Override
	public List<SkuType> findAll() {
		return skuTypeDao.getAll();
	}
}
