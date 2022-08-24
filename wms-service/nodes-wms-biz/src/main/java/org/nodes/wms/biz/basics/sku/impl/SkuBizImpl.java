package org.nodes.wms.biz.basics.sku.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.sku.modular.SkuFactory;
import org.nodes.wms.dao.basics.sku.SkuDao;
import org.nodes.wms.dao.basics.sku.SkuIncDao;
import org.nodes.wms.dao.basics.sku.SkuReplaceDao;
import org.nodes.wms.dao.basics.sku.SkuUmDao;
import org.nodes.wms.dao.basics.sku.dto.input.SkuAddOrEditRequest;
import org.nodes.wms.dao.basics.sku.dto.input.SkuSelectQuery;
import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.output.SkuUmSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.*;
import org.nodes.wms.dao.basics.skuType.SkuTypeDao;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物品 业务层实现类
 */
@Service
@RequiredArgsConstructor
public class SkuBizImpl implements SkuBiz {

	private final SkuDao skuDao;
	private final SkuTypeDao skuTypeDao;
	private final SkuUmDao skuUmDao;

	private final SkuReplaceDao skuReplaceDao;

	private final SkuIncDao skuIncDao;

	private final SkuFactory skuFactory;

	@Override
	public List<SkuSelectResponse> getSkuSelectResponseTop10List(SkuSelectQuery skuSelectQuery) {
		return skuDao.listTop10BySkuCodeSkuName(skuSelectQuery.getKey(), skuSelectQuery.getKey());
	}

	@Override
	public Sku findById(Long skuId) {
		return skuDao.getById(skuId);
	}

	@Override
	public Sku findByCode(String skuCode) {
		return skuDao.getSkuByCode(skuCode);
	}

	@Override
	public SkuType findSkuTypeById(Long skuTypeId) {
		return skuTypeDao.getSkuTypeById(skuTypeId);
	}

	@Override
	public SkuType findSkuTypeByCode(String typeCode) {
		return skuTypeDao.getSkuTypeByCode(typeCode);
	}

	@Override
	public List<SkuUmSelectResponse> findSkuUmSelectResponseListBySkuId(Long skuId) {
		return skuDao.listSkuUmBySkuId(skuId);
	}

	@Override
	public SkuPackageAggregate findSkuPackageAggregateBySkuId(Long skuId) {
		return skuDao.getSkuPackageAggregateBySkuId(skuId);
	}

	@Override
	public SkuUm findSkuUmByUmCode(String skuUmCode) {
		return skuDao.getSkuUmByUmCode(skuUmCode);
	}

	@Override
	public SkuUm findSkuUmById(Long wsuId) {
		return skuUmDao.getSkuUmById(wsuId);
	}

	@Override
	public SkuPackageDetail findBaseSkuPackageDetail(Long skuId) {
		return skuDao.getBaseSkuPackageDetail(skuId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Sku save(SkuAddOrEditRequest skuAddOrEditRequest) {
		//新增或修改 物品
		Sku sku = skuFactory.createSku(skuAddOrEditRequest);
		skuDao.saveSku(sku);
		//新增或修改 替代物品
		List<SkuReplace> skuReplaceList = skuAddOrEditRequest.getSkuReplaceList();
		skuReplaceList.forEach(item -> item.setSkuId(sku.getSkuId()));
		skuReplaceDao.saveBatchSkuReplace(skuReplaceList);
		// 删除替代物品
		if (Func.isNotEmpty(skuAddOrEditRequest.getSkuReplaceIdList())) {
			skuReplaceDao.deleteByIdList(skuAddOrEditRequest.getSkuReplaceIdList());
		}
		//新增或修改 物品与供应商关联结合
		List<SkuInc> skuIncList = skuAddOrEditRequest.getSkuIncList();
		skuIncList.forEach(item -> item.setSkuId(sku.getSkuId()));
		skuIncDao.saveBatchSkuInc(skuIncList);
		// 删除物品与供应商关联
		if (Func.isNotEmpty(skuAddOrEditRequest.getSkuIncIdList())) {
			skuIncDao.deleteByIdList(skuAddOrEditRequest.getSkuIncIdList());
		}
		return sku;
	}

	@Override
	public SkuPackage findSkuPackageByWspId(Long wspId) {
		return skuDao.getSkuPackageByWspId(wspId);
	}

	@Override
	public List<String> getSkuDropDownBox() {
		List<Sku> skuList = skuDao.getSkuList();
		List<String> skuSpecList = skuList
			.stream()
			.filter(sku -> Func.isNotEmpty(sku.getSkuSpec()))
			.map(Sku::getSkuSpec)
			.distinct()
			.collect(Collectors.toList());
		return skuSpecList;
	}

	@Override
	public int getSkuCountByCode(String skuCode) {
		return skuDao.countByCode(skuCode);
	}

	@Override
	public int getSkuUmCountByUmCode(String umCode) {
		return skuUmDao.countByCode(umCode);
	}

	@Override
	public SkuPackageDetail getSkuPackageDetailBySkuId(Long skuId, String wsuCode) {
		return skuDao.getSkuPackageDetailBySkuId(skuId, wsuCode);
	}

	@Override
	public List<Sku> selectSkuListByNo(String no) {
		return skuDao.getSkuListByNo(no);
	}

    @Override
    public List<String> findSkuSpecSelectListBySkuId(Long skuId) {
		List<String> skuSpecList = new ArrayList<>();
		Sku sku = skuDao.getById(skuId);
		if(Func.isEmpty(sku)){
			throw new ServiceException("查询物品规格失败，该物品不存在");
		}
		if(Func.isNotEmpty(sku.getSkuSpec())){
			skuSpecList.add(sku.getSkuSpec());
		}else {
			List<String> specList = skuDao.getSkuList().stream()
				.map(Sku::getSkuSpec).sorted().collect(Collectors.toList());
			skuSpecList.addAll(specList);
		}
		return skuSpecList;
    }
}
