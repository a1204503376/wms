package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.dto.SkuDTO;
import org.nodes.wms.core.basedata.dto.SkuIncDTO;
import org.nodes.wms.core.basedata.dto.SkuReplaceDTO;
import org.nodes.wms.core.basedata.entity.*;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.core.basedata.excel.SkuExcel;
import org.nodes.wms.core.basedata.mapper.SkuMapper;
import org.nodes.wms.core.basedata.service.*;
import org.nodes.wms.core.basedata.vo.SkuIncVO;
import org.nodes.wms.core.basedata.vo.SkuLotVO;
import org.nodes.wms.core.basedata.vo.SkuLotValVO;
import org.nodes.wms.core.basedata.vo.SkuVO;
import org.nodes.wms.core.basedata.wrapper.SkuIncWrapper;
import org.nodes.wms.core.basedata.wrapper.SkuLotValWrapper;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
import org.nodes.wms.core.basedata.wrapper.SkuWrapper;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.dao.putway.entities.Instock;
import org.nodes.wms.core.strategy.entity.Outstock;
import org.nodes.wms.core.strategy.service.IInstockService;
import org.nodes.wms.core.strategy.service.IOutstockService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.*;
import org.nodes.wms.dao.basics.sku.enums.SnEnum;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物品 服务实现类
 *
 * @author pengwei
 * @since 2019-12-09
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuServiceImpl<M extends SkuMapper, T extends Sku>
	extends BaseServiceImpl<SkuMapper, Sku>
	implements ISkuService {

	@Autowired
	ISkuIncService skuIncService;
	@Autowired
	ISkuReplaceService skuReplaceService;
	@Autowired
	ISkuOutstockService skuOutstockService;
	@Autowired
	ISkuInstockService skuInstockService;
	@Autowired
	ISkuPackageService skuPackageService;
	@Autowired
	ISkuTypeService skuTypeService;
	@Autowired
	ISkuLotService skuLotService;
	@Autowired
	ISkuLotValService skuLotValService;
	@Autowired
	IStockService stockService;
	@Autowired
	IDictService dictService;

	@Override
	public IPage<Sku> selectPage(IPage<Sku> page, SkuDTO sku) {
		IPage<Sku> skuIPage = super.page(page, this.getQueryWrapper(sku));
		if (Func.isNotEmpty(sku.getStockIds())) {
			List<Stock> stocks = stockService.list(Wrappers.lambdaQuery(Stock.class)
				.in(Stock::getStockId, Func.toLongList(sku.getStockIds())));
			if (Func.isNotEmpty(stocks)) {
				List<Long> skuIds = NodesUtil.toList(stocks, Stock::getSkuId);
				List<Sku> skus = skuIPage.getRecords().stream().filter(sku1 -> skuIds.contains(sku1.getSkuId()))
					.collect(Collectors.toList());
				skuIPage.setRecords(skus);
			}
		}
		return skuIPage;
	}

	@Override
	public List<Sku> selectList(SkuDTO sku) {
		return super.list(this.getQueryWrapper(sku));
	}

	/**
	 * 获取模糊查询构造器
	 *
	 * @param sku 查询条件
	 * @return 查询构造器
	 */
	protected QueryWrapper<Sku> getQueryWrapper(SkuDTO sku) {
		QueryWrapper<Sku> skuQueryWrapper = Condition.getQueryWrapper(new Sku());
		if (Func.isNotEmpty(sku.getSkuCode())) {
			skuQueryWrapper.lambda().like(Sku::getSkuCode, sku.getSkuCode());
		}
		if (Func.isNotEmpty(sku.getSkuName())) {
			skuQueryWrapper.lambda().like(Sku::getSkuName, sku.getSkuName());
		}
		if (Func.isNotEmpty(sku.getWspName())) {
			List<SkuPackage> skuPackageList = skuPackageService.list(Condition.getQueryWrapper(new SkuPackage())
				.lambda().like(SkuPackage::getWspName, sku.getWspName()));
			if (Func.isNotEmpty(skuPackageList)) {
				skuQueryWrapper.lambda()
					.in(Sku::getWspId, NodesUtil.toList(skuPackageList, SkuPackage::getWspId));
			}
		}
		if (Func.isNotEmpty(sku.getSkuTypeId())) {
			// 获取该分类下的子级
			List<SkuType> skuTypeList = skuTypeService.list(Condition.getQueryWrapper(new SkuType()).lambda()
				.eq(SkuType::getTypePreId, sku.getSkuTypeId())
				.or()
				.eq(SkuType::getSkuTypeId, sku.getSkuTypeId()));
			if (Func.isNotEmpty(skuTypeList)) {
				skuQueryWrapper.lambda()
					.in(Sku::getSkuTypeId, NodesUtil.toList(skuTypeList, SkuType::getSkuTypeId));
			}
		}
		if (Func.isNotEmpty(sku.getIsSn())) {
			skuQueryWrapper.lambda().eq(Sku::getIsSn, sku.getIsSn());
		}
		if (Func.isNotEmpty(sku.getWoId())) {
			skuQueryWrapper.lambda().eq(Sku::getWoId, sku.getWoId());
		}
		if (Func.isNotEmpty(sku.getAbc())) {
			skuQueryWrapper.lambda().eq(Sku::getAbc, sku.getAbc());
		}
//		if (!NodesUtil.isAllNull(sku)&& skuQueryWrapper.isEmptyOfWhere()) {
//			skuQueryWrapper.apply("1 <> 1");
//		}
		return skuQueryWrapper;
	}

	@Override
	public SkuVO getDetail(SkuDTO sku) {
		Sku entity = super.getById(sku.getSkuId());

		if (Func.isEmpty(entity)) {
			throw new ServiceException("物品不存在");
		}
		SkuVO skuVo = SkuWrapper.build().entityVO(entity);
		List<SkuIncVO> skuIncVO = SkuIncWrapper.build().listVO(skuIncService.list(Wrappers.lambdaQuery(SkuInc.class)
			.eq(SkuInc::getSkuId, skuVo.getSkuId())));
		if (Func.isNotEmpty(skuIncVO)) {
			skuVo.setSkuIncList(skuIncVO);
		}
		skuVo.setSkuReplaceList(skuReplaceService.listBySkuId(skuVo.getSkuId()));
		ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
		SkuLotVO skuLotVO = SkuLotWrapper.build().entityVO(skuLotService.getById(skuVo.getWslId()));
		if (Func.isNotEmpty(skuLotVO)) {
			skuVo.setSkuLot(skuLotVO);
		}
		ISkuLotValService skuLotValService = SpringUtil.getBean(ISkuLotValService.class);
		SkuLotValVO skuLotValVO = SkuLotValWrapper.build().entityVO(skuLotValService.getById(skuVo.getWslvId()));
		if (Func.isNotEmpty(skuLotValVO)) {
			skuVo.setSkuLotVal(skuLotValVO);
		}
		SkuLot skuLot = skuLotService.getById(entity.getWslId());
		if (Func.isNotEmpty(skuLot)) {
			skuVo.setSkuLotName(skuLot.getSkuLotName());
		}
		SkuLotVal skuLotVal = skuLotValService.getById(entity.getWslvId());
		if (Func.isNotEmpty(skuLotVal)) {
			skuVo.setSkuLotValName(skuLotVal.getSkuLotValName());
		}
		skuVo.setAbcName(DictCache.getValue(DictConstant.LOC_ABC, entity.getAbc()));
		return skuVo;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		List<Sku> skuList = super.listByIds(idList);
		for (Sku sku : skuList) {
			if (ObjectUtil.isNotEmpty(sku)) {
				continue;
			}
			// 判断物品是否有库存
			List<Stock> stockList = stockService.list(Condition.getQueryWrapper(new Stock())
				.lambda()
				.eq(Stock::getSkuId, sku.getSkuId()));
			if (Func.isNotEmpty(stockList)) {
				throw new ServiceException("有库存的物品不允许删除（物品：" + sku.getSkuName() + " ）！");
			}
		}
		// 删除-替代品
		skuReplaceService.remove(Condition.getQueryWrapper(new SkuReplace()).lambda()
			.in(SkuReplace::getSkuId, idList));
		// 删除-企业关联
		skuIncService.remove(Condition.getQueryWrapper(new SkuInc()).lambda()
			.in(SkuInc::getSkuId, idList));
		// 删除-出库设置
		skuOutstockService.remove(Condition.getQueryWrapper(new SkuOutstock()).lambda()
			.in(SkuOutstock::getSkuId, idList));
		// 删除-入库设置
		skuInstockService.remove(Condition.getQueryWrapper(new SkuInstock()).lambda()
			.in(SkuInstock::getSkuId, idList));
		boolean result = super.removeByIds(idList);
		if (result) {
			//super.removeByIds(idList);
		}
		return result;
	}

	@Override
	public boolean save(@Validated SkuDTO entity) {
		this.validData(entity);

		boolean result = super.save(entity);
		// 物品替代
		if (ObjectUtil.isNotEmpty(entity.getSkuReplaceList())) {
			for (SkuReplace skuReplace : entity.getSkuReplaceList()) {
				Integer hasReplace = skuReplaceService.count(Condition.getQueryWrapper(new SkuReplace()).lambda()
					.eq(SkuReplace::getSkuId, skuReplace.getWsrepSkuId()));
				if (hasReplace > 0) {
					// 如果替代品也设置了替代品抛出异常（避免生成拣货计划时死循环）
					Sku replaceSku = super.getById(skuReplace.getWsrepSkuId());
					if (ObjectUtil.isNotEmpty(replaceSku)) {
						throw new ServiceException(
							"物品：：" + replaceSku.getSkuId() + " 已关联替代品， 不允许被设置为替代品！");
					} else {
						throw new ServiceException(
							"物品ID：：" + skuReplace.getWsrepSkuId() + " 已关联替代品， 不允许被设置为替代品！");
					}
				}
				skuReplace.setSkuId(entity.getSkuId());

				skuReplaceService.save(skuReplace);
			}
		}
		// 物品供应商关联
		if (ObjectUtil.isNotEmpty(entity.getSkuIncList())) {
			for (SkuInc skuInc : entity.getSkuIncList()) {
				skuInc.setSkuId(entity.getSkuId());

				skuIncService.save(skuInc);
			}
		}
		if (result) {
			//super.saveOrUpdate(entity);
		}
		this.saveSkuInstock(entity);
		this.saveSkuOutstock(entity);
		return result;
	}

	protected boolean saveSkuInstock(Sku sku) {
		IInstockService instockService = SpringUtil.getBean(IInstockService.class);
		// 找到默认的上架策略
		/*Instock instock = InstockCache.list().stream().filter(u -> {
			return Func.equals(u.getIsDefault(), 1);
		}).findFirst().orElse(null);*/

		Instock instock = instockService.list(Condition.getQueryWrapper(new Instock())
			.lambda()
			.eq(Instock::getIsDefault, 1)
		).stream().findFirst().orElse(null);
		if (Func.isEmpty(instock)) {
			// 没有默认的策略，则退出（不生成默认的入库设置了）
			return false;
		}
		// 找到默认的入库设置
		ISkuInstockService skuInstockService = SpringUtil.getBean(ISkuInstockService.class);
		SkuInstock skuInstockDefault = skuInstockService.list().stream().filter(u -> {
			return Func.equals(u.getIsDefault(), 1);
		}).findFirst().orElse(null);
		if (Func.isEmpty(skuInstockDefault)) {
			return false;
		}
		List<SkuInstock> skuInstockList = new ArrayList<>();
		IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
		List<Warehouse> warehouseList = warehouseService.list();
		for (Warehouse warehouse : warehouseList) {
			SkuInstock skuInstock = new SkuInstock();
			BeanUtil.copyProperties(skuInstockDefault, skuInstock);
			skuInstock.setWsiId(null);
			skuInstock.setWhId(warehouse.getWhId());
			skuInstock.setSkuId(sku.getSkuId());
			skuInstock.setWoId(sku.getWoId());
			skuInstockList.add(skuInstock);
		}
		return skuInstockService.saveBatch(skuInstockList, skuInstockList.size());
	}

	protected boolean saveSkuOutstock(Sku sku) {
		// 找到默认的分配策略
		IOutstockService outstockService = SpringUtil.getBean(IOutstockService.class);
		/*Outstock outstock = OutstockCache.list().stream().filter(u -> {
			return Func.equals(u.getIsDefault(), 1);
		}).findFirst().orElse(null);*/
		Outstock outstock = outstockService.list(Condition.getQueryWrapper(new Outstock())
			.lambda()
			.eq(Outstock::getIsDefault, 1)
		).stream().findFirst().orElse(null);
		if (Func.isEmpty(outstock)) {
			// 没有默认的策略，则退出（不生成默认的出库设置了）
			return false;
		}
		// 找到默认的出库设置
		ISkuOutstockService skuOutstockService = SpringUtil.getBean(ISkuOutstockService.class);
		SkuOutstock skuOutstockDefault = skuOutstockService.list().stream().filter(u -> {
			return u.getIsDefault() == 1;
		}).findFirst().orElse(null);
		if (Func.isEmpty(skuOutstockDefault)) {
			return false;
		}
		IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
		List<SkuOutstock> skuOutstockList = new ArrayList<>();
		List<Warehouse> warehouseList = warehouseService.list();
		for (Warehouse warehouse : warehouseList) {
			SkuOutstock skuOutstock = new SkuOutstock();
			BeanUtil.copyProperties(skuOutstockDefault, skuOutstock);
			skuOutstock.setWsoId(null);
			skuOutstock.setWhId(warehouse.getWhId());
			skuOutstock.setSkuId(sku.getSkuId());
			skuOutstock.setWoId(sku.getWoId());
			skuOutstockList.add(skuOutstock);
		}
		return skuOutstockService.saveBatch(skuOutstockList, skuOutstockList.size());
	}

	@Override
	public boolean updateById(@Validated SkuDTO entity) {

		this.validData(entity);
		//处理该物品库存的包装id
		stockService.update(Wrappers.lambdaUpdate(Stock.class).eq(Stock::getSkuId, entity.getSkuId())
			.set(Stock::getWspId, entity.getWspId()));
		boolean result = super.updateById(entity);
		// 物品替代
		if (ObjectUtil.isNotEmpty(entity.getSkuReplaceDeletedList())) {
			for (SkuReplace skuReplace : entity.getSkuReplaceDeletedList()) {
				skuReplaceService.removeById(skuReplace.getWsrepId());
			}
		}
		if (ObjectUtil.isNotEmpty(entity.getSkuReplaceList())) {
			for (SkuReplace skuReplace : entity.getSkuReplaceList()) {
				SkuReplace find = skuReplaceService.getById(skuReplace.getWsrepId());
				if (ObjectUtil.isEmpty(find)) {
					skuReplace.setSkuId(entity.getSkuId());
				}
				skuReplaceService.saveOrUpdate(skuReplace);
			}
		}
		// 物品供应商关联
		if (ObjectUtil.isNotEmpty(entity.getSkuIncDeletedList())) {
			for (SkuInc skuInc : entity.getSkuIncDeletedList()) {
				skuIncService.removeById(skuInc.getWssupId());
			}
		}
		if (ObjectUtil.isNotEmpty(entity.getSkuIncList())) {
			for (SkuInc skuInc : entity.getSkuIncList()) {
				SkuInc find = skuIncService.getById(skuInc.getWssupId());
				if (ObjectUtil.isEmpty(find)) {
					skuInc.setSkuId(entity.getSkuId());
				}
				skuIncService.saveOrUpdate(skuInc);
			}
		}
		if (result) {
			//super.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean saveOrUpdate(SkuDTO entity) {
		if (Func.isEmpty(super.getIdVal(entity))) {
			Sku sku = getOne(Wrappers.lambdaQuery(Sku.class)
				.eq(Sku::getSkuCode, entity.getSkuCode()));
			if (Func.isNotEmpty(sku)) {
				throw new ServiceException("系统中已经存在该物品编码！");
			}
			return this.save(entity);
		} else {
			Sku sku = getOne(Wrappers.lambdaQuery(Sku.class)
				.ne(Sku::getSkuId, entity.getSkuId())
				.eq(Sku::getSkuCode, entity.getSkuCode()));
			if (Func.isNotEmpty(sku)) {
				throw new ServiceException("系统中已经存在该物品编码！");
			}
			return this.updateById(entity);
		}
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<Sku> skuList = this.list(Condition.getQueryWrapper(params, Sku.class));
		if (Func.isNotEmpty(skuList)) {
			List<SkuExcel> skuExportList = this.getSkuExportDTOList(skuList);
			ExcelUtil.export(response, "物品", "物品数据表", skuExportList, SkuExcel.class);
		}
	}

	@Override
	public List<DataVerify> validExcel(List<SkuExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();
		ISkuUmService skuUmService = SpringUtil.getBean(ISkuUmService.class);
		if (Func.isNotEmpty(dataList)) {
			//物品主表查询
			List<String> skuCodeList = NodesUtil.toList(dataList, SkuExcel::getSkuCode);
			//List<Sku> skuList = SkuCache.listByCode(skuCodeList);
			List<Sku> skuList = super.list(Condition.getQueryWrapper(new Sku())
				.lambda()
				.in(Sku::getSkuCode, skuCodeList)
			).stream().collect(Collectors.toList());
			//物品主表货主列表
			List<String> ownerCodeList = NodesUtil.toList(dataList, SkuExcel::getOwnerCode);
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			List<Owner> ownerList = ownerService.list().stream().filter(u -> {
				return ownerCodeList.contains(u.getOwnerCode());
			}).collect(Collectors.toList());
			//物品分类
			QueryWrapper<SkuType> stqw = new QueryWrapper<>();
			List<String> typeCodeList = NodesUtil.toList(dataList, SkuExcel::getTypeCode);
			stqw.lambda().in(SkuType::getTypeCode, typeCodeList);
			List<SkuType> skuTypeList = new ArrayList<>();
			if (Func.isNotEmpty(typeCodeList)) {
				skuTypeList = skuTypeService.list(stqw);
			}
			//物品包装列表
			QueryWrapper<SkuPackage> spqw = new QueryWrapper<>();
			List<String> wspNameList = NodesUtil.toList(dataList, SkuExcel::getPackageName);
			spqw.lambda().in(SkuPackage::getWspName, wspNameList);
			List<SkuPackage> skuePackageList = new ArrayList<>();
			if (Func.isNotEmpty(wspNameList)) {
				skuePackageList = skuPackageService.list(spqw);
			}
			//批属性标签
			QueryWrapper<SkuLot> slqw = new QueryWrapper<>();
			List<String> skuLotCodeList = NodesUtil.toList(dataList, SkuExcel::getSkuLotCode);
			slqw.lambda().in(SkuLot::getSkuLotCode, skuLotCodeList);
			List<SkuLot> skuLotList = new ArrayList<>();
			if (Func.isNotEmpty(skuLotCodeList)) {
				skuLotList = skuLotService.list(slqw);
			}
			//批属性规则
			QueryWrapper<SkuLotVal> slvqw = new QueryWrapper<>();
			List<String> skuLotValNameList = NodesUtil.toList(dataList, SkuExcel::getSkuLotVal);
			slvqw.lambda().in(SkuLotVal::getSkuLotValName, skuLotValNameList);
			List<SkuLotVal> skuLotValList = new ArrayList<>();
			if (Func.isNotEmpty(skuLotValNameList)) {
				skuLotValList = skuLotValService.list(slvqw);
			}
			//被替代物品包装列表
			spqw = new QueryWrapper<>();
			List<String> replaceWspNameList = NodesUtil.toList(dataList, SkuExcel::getReplacePackageName1);
			spqw.lambda().in(SkuPackage::getWspName, replaceWspNameList);
			List<SkuPackage> skuReplacePackageList = new ArrayList<>();
			if (Func.isNotEmpty(replaceWspNameList)) {
				skuReplacePackageList = skuPackageService.list(spqw);
			}
			//被替代物品计量单位
			List<String> replaceWsuCodeList = NodesUtil.toList(dataList, SkuExcel::getUnitCode);
			//List<SkuUm> skuReplaceUmList = SkuUmCache.listByCode(replaceWsuCodeList);
			List<SkuUm> skuReplaceUmList = skuUmService.list(Condition.getQueryWrapper(new SkuUm())
				.lambda()
				.in(SkuUm::getWsuCode, replaceWsuCodeList)
			).stream().collect(Collectors.toList());
			List<String> replaceWsrepOwnerCodeList = getReplaceWsrepOwnerCodeList(dataList);
			List<Owner> skuReplaceWsrepOwnerList = ownerService.list().stream().filter(u -> {
				return replaceWsrepOwnerCodeList.contains(u.getOwnerCode());
			}).collect(Collectors.toList());
			//替代物品列表
			List<String> replaceWsrepSkuCodeList = NodesUtil.toList(
				dataList, SkuExcel::getReplaceSkuCode);
			//List<Sku> skuReplaceWsrepSkuList = SkuCache.listByCode(replaceWsrepSkuCodeList);
			List<Sku> skuReplaceWsrepSkuList = super.list(Condition.getQueryWrapper(new Sku())
				.lambda()
				.in(Sku::getSkuCode, replaceWsrepSkuCodeList)
			).stream().collect(Collectors.toList());
			//替代物品包装
			spqw = new QueryWrapper<>();
			List<String> replaceWsrepWspNameList = NodesUtil.toList(
				dataList, SkuExcel::getReplacePackageName);
			spqw.lambda().in(SkuPackage::getWspName, replaceWsrepWspNameList);
			List<SkuPackage> skuReplaceWsrepPackageList = new ArrayList<>();
			if (Func.isNotEmpty(replaceWsrepWspNameList)) {
				skuReplaceWsrepPackageList = skuPackageService.list(spqw);
			}
			//替代物品计量单位
			List<String> replaceWsrepWsuCodeList = NodesUtil.toList(
				dataList, SkuExcel::getReplaceUnitCode);
			//List<SkuUm> skuReplaceWsrepUmList = SkuUmCache.listByCode(replaceWsrepWsuCodeList);
			List<SkuUm> skuReplaceWsrepUmList = skuUmService.list(Condition.getQueryWrapper(new SkuUm())
				.lambda()
				.in(SkuUm::getWsuCode, replaceWsrepWsuCodeList)
			).stream().collect(Collectors.toList());

			//企业关联的企业集合
			List<String> enterpriseCodeList = NodesUtil.toList(
				dataList, SkuExcel::getEnterpriseCode);
			//List<Enterprise> skuIncEnterpriseList = EnterpriseCache.listByCode(enterpriseCodeList);
			List<Enterprise> skuIncEnterpriseList = new ArrayList<>();
			if (Func.isNotEmpty(enterpriseCodeList)) {
				IEnterpriseService enterpriseService = SpringUtil.getBean(IEnterpriseService.class);
				skuIncEnterpriseList = enterpriseService.list(Condition.getQueryWrapper(new Enterprise())
					.lambda()
					.in(Enterprise::getEnterpriseCode, enterpriseCodeList));
			}


			//企业关联的包装集合
			spqw = new QueryWrapper<>();
			List<String> enterpriseWspNameList = NodesUtil.toList(
				dataList, SkuExcel::getEnterprisePackageName);
			spqw.lambda().in(SkuPackage::getWspName, enterpriseWspNameList);
			List<SkuPackage> skuIncPackageList = new ArrayList<>();
			if (Func.isNotEmpty(enterpriseWspNameList)) {
				skuIncPackageList = skuPackageService.list(spqw);
			}

			Map<String, SkuDTO> cache = new HashMap<>();
			int index = 1;
			for (SkuExcel skuExcel : dataList) {
				DataVerify dataVerify = new DataVerify();
				List<String> errorList = new ArrayList<>();
				dataVerify.setIndex(index);
				String cacheKey = skuExcel.getSkuCode() + skuExcel.getOwnerCode();
				SkuDTO skuDTO = new SkuDTO();
				skuDTO.setSkuReplaceList(new ArrayList<>());
				skuDTO.setSkuIncList(new ArrayList<>());
				skuDTO.setSkuCode(skuExcel.getSkuCode());
				skuDTO.setOwnerName(skuExcel.getOwnerName());
				skuDTO.setOwnerCode(skuExcel.getOwnerCode());
				skuDTO.setWspName(skuExcel.getPackageName());
				skuDTO.setSkuName(skuExcel.getSkuName());
				skuDTO.setSkuNameS(skuExcel.getSkuNameS());
				skuDTO.setSkuNetWeight(new BigDecimal(skuExcel.getSkuNetWeight()));
				skuDTO.setSkuGrossWeight(new BigDecimal(skuExcel.getSkuGrossWeight()));
				skuDTO.setSkuTareWeight(new BigDecimal(skuExcel.getSkuTareWeight()));
				skuDTO.setSkuVolume(new BigDecimal(skuExcel.getSkuVolume()));
				skuDTO.setSkuBarcodeList(skuExcel.getSkuBarcodeList());
				skuDTO.setSkuRemark(skuExcel.getRemarks());
				Dict dict = DictCache.list(DictConstant.INVENTORY_TYPE).stream().filter(u->{
					return Func.equals(u.getDictValue(), skuExcel.getStorageType());
				}).findFirst().orElse(null);
				skuDTO.setSkuStorageType(Func.isEmpty(dict) ? null : dict.getDictKey());
				skuDTO.setQualityHours(Integer.valueOf(skuExcel.getShelfLife()));
				// 开始效验数据
				ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(skuExcel);
				List<Owner> owners = ownerList.stream()
					.filter(owner -> owner.getOwnerCode().equals(skuExcel.getOwnerCode()))
					.collect(Collectors.toList());
				if (Func.isNotEmpty(owners)) {
					skuDTO.setWoId(owners.get(0).getWoId());
					//验证物品是否存在
					List<Sku> skus = skuList.stream().filter(sku ->
						sku.getSkuCode().equals(skuExcel.getSkuCode()) &&
							sku.getWoId().equals(owners.get(0).getWoId()))
						.collect(Collectors.toList());
					if (Func.isNotEmpty(skus))
						errorList.add(String.format("编号为%s,货主编号为%s的物品已经存在",
							skuExcel.getSkuCode(), skuExcel.getOwnerCode()));

				} else {
					errorList.add(String.format("编号为%s的货主不存在",
						skuExcel.getOwnerCode()));
				}
				//物品分类
				List<SkuType> skuTypes = skuTypeList.stream()
					.filter(skuType -> skuType.getTypeCode().equals(skuExcel.getTypeCode())
					).collect(Collectors.toList());
				if (Func.isNotEmpty(skuTypes)) {
					skuDTO.setSkuTypeId(skuTypes.get(0).getSkuTypeId());
				} else {
					errorList.add(String.format("编号为%s的物品分类不存在",
						skuExcel.getTypeCode()));
				}
				//包装名称
				List<SkuPackage> skuPackages = skuePackageList.stream().filter(skuPackage ->
					skuPackage.getWspName().equals(skuExcel.getPackageName())).collect(Collectors.toList());
				if (Func.isNotEmpty(skuPackages)) {
					skuDTO.setWspId(skuPackages.get(0).getWspId());
				} else {
					errorList.add(String.format("名称为%s的包装不存在", skuExcel.getPackageName()));
				}
				//批属性标签
				List<SkuLot> skuLots = skuLotList.stream().filter(skuLot ->
					skuLot.getSkuLotCode().equals(skuExcel.getSkuLotCode())
				).collect(Collectors.toList());
				if (Func.isNotEmpty(skuLots)) {
					skuDTO.setWslId(skuLots.get(0).getWslId());
				} else {
					errorList.add(String.format("编号为%s的批属性标签不存在",
						skuExcel.getSkuLotCode()));
				}
				//批属性验证名称
				List<SkuLotVal> skuLotVals = skuLotValList.stream().filter(skuLotVal ->
					skuLotVal.getSkuLotValName().equals(skuExcel.getSkuLotVal())).collect(Collectors.toList());
				if (Func.isNotEmpty(skuLotVals)) {
					skuDTO.setWslvId(skuLotVals.get(0).getWslvId());
				} else {
					errorList.add(String.format("名称为%s的批属性验证规则不存在",
						skuExcel.getSkuLotVal()));
				}
				//是否序列号管理
				skuDTO.setIsSn(SnEnum.YES.getName().equals(skuExcel.getIsSn()) ?
					SnEnum.YES.getIndex() : SnEnum.NO.getIndex());

				if (Func.isNotEmpty(skuExcel.getReplaceSkuCode())) {
					SkuReplaceDTO skuReplaceDTO = new SkuReplaceDTO();
					//替换物品
					skuReplaceDTO.setReplaceSkuCode(skuExcel.getSkuCode());//被替代物品物品编码
					skuReplaceDTO.setReplaceSkuName(skuExcel.getSkuName());//被替代物品物品名称
					skuReplaceDTO.setReplaceOwnerCode(skuExcel.getOwnerCode());//被替代物品货主编码
					skuReplaceDTO.setReplaceOwnerName(skuExcel.getOwnerName());//被替代物品货主名称
					skuReplaceDTO.setQty(new BigDecimal(skuExcel.getSkuCount())); //被替代物品属性
					skuReplaceDTO.setWsrepQty(new BigDecimal(skuExcel.getReplaceSkuCount()));//替代物品数量
					//被替代物品包装
					List<SkuPackage> skuReplacePackages = skuReplacePackageList.stream().filter(skuReplacePackage ->
						skuReplacePackage.getWspName().equals(skuExcel.getReplacePackageName1())).collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplacePackages)) {
						skuReplaceDTO.setWspId(skuReplacePackages.get(0).getWspId());
					} else {
						errorList.add(String.format("名称为%s的被替代物品包装不存在",
							skuExcel.getReplacePackageName1()));
					}
					//被替代物品单位
					List<SkuUm> skuReplaceUms = skuReplaceUmList.stream().filter(skuReplaceUm ->
						skuReplaceUm.getWsuCode().equals(skuExcel.getUnitCode())
					)
						.collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplaceUms)) {
						skuReplaceDTO.setWsuId(skuReplaceUms.get(0).getWsuId());
					} else {
						errorList.add(String.format("编号为%s的被替代物品单位不存在",
							skuExcel.getUnitCode()));
					}

					//替代物品货主
					List<Owner> skuReplaceWsrepOwners = skuReplaceWsrepOwnerList.stream().filter(skuReplaceWsrepOwner ->
						skuReplaceWsrepOwner.getOwnerCode().equals(skuExcel.getReplaceOwnerCode())
					)
						.collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplaceWsrepOwners)) {
						Long woId = skuReplaceWsrepOwners.get(0).getWoId();
						//替代物品
						List<Sku> skuReplaceWsrepSkus = skuReplaceWsrepSkuList.stream().filter(skuReplaceWsrepSku ->
							skuReplaceWsrepSku.getSkuCode().equals(skuExcel.getReplaceSkuCode()) &&
								skuReplaceWsrepSku.getWoId().equals(woId))
							.collect(Collectors.toList());
						if (Func.isNotEmpty(skuReplaceWsrepSkus)) {
							skuReplaceDTO.setWsrepSkuId(skuReplaceWsrepSkus.get(0).getSkuId());
						} else {
							errorList.add(String.format("替代物品编号为%s,替代货主为%s的替代物品不存在",
								skuExcel.getReplaceSkuCode(), skuExcel.getReplaceOwnerCode()));
						}

					} else {
						errorList.add(String.format("编号为%s的替代物品货主不存在",
							skuExcel.getReplaceOwnerCode()));
						//替代物品
						List<Sku> skuReplaceWsrepSkus = skuReplaceWsrepSkuList.stream().filter(skuReplaceWsrepSku ->
							skuReplaceWsrepSku.getSkuCode().equals(skuExcel.getReplaceSkuCode()))
							.collect(Collectors.toList());
						if (Func.isEmpty(skuReplaceWsrepSkus)) {
							errorList.add(String.format("编号为%s的替代物品不存在",
								skuExcel.getReplaceSkuCode()));
						}
					}
					//替代物品包装
					List<SkuPackage> skuReplaceWsrepPackages = skuReplaceWsrepPackageList.stream().filter(skuReplaceWsrepPackage ->
						skuReplaceWsrepPackage.getWspName().equals(skuExcel.getReplacePackageName()))
						.collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplaceWsrepPackages)) {
						skuReplaceDTO.setWsrepWspId(skuReplaceWsrepPackages.get(0).getWspId());
					} else {
						errorList.add(String.format("名称为%s的替代物品包装不存在",
							skuExcel.getReplacePackageName()));
					}
					List<SkuUm> skuReplaceWsrepUms = skuReplaceWsrepUmList.stream().filter(skuReplaceWsrepUm ->
						skuReplaceWsrepUm.getWsuCode().equals(skuExcel.getReplaceUnitCode()))
						.collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplaceWsrepUms)) {
						skuReplaceDTO.setWsrepWsuId(skuReplaceWsrepUms.get(0).getWsuId());
					} else {
						errorList.add(String.format("编号为%s的替代物品单位不存在",
							skuExcel.getReplaceUnitCode()));
					}
					skuDTO.getSkuReplaceList().add(skuReplaceDTO);
				}
				if (Func.isNotEmpty(skuExcel.getEnterpriseName())) {
					SkuIncDTO skuIncDTO = new SkuIncDTO();
					//Sku sku = SkuCache.getSku(skuExcel.getEnterpriseSkuName(), owners.get(0).getWoId());
					Sku sku = super.list(Condition.getQueryWrapper(new Sku())
						.lambda()
						.eq(Sku::getSkuName, skuExcel.getEnterpriseSkuName())
						.eq(Sku::getWoId, owners.get(0).getWoId())
					).stream().findFirst().orElse(null);
					if (Func.isNotEmpty(sku)) {
						skuIncDTO.setReplaceSkuName(sku.getSkuName()); //关连物品名称
						skuIncDTO.setReplaceSkuCode(sku.getSkuCode());
						skuIncDTO.setSkuId(sku.getSkuId());
						skuIncDTO.setSkuName(sku.getSkuName());
					} else {
						errorList.add(String.format("企业物品[%s]不存在！",
							skuExcel.getEnterpriseSkuName()));
					}
					skuIncDTO.setReplaceOwnerCode(skuExcel.getOwnerCode()); //关连物品货主编码
					skuIncDTO.setReplaceOwnerName(skuExcel.getOwnerName()); //关连物品货主名称
					Field[] declaredFields = SkuExcel.class.getDeclaredFields();
					for (Field field : declaredFields) {
						field.setAccessible(true);
						try {
							if (field.getName().contains("extendField") && Func.isNotEmpty(field.get(skuExcel))) {
								String index1 = field.getName().substring("extendField".length(), field.getName().length());
								Method method = SkuIncDTO.class.getMethod("setAttribute" + index1, String.class);
								method.invoke(skuIncDTO, field.get(skuExcel));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					//供应商
					List<Enterprise> skuIncEnterprises = skuIncEnterpriseList.stream().filter(skuIncEnterprise ->
						skuIncEnterprise.getEnterpriseCode().equals(skuExcel.getEnterpriseCode()))
						.collect(Collectors.toList());
					if (Func.isNotEmpty(skuIncEnterprises)) {
						skuIncDTO.setPeId(skuIncEnterprises.get(0).getPeId());
					} else {
						errorList.add(String.format("编号为%s的供应商不存在",
							skuExcel.getEnterpriseCode()));
					}
					//供应商包装
					List<SkuPackage> skuIncPackages = skuIncPackageList.stream().filter(skuIncPackage ->
						skuIncPackage.getWspName().equals(skuExcel.getEnterprisePackageName()))
						.collect(Collectors.toList());
					if (Func.isNotEmpty(skuIncPackages)) {
						skuIncDTO.setWspId(skuIncPackages.get(0).getWspId());
					} else {
						errorList.add(String.format("名称为%s的供应商包装不存在",
							skuExcel.getEnterprisePackageName()));
					}
					//供应商关系列表
					skuDTO.getSkuIncList().add(skuIncDTO);
				}
				if (validResult.hasErrors()) {
					errorList.add(StringUtil.join(validResult.getAllErrors()));
				}
				if (Func.isNotEmpty(errorList)) {
					dataVerify.setMessage(StringUtil.join(errorList));
				} else {
					if (cache.containsKey(cacheKey)) {
						// 更新地址、联系人信息
						cache.get(cacheKey).getSkuIncList().addAll(skuDTO.getSkuIncList());
						cache.get(cacheKey).getSkuReplaceList().addAll(skuDTO.getSkuReplaceList());
					} else {
						// 记录到缓存map中
						cache.put(cacheKey, skuDTO);
					}
					dataVerify.setCacheKey(cacheKey);
				}
				dataVerifyList.add(dataVerify);
				index++;
			}
			if (Func.isNotEmpty(cache)) {
				// 存储数据到redis缓存中
				for (String code : cache.keySet()) {
					SkuCache.put(code, cache.get(code));
				}
			}
		}
		return dataVerifyList;
	}

	private List<String> getReplaceWsrepOwnerCodeList(List<SkuExcel> dataList) {
		return NodesUtil.toList(
			dataList, SkuExcel::getReplaceOwnerCode);
	}

	@Override
	public boolean importData(List<DataVerify> dataVerifyList) {
		if (Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("没有数据可以保存！");
		}
		for (DataVerify dataVerify : dataVerifyList) {
			if (ObjectUtil.isEmpty(dataVerify)) {
				continue;
			}
			SkuDTO skuDTO = SkuCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(skuDTO)) {
				continue;
			}
			if (this.saveOrUpdate(skuDTO)) {
				SkuCache.remove(dataVerify.getCacheKey());
				//super.saveOrUpdate(skuDTO);
			}
		}
		return true;
	}

	private List<SkuExcel> getSkuExportDTOList(List<Sku> skuList) {
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
		ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
		ISkuLotValService skuLotValService1 = SpringUtil.getBean(ISkuLotValService.class);
		// 最终返回的物品的导出列表
		List<SkuExcel> skuExportList = new ArrayList<>();
		//查询结果的SKUID集合
		List<Long> skuIdsList = NodesUtil.toList(skuList, Sku::getSkuId);
		//查询结果的所有替代物品关系集合
		QueryWrapper<SkuReplace> srqw = new QueryWrapper<>();
		srqw.lambda().in(SkuReplace::getSkuId, skuIdsList);
		List<SkuReplace> skuReplaceList = new ArrayList<>();
		if (Func.isNotEmpty(skuIdsList)) {
			skuReplaceList = skuReplaceService.list(srqw);
		}
		//查询结果的物品与企业关系集合
		QueryWrapper<SkuInc> siqw = new QueryWrapper<>();
		siqw.lambda().in(SkuInc::getSkuId, skuIdsList);
		List<SkuInc> skuIncList = new ArrayList<>();
		if (Func.isNotEmpty(skuIdsList)) {
			skuIncList = skuIncService.list(siqw);
		}
		//开始循环查询物品结果集 用于封装数据
		for (Sku sku : skuList) {
			//实体类装换
			SkuExcel skuExportDTO = new SkuExcel();
			skuExportDTO.setSkuCode(sku.getSkuCode());
			skuExportDTO.setSkuName(sku.getSkuName());
			skuExportDTO.setSkuNameS(sku.getSkuNameS());
			skuExportDTO.setAbc(DictCache.getValue(DictConstant.LOC_ABC, sku.getAbc()));
			skuExportDTO.setSkuGrossWeight(sku.getSkuGrossWeight().stripTrailingZeros().toPlainString());
			skuExportDTO.setSkuNetWeight(sku.getSkuNetWeight().stripTrailingZeros().toPlainString());
			skuExportDTO.setSkuTareWeight(sku.getSkuTareWeight().stripTrailingZeros().toPlainString());
			skuExportDTO.setSkuVolume(sku.getSkuVolume().stripTrailingZeros().toPlainString());
			skuExportDTO.setStorageType(DictCache.getValue(DictConstant.INVENTORY_TYPE, sku.getSkuStorageType()));
			skuExportDTO.setRemarks(sku.getSkuRemark());
			skuExportDTO.setSkuBarcodeList(sku.getSkuBarcodeList());
			skuExportDTO.setShelfLife(sku.getQualityHours().toString());
			//当前物品的替代物品集合
			List<SkuReplace> skuReplaces = skuReplaceList.stream().filter(skuReplace ->
				skuReplace.getSkuId().equals(sku.getSkuId())).collect(Collectors.toList());
			//当前物品的物品与企业集合
			List<SkuInc> skuIncs = skuIncList.stream().filter(skuInc ->
				skuInc.getSkuId().equals(sku.getSkuId())).collect(Collectors.toList());
			//当前物品货主
			Owner owner = ownerService.getById(sku.getWoId());
			//当前物品分类
			SkuType skuType = skuTypeService.getById(sku.getSkuTypeId());
			// 包装
			SkuPackage skuPackage = SkuPackageCache.getById(sku.getWspId());
			// 批属性
			SkuLot skuLot = skuLotService.getById(sku.getWslId());
			// 批属性验证
			SkuLotVal skuLotVal = skuLotValService1.getById(sku.getWslvId());
			//货主
			if (Func.isNotEmpty(owner)) {
				skuExportDTO.setOwnerCode(owner.getOwnerCode());
				skuExportDTO.setOwnerName(owner.getOwnerName());
			}
			//物品分类
			if (Func.isNotEmpty(skuType)) {
				skuExportDTO.setTypeCode(skuType.getTypeCode());
				skuExportDTO.setSkuType(skuType.getTypeName());
			}
			// 包装
			if (Func.isNotEmpty(skuPackage)) {
				skuExportDTO.setPackageName(skuPackage.getWspName());
			}
			//批属性标签
			if (Func.isNotEmpty(skuLot)) {
				skuExportDTO.setSkuLotCode(skuLot.getSkuLotCode());
				skuExportDTO.setSkuLot(skuLot.getSkuLotName());
			}
			//批属性规则
			if (Func.isNotEmpty(skuLotVal)) {
				skuExportDTO.setSkuLotVal(skuLotVal.getSkuLotValName());
			}
			//是否序列号物品
			skuExportDTO.setIsSn(SnEnum.YES.getIndex() == sku.getIsSn() ?
				SnEnum.YES.getName() : SnEnum.NO.getName());

			//替代物品与供应商集合的最大长度
			int maxLength = 1;
			if (Func.isNotEmpty(skuReplaces) || Func.isNotEmpty(skuIncs)) {
				maxLength = skuReplaces.size() > skuIncs.size() ? skuReplaces.size() : skuIncs.size();
			}
			//按照长的集合的长度循环
			for (int i = 0; i < maxLength; i++) {
				SkuReplace skuReplace = i < skuReplaces.size() ? skuReplaces.get(i) : null;
				SkuInc skuInc = i < skuIncs.size() ? skuIncs.get(i) : null;
				SkuExcel skuExcel = BeanUtil.copy(skuExportDTO, SkuExcel.class);
				//替代物品
				if (Func.isNotEmpty(skuReplace)) {
					// 被替代物品包装名称
					if (Func.isNotEmpty(skuPackage)) {
						skuExcel.setReplacePackageName1(skuPackage.getWspName());
					}
					// 被替代物品包装单位
					ISkuUmService skuUmService = SpringUtil.getBean(ISkuUmService.class);
					SkuUm skuUm = skuUmService.getById(skuReplace.getWsuId());
					if (Func.isNotEmpty(skuUm)) {
						skuExcel.setUnitCode(skuUm.getWsuCode());
						skuExcel.setUnitName(skuUm.getWsuName());
					}
					// 被替代品的数量
					skuExcel.setSkuCount(skuReplaces.get(i).getQty().stripTrailingZeros().toPlainString());
					// 替代物品信息
					Sku wsrepSku = super.getById(skuReplace.getWsrepSkuId());
					if (Func.isNotEmpty(wsrepSku)) {
						skuExcel.setReplaceSkuCode(wsrepSku.getSkuCode());
						skuExcel.setReplaceSkuName(wsrepSku.getSkuName());
						IOwnerService ownerService1 = SpringUtil.getBean(IOwnerService.class);
						Owner wsrepOwner = ownerService1.getById(wsrepSku.getWoId());
						if (Func.isNotEmpty(wsrepOwner)) {
							skuExcel.setReplaceOwnerCode(wsrepOwner.getOwnerCode());
							skuExcel.setReplaceOwnerName(wsrepOwner.getOwnerName());
						}
					}
					// 替代物品包装

					SkuPackage wsrepSkuPackage = SkuPackageCache.getById(skuReplace.getWsrepWspId());
					if (Func.isNotEmpty(wsrepSkuPackage)) {
						skuExcel.setReplacePackageName(wsrepSkuPackage.getWspName());
					}
					// 替代物品包装单位
					SkuUm wsrepSkuUm = skuUmService.getById(skuReplace.getWsrepWsuId());
					if (Func.isNotEmpty(wsrepSkuUm)) {
						skuExcel.setReplaceUnitCode(wsrepSkuUm.getWsuCode());
						skuExcel.setReplaceUnitName(wsrepSkuUm.getWsuName());
					}
					skuExcel.setReplaceSkuCount(skuReplaces.get(i).getWsrepQty().stripTrailingZeros().toPlainString());
				}
				//供应商
				if (Func.isNotEmpty(skuIncs) && Func.isNotEmpty(skuInc)) {
					IEnterpriseService enterpriseService = SpringUtil.getBean(IEnterpriseService.class);
					Enterprise enterprise = enterpriseService.getById(skuInc.getPeId());
					if (Func.isNotEmpty(enterprise)) {
						skuExcel.setEnterpriseCode(enterprise.getEnterpriseCode());
						skuExcel.setEnterpriseName(enterprise.getEnterpriseName());
					}
					SkuPackage incSkuPackage = SkuPackageCache.getById(skuInc.getWspId());
					if (Func.isNotEmpty(incSkuPackage)) {
						skuExcel.setEnterprisePackageName(incSkuPackage.getWspName());
					}
					skuExcel.setEnterpriseSkuName(skuInc.getSkuName());
					Field[] declaredFields = SkuInc.class.getDeclaredFields();
					for (Field field : declaredFields) {
						field.setAccessible(true);
						try {
							if (field.getName().contains("attribute") && Func.isNotEmpty(field.get(skuInc))) {
								String index = field.getName().substring("attribute".length(), field.getName().length());
								Method method = SkuExcel.class.getMethod("setExtendField" + index, String.class);
								method.invoke(skuExcel, field.get(skuInc));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
				skuExportList.add(skuExcel);
			}
		}
		return skuExportList;
	}

	/**
	 * 验证物品数据
	 *
	 * @param sku 物品
	 */

	protected void validData(Sku sku) {
		if (ObjectUtil.isEmpty(sku)) {
			throw new ServiceException("物品不能为空！");
		}
		// 验证：物品ID + 货主ID 的唯一性
		Sku skuQuery = new Sku();
		skuQuery.setSkuCode(sku.getSkuCode());
		skuQuery.setWoId(sku.getWoId());

		Sku findObj = super.getOne(Condition.getQueryWrapper(skuQuery));
		if (ObjectUtil.isNotEmpty(findObj) && !findObj.getSkuId().equals(sku.getSkuId())) {
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(sku.getWoId());
			if (ObjectUtil.isEmpty(owner)) {
				throw new ServiceException("货主不存在（ID：" + sku.getWoId() + "）！");
			}
			throw new ServiceException(
				"货主：" + owner.getOwnerName() + " 已存在物品编码：" + sku.getSkuCode() + " 的物品！");
		}
		// 如果该物品存在库存，则不允许修改序列号状态
		Sku sourceSku = super.getById(sku.getSkuId());
		if (Func.isNotEmpty(sourceSku) && Func.isNotEmpty(sourceSku.getIsSn())) {
			if (!sourceSku.getIsSn().equals(sku.getIsSn()) &&
				Func.isNotEmpty(stockService.getOne(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getSkuId, sku.getSkuId())
					.last("limit 1")))) {
				throw new ServiceException("存在库存的物品，不允许修改序列号管理状态！");
			}
		}
	}
}
