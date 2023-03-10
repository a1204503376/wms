package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.dto.SkuDTO;
import org.nodes.wms.core.basedata.dto.SkuIncDTO;
import org.nodes.wms.core.basedata.dto.SkuReplaceDTO;
import org.nodes.wms.core.basedata.entity.*;
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
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.strategy.entity.Outstock;
import org.nodes.wms.core.strategy.service.IInstockService;
import org.nodes.wms.core.strategy.service.IOutstockService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.*;
import org.nodes.wms.dao.basics.sku.enums.SnEnum;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotVal;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.putaway.entities.StInstock;
import org.nodes.wms.dao.stock.entities.Stock;
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
 * ?????? ???????????????
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
	 * ???????????????????????????
	 *
	 * @param sku ????????????
	 * @return ???????????????
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
			// ???????????????????????????
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
		// if (!NodesUtil.isAllNull(sku)&& skuQueryWrapper.isEmptyOfWhere()) {
		// skuQueryWrapper.apply("1 <> 1");
		// }
		return skuQueryWrapper;
	}

	@Override
	public SkuVO getDetail(SkuDTO sku) {
		Sku entity = super.getById(sku.getSkuId());

		if (Func.isEmpty(entity)) {
			throw new ServiceException("???????????????");
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
		skuVo.setAbcName(DictCache.getValue(DictCodeConstant.LOC_ABC, entity.getAbc()));
		return skuVo;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		List<Sku> skuList = super.listByIds(idList);
		for (Sku sku : skuList) {
			if (ObjectUtil.isNotEmpty(sku)) {
				continue;
			}
			// ???????????????????????????
			List<Stock> stockList = stockService.list(Condition.getQueryWrapper(new Stock())
					.lambda()
					.eq(Stock::getSkuId, sku.getSkuId()));
			if (Func.isNotEmpty(stockList)) {
				throw new ServiceException("?????????????????????????????????????????????" + sku.getSkuName() + " ??????");
			}
		}
		// ??????-?????????
		skuReplaceService.remove(Condition.getQueryWrapper(new SkuReplace()).lambda()
				.in(SkuReplace::getSkuId, idList));
		// ??????-????????????
		skuIncService.remove(Condition.getQueryWrapper(new SkuInc()).lambda()
				.in(SkuInc::getSkuId, idList));
		// ??????-????????????
		skuOutstockService.remove(Condition.getQueryWrapper(new SkuOutstock()).lambda()
				.in(SkuOutstock::getSkuId, idList));
		// ??????-????????????
		skuInstockService.remove(Condition.getQueryWrapper(new SkuInstock()).lambda()
				.in(SkuInstock::getSkuId, idList));
		boolean result = super.removeByIds(idList);
		if (result) {
			// super.removeByIds(idList);
		}
		return result;
	}

	@Override
	public boolean save(@Validated SkuDTO entity) {
		this.validData(entity);

		boolean result = super.save(entity);
		// ????????????
		if (ObjectUtil.isNotEmpty(entity.getSkuReplaceList())) {
			for (SkuReplace skuReplace : entity.getSkuReplaceList()) {
				Integer hasReplace = skuReplaceService.count(Condition.getQueryWrapper(new SkuReplace()).lambda()
						.eq(SkuReplace::getSkuId, skuReplace.getWsrepSkuId()));
				if (hasReplace > 0) {
					// ??????????????????????????????????????????????????????????????????????????????????????????
					Sku replaceSku = super.getById(skuReplace.getWsrepSkuId());
					if (ObjectUtil.isNotEmpty(replaceSku)) {
						throw new ServiceException(
								"????????????" + replaceSku.getSkuId() + " ????????????????????? ?????????????????????????????????");
					} else {
						throw new ServiceException(
								"??????ID??????" + skuReplace.getWsrepSkuId() + " ????????????????????? ?????????????????????????????????");
					}
				}
				skuReplace.setSkuId(entity.getSkuId());

				skuReplaceService.save(skuReplace);
			}
		}
		// ?????????????????????
		if (ObjectUtil.isNotEmpty(entity.getSkuIncList())) {
			for (SkuInc skuInc : entity.getSkuIncList()) {
				skuInc.setSkuId(entity.getSkuId());

				skuIncService.save(skuInc);
			}
		}
		if (result) {
			// super.saveOrUpdate(entity);
		}
		this.saveSkuInstock(entity);
		this.saveSkuOutstock(entity);
		return result;
	}

	protected boolean saveSkuInstock(Sku sku) {
		IInstockService instockService = SpringUtil.getBean(IInstockService.class);
		// ???????????????????????????
		/*
		 * Instock instock = InstockCache.list().stream().filter(u -> {
		 * return Func.equals(u.getIsDefault(), 1);
		 * }).findFirst().orElse(null);
		 */

		StInstock instock = instockService.list(Condition.getQueryWrapper(new StInstock())
				.lambda()
				.eq(StInstock::getIsDefault, 1)).stream().findFirst().orElse(null);
		if (Func.isEmpty(instock)) {
			// ????????????????????????????????????????????????????????????????????????
			return false;
		}
		// ???????????????????????????
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
		// ???????????????????????????
		IOutstockService outstockService = SpringUtil.getBean(IOutstockService.class);
		/*
		 * Outstock outstock = OutstockCache.list().stream().filter(u -> {
		 * return Func.equals(u.getIsDefault(), 1);
		 * }).findFirst().orElse(null);
		 */
		Outstock outstock = outstockService.list(Condition.getQueryWrapper(new Outstock())
				.lambda()
				.eq(Outstock::getIsDefault, 1)).stream().findFirst().orElse(null);
		if (Func.isEmpty(outstock)) {
			// ????????????????????????????????????????????????????????????????????????
			return false;
		}
		// ???????????????????????????
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
		// ??????????????????????????????id
		stockService.update(Wrappers.lambdaUpdate(Stock.class).eq(Stock::getSkuId, entity.getSkuId())
				.set(Stock::getWspId, entity.getWspId()));
		boolean result = super.updateById(entity);
		// ????????????
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
		// ?????????????????????
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
			// super.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean saveOrUpdate(SkuDTO entity) {
		if (Func.isEmpty(super.getIdVal(entity))) {
			Sku sku = getOne(Wrappers.lambdaQuery(Sku.class)
					.eq(Sku::getSkuCode, entity.getSkuCode()));
			if (Func.isNotEmpty(sku)) {
				throw new ServiceException("???????????????????????????????????????");
			}
			SkuPackage skuPackage = skuPackageService.getById(entity.getWspId());
			entity.setWspName(skuPackage.getWspName());
			return this.save(entity);
		} else {
			Sku sku = getOne(Wrappers.lambdaQuery(Sku.class)
					.ne(Sku::getSkuId, entity.getSkuId())
					.eq(Sku::getSkuCode, entity.getSkuCode()));
			if (Func.isNotEmpty(sku)) {
				throw new ServiceException("???????????????????????????????????????");
			}
			SkuPackage skuPackage = skuPackageService.getById(entity.getWspId());
			entity.setWspName(skuPackage.getWspName());
			return this.updateById(entity);
		}
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<Sku> skuList = this.list(Condition.getQueryWrapper(params, Sku.class));
		if (Func.isNotEmpty(skuList)) {
			List<SkuExcel> skuExportList = this.getSkuExportDTOList(skuList);
			ExcelUtil.export(response, "??????", "???????????????", skuExportList, SkuExcel.class);
		}
	}

	@Override
	public List<DataVerify> validExcel(List<SkuExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();
		ISkuUmService skuUmService = SpringUtil.getBean(ISkuUmService.class);
		if (Func.isNotEmpty(dataList)) {
			// ??????????????????
			List<String> skuCodeList = NodesUtil.toList(dataList, SkuExcel::getSkuCode);
			// List<Sku> skuList = SkuCache.listByCode(skuCodeList);
			List<Sku> skuList = super.list(Condition.getQueryWrapper(new Sku())
					.lambda()
					.in(Sku::getSkuCode, skuCodeList)).stream().collect(Collectors.toList());
			// ????????????????????????
			List<String> ownerCodeList = NodesUtil.toList(dataList, SkuExcel::getOwnerCode);
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			List<Owner> ownerList = ownerService.list().stream().filter(u -> {
				return ownerCodeList.contains(u.getOwnerCode());
			}).collect(Collectors.toList());
			// ????????????
			QueryWrapper<SkuType> stqw = new QueryWrapper<>();
			List<String> typeCodeList = NodesUtil.toList(dataList, SkuExcel::getTypeCode);
			stqw.lambda().in(SkuType::getTypeCode, typeCodeList);
			List<SkuType> skuTypeList = new ArrayList<>();
			if (Func.isNotEmpty(typeCodeList)) {
				skuTypeList = skuTypeService.list(stqw);
			}
			// ??????????????????
			QueryWrapper<SkuPackage> spqw = new QueryWrapper<>();
			List<String> wspNameList = NodesUtil.toList(dataList, SkuExcel::getPackageName);
			spqw.lambda().in(SkuPackage::getWspName, wspNameList);
			List<SkuPackage> skuePackageList = new ArrayList<>();
			if (Func.isNotEmpty(wspNameList)) {
				skuePackageList = skuPackageService.list(spqw);
			}
			// ???????????????
			QueryWrapper<SkuLot> slqw = new QueryWrapper<>();
			List<String> skuLotCodeList = NodesUtil.toList(dataList, SkuExcel::getSkuLotCode);
			slqw.lambda().in(SkuLot::getSkuLotCode, skuLotCodeList);
			List<SkuLot> skuLotList = new ArrayList<>();
			if (Func.isNotEmpty(skuLotCodeList)) {
				skuLotList = skuLotService.list(slqw);
			}
			// ???????????????
			QueryWrapper<SkuLotVal> slvqw = new QueryWrapper<>();
			List<String> skuLotValNameList = NodesUtil.toList(dataList, SkuExcel::getSkuLotVal);
			slvqw.lambda().in(SkuLotVal::getSkuLotValName, skuLotValNameList);
			List<SkuLotVal> skuLotValList = new ArrayList<>();
			if (Func.isNotEmpty(skuLotValNameList)) {
				skuLotValList = skuLotValService.list(slvqw);
			}
			// ???????????????????????????
			spqw = new QueryWrapper<>();
			List<SkuPackage> skuReplacePackageList = new ArrayList<>();
			List<SkuUm> skuReplaceUmList = new ArrayList<>();
			List<Owner> skuReplaceWsrepOwnerList = new ArrayList<>();
			List<Sku> skuReplaceWsrepSkuList = new ArrayList<>();
			List<SkuUm> skuReplaceWsrepUmList = new ArrayList<>();
			List<SkuPackage> skuReplaceWsrepPackageList = new ArrayList<>();
			List<String> replaceWspNameList = NodesUtil.toList(dataList, SkuExcel::getReplacePackageName1);
			if (Func.isNotEmpty(replaceWspNameList)) {
				spqw.lambda().in(SkuPackage::getWspName, replaceWspNameList);
				if (Func.isNotEmpty(replaceWspNameList)) {
					skuReplacePackageList = skuPackageService.list(spqw);
				}
				// ???????????????????????????
				List<String> replaceWsuCodeList = NodesUtil.toList(dataList, SkuExcel::getUnitCode);
				// List<SkuUm> skuReplaceUmList = SkuUmCache.listByCode(replaceWsuCodeList);
				skuReplaceUmList = skuUmService.list(Condition.getQueryWrapper(new SkuUm())
						.lambda()
						.in(SkuUm::getWsuCode, replaceWsuCodeList)).stream().collect(Collectors.toList());

				// ??????????????????
				List<String> replaceWsrepOwnerCodeList = getReplaceWsrepOwnerCodeList(dataList);
				skuReplaceWsrepOwnerList = ownerService.list().stream().filter(u -> {
					return replaceWsrepOwnerCodeList.contains(u.getOwnerCode());
				}).collect(Collectors.toList());

				// ??????????????????
				List<String> replaceWsrepSkuCodeList = NodesUtil.toList(
						dataList, SkuExcel::getReplaceSkuCode);
				// List<Sku> skuReplaceWsrepSkuList =
				// SkuCache.listByCode(replaceWsrepSkuCodeList);
				skuReplaceWsrepSkuList = super.list(Condition.getQueryWrapper(new Sku())
						.lambda()
						.in(Sku::getSkuCode, replaceWsrepSkuCodeList)).stream().collect(Collectors.toList());

				// ??????????????????
				spqw = new QueryWrapper<>();
				List<String> replaceWsrepWspNameList = NodesUtil.toList(
						dataList, SkuExcel::getReplacePackageName);
				spqw.lambda().in(SkuPackage::getWspName, replaceWsrepWspNameList);
				skuReplaceWsrepPackageList = new ArrayList<>();
				if (Func.isNotEmpty(replaceWsrepWspNameList)) {
					skuReplaceWsrepPackageList = skuPackageService.list(spqw);
				}

				// ????????????????????????
				List<String> replaceWsrepWsuCodeList = NodesUtil.toList(
						dataList, SkuExcel::getReplaceUnitCode);
				// List<SkuUm> skuReplaceWsrepUmList =
				// SkuUmCache.listByCode(replaceWsrepWsuCodeList);
				skuReplaceWsrepUmList = skuUmService.list(Condition.getQueryWrapper(new SkuUm())
						.lambda()
						.in(SkuUm::getWsuCode, replaceWsrepWsuCodeList)).stream().collect(Collectors.toList());
			} // if (Func.isNotEmpty(replaceWspNameList))

			// ???????????????????????????
			List<String> enterpriseCodeList = NodesUtil.toList(
					dataList, SkuExcel::getEnterpriseCode);
			// List<Enterprise> skuIncEnterpriseList =
			// EnterpriseCache.listByCode(enterpriseCodeList);
			List<Enterprise> skuIncEnterpriseList = new ArrayList<>();
			if (Func.isNotEmpty(enterpriseCodeList)) {
				IEnterpriseService enterpriseService = SpringUtil.getBean(IEnterpriseService.class);
				skuIncEnterpriseList = enterpriseService.list(Condition.getQueryWrapper(new Enterprise())
						.lambda()
						.in(Enterprise::getEnterpriseCode, enterpriseCodeList));
			}

			// ???????????????????????????
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
				skuDTO.setSkuSpec(skuExcel.getSkuSpec());
				skuDTO.setSkuNetWeight(
						Func.isEmpty(skuExcel.getSkuNetWeight())
								? null
								: new BigDecimal(skuExcel.getSkuNetWeight()));
				skuDTO.setSkuGrossWeight(
						Func.isEmpty(skuExcel.getSkuGrossWeight())
								? null
								: new BigDecimal(skuExcel.getSkuGrossWeight()));
				skuDTO.setSkuTareWeight(
						Func.isEmpty(skuExcel.getSkuTareWeight())
								? null
								: new BigDecimal(skuExcel.getSkuTareWeight()));
				skuDTO.setSkuVolume(
						Func.isEmpty(skuExcel.getSkuVolume())
								? null
								: new BigDecimal(skuExcel.getSkuVolume()));
				skuDTO.setSkuBarcodeList(
						Func.isEmpty(skuExcel.getSkuBarcodeList())
								? null
								: skuExcel.getSkuBarcodeList());
				skuDTO.setSkuRemark(skuExcel.getRemarks());
				Dict dict = DictCache.list(DictCodeConstant.INVENTORY_TYPE).stream().filter(u -> {
					return Func.equals(u.getDictValue(), skuExcel.getStorageType());
				}).findFirst().orElse(null);
				skuDTO.setSkuStorageType(Func.isEmpty(dict) ? null : dict.getDictKey());
				skuDTO.setQualityHours(
						Func.isEmpty(skuExcel.getShelfLife()) ? null : Integer.valueOf(skuExcel.getShelfLife()));
				// ??????????????????
				ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(skuExcel);
				List<Owner> owners = ownerList.stream()
						.filter(owner -> owner.getOwnerCode().equals(skuExcel.getOwnerCode()))
						.collect(Collectors.toList());
				if (Func.isNotEmpty(owners)) {
					skuDTO.setWoId(owners.get(0).getWoId());
					// ????????????????????????
					List<Sku> skus = skuList.stream().filter(sku -> sku.getSkuCode().equals(skuExcel.getSkuCode()) &&
							sku.getWoId().equals(owners.get(0).getWoId()))
							.collect(Collectors.toList());
					if (Func.isNotEmpty(skus)) {
						errorList.add(String.format("?????????%s,???????????????%s?????????????????????",
								skuExcel.getSkuCode(), skuExcel.getOwnerCode()));
					}
				} else {
					errorList.add(String.format("?????????%s??????????????????",
							skuExcel.getOwnerCode()));
				}
				// ????????????
				List<SkuType> skuTypes = skuTypeList.stream()
						.filter(skuType -> skuType.getTypeCode().equals(skuExcel.getTypeCode()))
						.collect(Collectors.toList());
				if (Func.isNotEmpty(skuTypes)) {
					skuDTO.setSkuTypeId(skuTypes.get(0).getSkuTypeId());
				} else {
					errorList.add(String.format("?????????%s????????????????????????",
							skuExcel.getTypeCode()));
				}
				// ????????????
				List<SkuPackage> skuPackages = skuePackageList.stream()
						.filter(skuPackage -> skuPackage.getWspName().equals(skuExcel.getPackageName()))
						.collect(Collectors.toList());
				if (Func.isNotEmpty(skuPackages)) {
					skuDTO.setWspId(skuPackages.get(0).getWspId());
				} else {
					errorList.add(String.format("?????????%s??????????????????", skuExcel.getPackageName()));
				}
				// ???????????????
				List<SkuLot> skuLots = skuLotList.stream()
						.filter(skuLot -> skuLot.getSkuLotCode().equals(skuExcel.getSkuLotCode()))
						.collect(Collectors.toList());
				if (Func.isNotEmpty(skuLots)) {
					skuDTO.setWslId(skuLots.get(0).getWslId());
				} else {
					errorList.add(String.format("?????????%s???????????????????????????",
							skuExcel.getSkuLotCode()));
				}
				// ?????????????????????
				List<SkuLotVal> skuLotVals = skuLotValList.stream()
						.filter(skuLotVal -> skuLotVal.getSkuLotValName().equals(skuExcel.getSkuLotVal()))
						.collect(Collectors.toList());
				if (Func.isNotEmpty(skuLotVals)) {
					skuDTO.setWslvId(skuLotVals.get(0).getWslvId());
				} else {
					errorList.add(String.format("?????????%s?????????????????????????????????",
							skuExcel.getSkuLotVal()));
				}
				// ?????????????????????
				skuDTO.setIsSn(
						SnEnum.YES.getName().equals(skuExcel.getIsSn()) ? SnEnum.YES.getIndex() : SnEnum.NO.getIndex());
				// ????????????bom ????????? 0???1 ?????????
				if (Func.isNotEmpty(skuExcel.getHasBom())) {
					if (!("0".equals(skuExcel.getHasBom()) || "1".equals(skuExcel.getHasBom()))) {
						throw new ServiceException("???????????????????????????bom?????????0???1????????????");
					} else {
						skuDTO.setHasBom(Integer.parseInt(skuExcel.getHasBom()));
					}
				}
				if (Func.isNotEmpty(skuExcel.getReplaceSkuCode())) {
					SkuReplaceDTO skuReplaceDTO = new SkuReplaceDTO();
					// ????????????
					skuReplaceDTO.setReplaceSkuCode(skuExcel.getSkuCode());// ???????????????????????????
					skuReplaceDTO.setReplaceSkuName(skuExcel.getSkuName());// ???????????????????????????
					skuReplaceDTO.setReplaceOwnerCode(skuExcel.getOwnerCode());// ???????????????????????????
					skuReplaceDTO.setReplaceOwnerName(skuExcel.getOwnerName());// ???????????????????????????
					skuReplaceDTO.setQty(new BigDecimal(skuExcel.getSkuCount())); // ?????????????????????
					skuReplaceDTO.setWsrepQty(new BigDecimal(skuExcel.getReplaceSkuCount()));// ??????????????????
					// ?????????????????????
					List<SkuPackage> skuReplacePackages = skuReplacePackageList.stream()
							.filter(skuReplacePackage -> skuReplacePackage.getWspName()
									.equals(skuExcel.getReplacePackageName1()))
							.collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplacePackages)) {
						skuReplaceDTO.setWspId(skuReplacePackages.get(0).getWspId());
					} else {
						errorList.add(String.format("?????????%s?????????????????????????????????",
								skuExcel.getReplacePackageName1()));
					}
					// ?????????????????????
					List<SkuUm> skuReplaceUms = skuReplaceUmList.stream()
							.filter(skuReplaceUm -> skuReplaceUm.getWsuCode().equals(skuExcel.getUnitCode()))
							.collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplaceUms)) {
						skuReplaceDTO.setWsuId(skuReplaceUms.get(0).getWsuId());
					} else {
						errorList.add(String.format("?????????%s?????????????????????????????????",
								skuExcel.getUnitCode()));
					}

					// ??????????????????
					List<Owner> skuReplaceWsrepOwners = skuReplaceWsrepOwnerList.stream()
							.filter(skuReplaceWsrepOwner -> skuReplaceWsrepOwner.getOwnerCode()
									.equals(skuExcel.getReplaceOwnerCode()))
							.collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplaceWsrepOwners)) {
						Long woId = skuReplaceWsrepOwners.get(0).getWoId();
						// ????????????
						List<Sku> skuReplaceWsrepSkus = skuReplaceWsrepSkuList.stream()
								.filter(skuReplaceWsrepSku -> skuReplaceWsrepSku.getSkuCode()
										.equals(skuExcel.getReplaceSkuCode()) &&
										skuReplaceWsrepSku.getWoId().equals(woId))
								.collect(Collectors.toList());
						if (Func.isNotEmpty(skuReplaceWsrepSkus)) {
							skuReplaceDTO.setWsrepSkuId(skuReplaceWsrepSkus.get(0).getSkuId());
						} else {
							errorList.add(String.format("?????????????????????%s,???????????????%s????????????????????????",
									skuExcel.getReplaceSkuCode(), skuExcel.getReplaceOwnerCode()));
						}

					} else {
						errorList.add(String.format("?????????%s??????????????????????????????",
								skuExcel.getReplaceOwnerCode()));
						// ????????????
						List<Sku> skuReplaceWsrepSkus = skuReplaceWsrepSkuList.stream()
								.filter(skuReplaceWsrepSku -> skuReplaceWsrepSku.getSkuCode()
										.equals(skuExcel.getReplaceSkuCode()))
								.collect(Collectors.toList());
						if (Func.isEmpty(skuReplaceWsrepSkus)) {
							errorList.add(String.format("?????????%s????????????????????????",
									skuExcel.getReplaceSkuCode()));
						}
					}
					// ??????????????????
					List<SkuPackage> skuReplaceWsrepPackages = skuReplaceWsrepPackageList.stream()
							.filter(skuReplaceWsrepPackage -> skuReplaceWsrepPackage.getWspName()
									.equals(skuExcel.getReplacePackageName()))
							.collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplaceWsrepPackages)) {
						skuReplaceDTO.setWsrepWspId(skuReplaceWsrepPackages.get(0).getWspId());
					} else {
						errorList.add(String.format("?????????%s??????????????????????????????",
								skuExcel.getReplacePackageName()));
					}
					List<SkuUm> skuReplaceWsrepUms = skuReplaceWsrepUmList.stream().filter(
							skuReplaceWsrepUm -> skuReplaceWsrepUm.getWsuCode().equals(skuExcel.getReplaceUnitCode()))
							.collect(Collectors.toList());
					if (Func.isNotEmpty(skuReplaceWsrepUms)) {
						skuReplaceDTO.setWsrepWsuId(skuReplaceWsrepUms.get(0).getWsuId());
					} else {
						errorList.add(String.format("?????????%s??????????????????????????????",
								skuExcel.getReplaceUnitCode()));
					}
					skuDTO.getSkuReplaceList().add(skuReplaceDTO);
				}
				if (Func.isNotEmpty(skuExcel.getEnterpriseName())) {
					SkuIncDTO skuIncDTO = new SkuIncDTO();
					// Sku sku = SkuCache.getSku(skuExcel.getEnterpriseSkuName(),
					// owners.get(0).getWoId());
					Sku sku = super.list(Condition.getQueryWrapper(new Sku())
							.lambda()
							.eq(Sku::getSkuName, skuExcel.getEnterpriseSkuName())
							.eq(Sku::getWoId, owners.get(0).getWoId())).stream().findFirst().orElse(null);
					if (Func.isNotEmpty(sku)) {
						skuIncDTO.setReplaceSkuName(sku.getSkuName()); // ??????????????????
						skuIncDTO.setReplaceSkuCode(sku.getSkuCode());
						skuIncDTO.setSkuId(sku.getSkuId());
						skuIncDTO.setSkuName(sku.getSkuName());
					} else {
						errorList.add(String.format("????????????[%s]????????????",
								skuExcel.getEnterpriseSkuName()));
					}
					skuIncDTO.setReplaceOwnerCode(skuExcel.getOwnerCode()); // ????????????????????????
					skuIncDTO.setReplaceOwnerName(skuExcel.getOwnerName()); // ????????????????????????
					Field[] declaredFields = SkuExcel.class.getDeclaredFields();
					for (Field field : declaredFields) {
						field.setAccessible(true);
						try {
							if (field.getName().contains("extendField") && Func.isNotEmpty(field.get(skuExcel))) {
								String index1 = field.getName().substring("extendField".length(),
										field.getName().length());
								Method method = SkuIncDTO.class.getMethod("setAttribute" + index1, String.class);
								method.invoke(skuIncDTO, field.get(skuExcel));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					// ?????????
					List<Enterprise> skuIncEnterprises = skuIncEnterpriseList.stream()
							.filter(skuIncEnterprise -> skuIncEnterprise.getEnterpriseCode()
									.equals(skuExcel.getEnterpriseCode()))
							.collect(Collectors.toList());
					if (Func.isNotEmpty(skuIncEnterprises)) {
						skuIncDTO.setPeId(skuIncEnterprises.get(0).getPeId());
					} else {
						errorList.add(String.format("?????????%s?????????????????????",
								skuExcel.getEnterpriseCode()));
					}
					// ???????????????
					List<SkuPackage> skuIncPackages = skuIncPackageList.stream().filter(
							skuIncPackage -> skuIncPackage.getWspName().equals(skuExcel.getEnterprisePackageName()))
							.collect(Collectors.toList());
					if (Func.isNotEmpty(skuIncPackages)) {
						skuIncDTO.setWspId(skuIncPackages.get(0).getWspId());
					} else {
						errorList.add(String.format("?????????%s???????????????????????????",
								skuExcel.getEnterprisePackageName()));
					}
					// ?????????????????????
					skuDTO.getSkuIncList().add(skuIncDTO);
				}
				if (validResult.hasErrors()) {
					errorList.add(StringUtil.join(validResult.getAllErrors()));
				}
				if (Func.isNotEmpty(errorList)) {
					dataVerify.setMessage(StringUtil.join(errorList));
				} else {
					if (cache.containsKey(cacheKey)) {
						// ??????????????????????????????
						cache.get(cacheKey).getSkuIncList().addAll(skuDTO.getSkuIncList());
						cache.get(cacheKey).getSkuReplaceList().addAll(skuDTO.getSkuReplaceList());
					} else {
						// ???????????????map???
						cache.put(cacheKey, skuDTO);
					}
					dataVerify.setCacheKey(cacheKey);
				}
				dataVerifyList.add(dataVerify);
				index++;
			}
			if (Func.isNotEmpty(cache)) {
				// ???????????????redis?????????
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
			throw new ServiceException("???????????????????????????");
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
				// super.saveOrUpdate(skuDTO);
			}
		}
		return true;
	}

	private List<SkuExcel> getSkuExportDTOList(List<Sku> skuList) {
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
		ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
		ISkuLotValService skuLotValService1 = SpringUtil.getBean(ISkuLotValService.class);
		// ????????????????????????????????????
		List<SkuExcel> skuExportList = new ArrayList<>();
		// ???????????????SKUID??????
		List<Long> skuIdsList = NodesUtil.toList(skuList, Sku::getSkuId);
		// ?????????????????????????????????????????????
		QueryWrapper<SkuReplace> srqw = new QueryWrapper<>();
		srqw.lambda().in(SkuReplace::getSkuId, skuIdsList);
		List<SkuReplace> skuReplaceList = new ArrayList<>();
		if (Func.isNotEmpty(skuIdsList)) {
			skuReplaceList = skuReplaceService.list(srqw);
		}
		// ??????????????????????????????????????????
		QueryWrapper<SkuInc> siqw = new QueryWrapper<>();
		siqw.lambda().in(SkuInc::getSkuId, skuIdsList);
		List<SkuInc> skuIncList = new ArrayList<>();
		if (Func.isNotEmpty(skuIdsList)) {
			skuIncList = skuIncService.list(siqw);
		}
		// ????????????????????????????????? ??????????????????
		for (Sku sku : skuList) {
			// ???????????????
			SkuExcel skuExportDTO = new SkuExcel();
			skuExportDTO.setSkuCode(sku.getSkuCode());
			skuExportDTO.setSkuName(sku.getSkuName());
			skuExportDTO.setSkuNameS(sku.getSkuNameS());
			skuExportDTO.setSkuSpec(sku.getSkuSpec());
			// ???????????????????????????
			skuExportDTO.setAbc(DictCache.getValue(DictCodeConstant.LOC_ABC, sku.getAbc()));
//			skuExportDTO.setSkuGrossWeight(sku.getSkuGrossWeight().stripTrailingZeros().toPlainString());
//			skuExportDTO.setSkuNetWeight(sku.getSkuNetWeight().stripTrailingZeros().toPlainString());
//			skuExportDTO.setSkuTareWeight(sku.getSkuTareWeight().stripTrailingZeros().toPlainString());
//			skuExportDTO.setSkuVolume(sku.getSkuVolume().stripTrailingZeros().toPlainString());
//			skuExportDTO.setStorageType(DictCache.getValue(DictCodeConstant.INVENTORY_TYPE, sku.getSkuStorageType()));
			skuExportDTO.setRemarks(sku.getSkuRemark());
			skuExportDTO.setSkuBarcodeList(sku.getSkuBarcodeList());
//			skuExportDTO.setShelfLife(sku.getQualityHours().toString());
			// ?????????????????????????????????
			List<SkuReplace> skuReplaces = skuReplaceList.stream()
					.filter(skuReplace -> skuReplace.getSkuId().equals(sku.getSkuId())).collect(Collectors.toList());
			// ????????????????????????????????????
			List<SkuInc> skuIncs = skuIncList.stream().filter(skuInc -> skuInc.getSkuId().equals(sku.getSkuId()))
					.collect(Collectors.toList());
			// ??????????????????
			Owner owner = ownerService.getById(sku.getWoId());
			// ??????????????????
			SkuType skuType = skuTypeService.getById(sku.getSkuTypeId());
			// ??????
			SkuPackage skuPackage = SkuPackageCache.getById(sku.getWspId());
			// ?????????
			SkuLot skuLot = skuLotService.getById(sku.getWslId());
			// ???????????????
			SkuLotVal skuLotVal = skuLotValService1.getById(sku.getWslvId());
			// ??????
			if (Func.isNotEmpty(owner)) {
				skuExportDTO.setOwnerCode(owner.getOwnerCode());
				skuExportDTO.setOwnerName(owner.getOwnerName());
			}
			// ????????????
			if (Func.isNotEmpty(skuType)) {
				skuExportDTO.setTypeCode(skuType.getTypeCode());
				skuExportDTO.setSkuType(skuType.getTypeName());
			}
			// ??????
			if (Func.isNotEmpty(skuPackage)) {
				skuExportDTO.setPackageName(skuPackage.getWspName());
			}
			// ???????????????
			if (Func.isNotEmpty(skuLot)) {
				skuExportDTO.setSkuLotCode(skuLot.getSkuLotCode());
				skuExportDTO.setSkuLot(skuLot.getSkuLotName());
			}
			// ???????????????
			if (Func.isNotEmpty(skuLotVal)) {
				skuExportDTO.setSkuLotVal(skuLotVal.getSkuLotValName());
			}
			// ?????????????????????
			skuExportDTO.setIsSn(SnEnum.YES.getIndex().equals(sku.getIsSn()) ? SnEnum.YES.getName() : SnEnum.NO.getName());

			// ?????????????????????????????????????????????
			int maxLength = 1;
			if (Func.isNotEmpty(skuReplaces) || Func.isNotEmpty(skuIncs)) {
				maxLength = skuReplaces.size() > skuIncs.size() ? skuReplaces.size() : skuIncs.size();
			}
			// ?????????????????????????????????
			for (int i = 0; i < maxLength; i++) {
				SkuReplace skuReplace = i < skuReplaces.size() ? skuReplaces.get(i) : null;
				SkuInc skuInc = i < skuIncs.size() ? skuIncs.get(i) : null;
				SkuExcel skuExcel = BeanUtil.copy(skuExportDTO, SkuExcel.class);
				// ????????????
				if (Func.isNotEmpty(skuReplace)) {
					// ???????????????????????????
					if (Func.isNotEmpty(skuPackage)) {
						skuExcel.setReplacePackageName1(skuPackage.getWspName());
					}
					// ???????????????????????????
					ISkuUmService skuUmService = SpringUtil.getBean(ISkuUmService.class);
					SkuUm skuUm = skuUmService.getById(skuReplace.getWsuId());
					if (Func.isNotEmpty(skuUm)) {
						skuExcel.setUnitCode(skuUm.getWsuCode());
						skuExcel.setUnitName(skuUm.getWsuName());
					}
					// ?????????????????????
					skuExcel.setSkuCount(skuReplaces.get(i).getQty().stripTrailingZeros().toPlainString());
					// ??????????????????
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
					// ??????????????????

					SkuPackage wsrepSkuPackage = SkuPackageCache.getById(skuReplace.getWsrepWspId());
					if (Func.isNotEmpty(wsrepSkuPackage)) {
						skuExcel.setReplacePackageName(wsrepSkuPackage.getWspName());
					}
					// ????????????????????????
					SkuUm wsrepSkuUm = skuUmService.getById(skuReplace.getWsrepWsuId());
					if (Func.isNotEmpty(wsrepSkuUm)) {
						skuExcel.setReplaceUnitCode(wsrepSkuUm.getWsuCode());
						skuExcel.setReplaceUnitName(wsrepSkuUm.getWsuName());
					}
					skuExcel.setReplaceSkuCount(skuReplaces.get(i).getWsrepQty().stripTrailingZeros().toPlainString());
				}
				// ?????????
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
								String index = field.getName().substring("attribute".length(),
										field.getName().length());
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
	 * ??????????????????
	 *
	 * @param sku ??????
	 */

	protected void validData(Sku sku) {
		if (ObjectUtil.isEmpty(sku)) {
			throw new ServiceException("?????????????????????");
		}
		// ???????????????ID + ??????ID ????????????
		Sku skuQuery = new Sku();
		skuQuery.setSkuCode(sku.getSkuCode());
		skuQuery.setWoId(sku.getWoId());

		Sku findObj = super.getOne(Condition.getQueryWrapper(skuQuery));
		if (ObjectUtil.isNotEmpty(findObj) && !findObj.getSkuId().equals(sku.getSkuId())) {
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(sku.getWoId());
			if (ObjectUtil.isEmpty(owner)) {
				throw new ServiceException("??????????????????ID???" + sku.getWoId() + "??????");
			}
			throw new ServiceException(
					"?????????" + owner.getOwnerName() + " ????????????????????????" + sku.getSkuCode() + " ????????????");
		}
		// ???????????????????????????????????????????????????????????????
		Sku sourceSku = super.getById(sku.getSkuId());
		if (Func.isNotEmpty(sourceSku) && Func.isNotEmpty(sourceSku.getIsSn())) {
			if (!sourceSku.getIsSn().equals(sku.getIsSn()) &&
					Func.isNotEmpty(stockService.getOne(Condition.getQueryWrapper(new Stock()).lambda()
							.eq(Stock::getSkuId, sku.getSkuId())
							.last("limit 1")))) {
				throw new ServiceException("???????????????????????????????????????????????????????????????");
			}
		}
	}
}
