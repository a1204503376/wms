package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.dto.SkuPackageDTO;
import org.nodes.wms.core.basedata.dto.SkuPackageDetailDTO;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.core.basedata.excel.SkuPackageExcel;
import org.nodes.wms.core.basedata.mapper.SkuPackageMapper;
import org.nodes.wms.core.basedata.service.ISkuPackageDetailService;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.nodes.wms.core.basedata.vo.SkuPackageDetailVO;
import org.nodes.wms.core.basedata.wrapper.SkuPackageDetailWrapper;
import org.nodes.wms.core.basedata.wrapper.SkuPackageWrapper;
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

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ??????ID ???????????????
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuPackageServiceImpl<M extends SkuPackageMapper, T extends SkuPackage>
	extends BaseServiceImpl<SkuPackageMapper, SkuPackage>
	implements ISkuPackageService {

	@Autowired
	ISkuPackageDetailService skuPackageDetailService;

	@Override
	public IPage<SkuPackage> selectPage(IPage<SkuPackage> page, SkuPackageDTO skuPackage) {
		return super.page(page, this.getQueryWrapper(skuPackage));
	}

	@Override
	public List<SkuPackage> selectList(SkuPackageDTO skuPackage) {
		return super.list(this.getQueryWrapper(skuPackage));
	}

	protected QueryWrapper<SkuPackage> getQueryWrapper(SkuPackageDTO skuPackage) {
		QueryWrapper<SkuPackage> skuPackageWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(skuPackage.getWspName())) {
			skuPackageWrapper.like("wsp_name", skuPackage.getWspName());
		}
		if (Func.isNotEmpty(skuPackage.getOnlinePackage())) {
			skuPackageWrapper.like("online_package", skuPackage.getOnlinePackage());
		}
		if (Func.isNotEmpty(skuPackage.getPackageType())) {
			skuPackageWrapper.eq("package_type", skuPackage.getPackageType());
		}
		if (ObjectUtil.isNotEmpty(skuPackage.getWspIds())) {
			skuPackageWrapper.in("wsp_id", Func.toLongList(skuPackage.getWspIds()));
		}
		return skuPackageWrapper;
	}

	/**
	 * ??????????????????
	 *
	 * @param skupackageDTO
	 */
	protected void verifyPackageDetail(SkuPackageDTO skupackageDTO) {

		//??????
		List<SkuPackageDetailDTO> list = skupackageDTO.getSkuPackageDetailDTOList();
		if (ObjectUtil.isEmpty(list)) {
			throw new ServiceException("????????????????????????,?????????????????????");
		}

		QueryWrapper<SkuPackage> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("wsp_name", skupackageDTO.getWspName());
		queryWrapper.ne("wsp_id", skupackageDTO.getWspId());
		if (super.count(queryWrapper) > 0) {
			throw new ServiceException("???????????????" + skupackageDTO.getWspName() + " ????????????");
		}

		//???????????????????????????
		boolean ifBase = false;
		int countBase = 0;
		int[] levels = new int[list.size()];
		int i = 0;
		for (SkuPackageDetailDTO detailDTO : list) {
			if (detailDTO.getConvertQty() <= 0) {
				throw new ServiceException("??????????????????????????????");
			}
			if (Func.equals(1, detailDTO.getSkuLevel())) {
				ifBase = true; // ???????????????????????????
				countBase++; // ??????????????????????????????
				if (detailDTO.getConvertQty() > 1) {
					throw new ServiceException("??????????????????????????????????????????1");
				}
			}
			// ???????????????????????????
			if (Arrays.binarySearch(levels, detailDTO.getSkuLevel()) > 0) {
				throw new ServiceException("?????????????????????????????????");
			} else {
				levels[i] = detailDTO.getSkuLevel();
			}
			String skuLevel = DictCache.getValue(DictCodeConstant.SKU_LEVEL, detailDTO.getSkuLevel());
			if (Func.isEmpty(skuLevel)) {
				throw new ServiceException("???????????????,?????????:1.??????????????????;10.?????????;20.???;30.??????");
			}
			i++;
		}
		if (countBase > 1) {
			throw new ServiceException("????????????????????????????????????");
		}
		if (!ifBase) {
			throw new ServiceException("??????????????????????????????");
		}
		//????????????????????????
		Collections.sort(list, (SkuPackageDetailDTO s1, SkuPackageDetailDTO s2) -> {
			return s2.getSkuLevel().compareTo(s1.getSkuLevel());
		});
		//????????????????????????
		int converQty = 0;
		for (SkuPackageDetailDTO detailDTO : list) {
			if (0 == converQty) {
				converQty = detailDTO.getConvertQty();
			} else {
				if (converQty == detailDTO.getConvertQty())
					throw new ServiceException("????????????????????????");
				converQty = detailDTO.getConvertQty();
			}
		}
	}

	@Override
	public boolean updateById(SkuPackageDTO entity) {
		if (Func.isEmpty(super.getById(entity.getWspId()))) {
			throw new ServiceException("?????????????????????:wspId");
		}
		this.verifyPackageDetail(entity);
		//???????????????????????????
		List<SkuPackageDetailDTO> packageDetailDeleteList = entity.getPageDeletedList();
		//??????????????????
		if (Func.isNotEmpty(packageDetailDeleteList)) {
			for (SkuPackageDetailDTO detailDTO : packageDetailDeleteList) {
				skuPackageDetailService.removeById(detailDTO.getWspdId());
			}
		}
		//????????????????????????????????????
		List<SkuPackageDetailDTO> packageDetailList = entity.getSkuPackageDetailDTOList()
			.stream()
			.sorted(Comparator.comparing(SkuPackageDetailDTO::getSkuLevel)).collect(Collectors.toList());
		;
		if (Func.isEmpty(packageDetailList)) {
			throw new ServiceException("????????????????????????");
		}
		SkuPackageDetailDTO defaultDetail = packageDetailList.get(0);
		//????????????????????????
		Map<Integer, Object> map = new HashMap<>();
		for (SkuPackageDetailDTO detailDTO : packageDetailList) {
			//??????map.get()?????????????????????
			if (Func.isEmpty(map.get(detailDTO.getSkuLevel()))) {
				map.put(detailDTO.getSkuLevel(), detailDTO);
			} else {
				String skuLevelDesc = DictCache.getValue(DictCodeConstant.SKU_LEVEL, detailDTO.getSkuLevel());
				throw new ServiceException(
					"?????????" + entity.getWspName() + " ???????????????" + skuLevelDesc + "???????????????");
			}
		}

		//??????????????????????????????
		packageDetailList = packageDetailList.stream().sorted(
			Comparator.comparing(SkuPackageDetailDTO::getSkuLevel).reversed()).collect(Collectors.toList());
		SkuPackageDetailDTO maxPackageDetail = packageDetailList.get(0);
		//??????
		StringBuffer spec = new StringBuffer(StringPool.ONE + maxPackageDetail.getWsuName());
		for (SkuPackageDetailDTO detailDTO : packageDetailList) {
			//??????id???????????????????????????
			SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(
				detailDTO.getWspId(), detailDTO.getSkuLevel());

			//???????????????????????????
			if (Func.isEmpty(detailDTO.getWspdId())) { // ??????
				//????????????id???????????????????????????
				if (Func.isNotEmpty(packageDetail)) { // ???????????????
					String skuLevelDesc = DictCache.getValue(DictCodeConstant.SKU_LEVEL, detailDTO.getSkuLevel());
					throw new ServiceException(
						"?????????" + entity.getWspName() + " ???????????????" + skuLevelDesc + "???????????????");
				}
			}
			detailDTO.setSkuSpec(String.format("%s%s-%s%s",
				defaultDetail.getConvertQty(), detailDTO.getWsuName(),
				detailDTO.getConvertQty(), defaultDetail.getWsuName()));
			detailDTO.setWspId(entity.getWspId());
			skuPackageDetailService.saveOrUpdate(detailDTO);
			if (detailDTO.getSkuLevel().equals(maxPackageDetail.getSkuLevel())) {
				continue;
			}
			spec.append("-")
				.append(maxPackageDetail.getConvertQty() / detailDTO.getConvertQty())
				.append(detailDTO.getWsuName());
		}

		entity.setSpec(spec.toString());
		return super.updateById(entity);
	}

	@Override
	public boolean save(SkuPackageDTO entity) {
		if (Func.isNotEmpty(super.getById(entity.getWspId()))) {
			throw new ServiceException("????????????????????????");
		}
		verifyPackageDetail(entity);

		//??????????????????????????????
		SkuPackage param = new SkuPackage();
		param.setWspName(entity.getWspName());
		if (super.count(Condition.getQueryWrapper(param).lambda()
			.eq(SkuPackage::getWspName, entity.getWspName()).or(i -> i.eq(SkuPackage::getIsDeleted, 1))) > 0) {
			throw new ServiceException("????????????????????????");
		}
		if (super.save(entity)) {
			//super.saveOrUpdate(entity);
		}
		List<Integer> specList = new ArrayList();
		if (ObjectUtil.isNotEmpty(entity.getSkuPackageDetailDTOList())) {
			// ?????????????????????
			List<SkuPackageDetailDTO> packageDetailList = entity.getSkuPackageDetailDTOList()
				.stream()
				.sorted(Comparator.comparing(SkuPackageDetailDTO::getSkuLevel).reversed()).collect(Collectors.toList());
			SkuPackageDetailDTO minSkuPackageDetail = packageDetailList.get(packageDetailList.size() - 1);
			SkuPackageDetailDTO maxSkuPackageDetail = packageDetailList.get(0);
			StringBuffer specBuffer = new StringBuffer(StringPool.ONE + maxSkuPackageDetail.getWsuName());
			for (SkuPackageDetailDTO packageDetail : packageDetailList) {
				if (Func.isNotEmpty(packageDetail.getWspdId())) {
					if (Func.isNotEmpty(SkuPackageDetailCache.getById(packageDetail.getWspdId())))
						throw new ServiceException("??????????????????????????????");
				}

				ISkuUmService skuUmService = SpringUtil.getBean(ISkuUmService.class);
				SkuUm skuUm = skuUmService.getById(packageDetail.getWsuId());
				if (Func.isNotEmpty(skuUm)) {
					packageDetail.setWsuCode(skuUm.getWsuCode());
				} else {
					//skuUm = skuUmService.getByCode(packageDetail.getWsuCode());
					skuUm = skuUmService.list(Condition.getQueryWrapper(new SkuUm())
						.lambda()
						.eq(SkuUm::getWsuCode, packageDetail.getWsuCode())
					).stream().findFirst().orElse(null);
					if (Func.isNotEmpty(skuUm)) {
						packageDetail.setWsuId(skuUm.getWsuId());
						packageDetail.setWsuCode(skuUm.getWsuCode());
						packageDetail.setWsuName(skuUm.getWsuName());
					} else {
						throw new ServiceException(StringUtil.format(
							"????????????[%s]?????????", packageDetail.getWsuCode()));
					}
				}
				//????????????
				String spec = String.format("%s%s-%s%s",
					minSkuPackageDetail.getConvertQty(), packageDetail.getWsuName(),
					packageDetail.getConvertQty(), minSkuPackageDetail.getWsuName());
				packageDetail.setSkuSpec(spec);
				packageDetail.setWspId(entity.getWspId());
				if (!skuPackageDetailService.saveOrUpdate(packageDetail)) {
					throw new ServiceException("???????????????????????????");
				}
				specList.add(packageDetail.getConvertQty());
				if (packageDetail.getSkuLevel().equals(maxSkuPackageDetail.getSkuLevel())) {
					continue;
				}
				specBuffer.append(String.format(
					"-%s%s",
					(maxSkuPackageDetail.getConvertQty() / packageDetail.getConvertQty()),
					packageDetail.getWsuName()));
			}
			// ??????????????????
			entity.setSpec(specBuffer.toString());
			if (super.updateById(entity)) {
				//super.saveOrUpdate(entity);
			}
		}

		return true;
	}

	@Override
	public boolean saveOrUpdate(SkuPackageDTO entity) {
		if (Func.isEmpty(super.getIdVal(entity))) {
			return this.save(entity);
		} else {
			return this.updateById(entity);
		}
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<SkuPackage> skuList = this.list(Condition.getQueryWrapper(params, SkuPackage.class));
		if (Func.isNotEmpty(skuList)) {
			List<SkuPackageExcel> skuPackageExcels = this.getSkuPackageExportDTOList(skuList);
			ExcelUtil.export(response, "????????????", "???????????????", skuPackageExcels, SkuPackageExcel.class);
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param name ????????????
	 * @return ????????????
	 */
	public SkuPackage getByName(String name) {
		SkuPackage skuPackage = new SkuPackage();
		skuPackage.setWspName(name);
		return getOne(Condition.getQueryWrapper(skuPackage));
	}

	@Override
	public List<DataVerify> validExcel(List<SkuPackageExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();

		Map<String, SkuPackageDTO> cache = new HashMap<>();
		int index = 1;
		for (SkuPackageExcel skuPackageExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// ?????????DTO
			SkuPackageDTO skuPackageDTO = SkuPackageWrapper.build().entityDTO(skuPackageExcel);

			// ??????????????????
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(skuPackageDTO);
			// ???????????????????????????
			SkuPackage findObj = this.getByName(skuPackageDTO.getWspName());
			if (ObjectUtil.isNotEmpty(findObj)) {
				validResult.addError(
					"wspName",
					"????????????" + skuPackageDTO.getWspName() + "?????????");
			}
			if (Func.isNotEmpty(skuPackageDTO.getSkuPackageDetailDTOList()) && !NodesUtil.isAllNull(skuPackageDTO.getSkuPackageDetailDTOList().get(0))) {
				ValidationUtil.ValidResult validResult1 = ValidationUtil.validateBean(skuPackageDTO.getSkuPackageDetailDTOList().get(0), Excel.class);
				validResult.getAllErrors().addAll(validResult1.getAllErrors());
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				if (cache.containsKey(skuPackageExcel.getWspName())) {
					// ??????????????????????????????
					cache.get(skuPackageDTO.getWspName()).getSkuPackageDetailDTOList()
						.addAll(skuPackageDTO.getSkuPackageDetailDTOList());
				} else {
					// ???????????????map???
					cache.put(skuPackageDTO.getWspName(), skuPackageDTO);
				}
				dataVerify.setCacheKey(skuPackageDTO.getWspName());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// ???????????????redis?????????
			for (String code : cache.keySet()) {
				SkuPackageCache.put(code, cache.get(code));
			}
		}

		return dataVerifyList;
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
			SkuPackageDTO skuPackageDTO = SkuPackageCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(skuPackageDTO)) {
				continue;
			}
			if (this.saveOrUpdate(skuPackageDTO)) {
				SkuPackageCache.remove(dataVerify.getCacheKey());
				//super.saveOrUpdate(skuPackageDTO);
			}
		}
		return true;
	}

	/**
	 * ????????????????????????????????????
	 *
	 * @param skuPackageList
	 * @return
	 */
	private List<SkuPackageExcel> getSkuPackageExportDTOList(List<SkuPackage> skuPackageList) {
		// ?????????????????????????????????
		List<SkuPackageExcel> skuPackageExportList = new ArrayList<>();
		List<Long> wspIdList = NodesUtil.toList(skuPackageList, SkuPackage::getWspId);
		// ????????????????????????
		ISkuPackageDetailService skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		List<SkuPackageDetail> skuPackageDetailAll = skuPackageDetailService.list(Condition.getQueryWrapper(
			new SkuPackageDetail())
			.lambda()
			.in(SkuPackageDetail::getWspId, wspIdList));
		// ?????????????????????????????????
		for (SkuPackage skuPackage : skuPackageList) {
			// ???????????????????????????????????????
			List<SkuPackageDetail> skuPackageDetailList = skuPackageDetailAll.stream().filter(skuPackageDetail -> {
				return skuPackageDetail.getWspId().equals(skuPackage.getWspId());
			}).collect(Collectors.toList());
			Integer maxLength = 1;
			if (Func.isNotEmpty(skuPackageDetailList)) {
				maxLength = skuPackageDetailList.size();
			}
			for (int i = 0; i < maxLength; i++) {
				SkuPackageDetail skuPackageDetail = i < skuPackageDetailList.size() ? skuPackageDetailList.get(i) : null;
				SkuPackageExcel skuPackageExportDTO = SkuPackageWrapper.build().entityToExportDTO(skuPackage);

				// ????????????????????????
				if (Func.isNotEmpty(skuPackageDetail)) {
					SkuPackageDetailVO skuPackageDetailVO = SkuPackageDetailWrapper.build().entityVO(skuPackageDetail);
					skuPackageExportDTO.setSkuLevelName(skuPackageDetailVO.getSkuLevelName());
					skuPackageExportDTO.setWsuCode(skuPackageDetail.getWsuCode());
					skuPackageExportDTO.setWsuName(skuPackageDetail.getWsuName());
					skuPackageExportDTO.setConvertQty(skuPackageDetail.getConvertQty());
					skuPackageExportDTO.setSkuSpec(skuPackageDetail.getSkuSpec());
					skuPackageExportDTO.setFilterValue(skuPackageDetail.getFilterValue());
					skuPackageExportDTO.setIndicatorDigit(skuPackageDetail.getIndicatorDigit());
					skuPackageExportDTO.setLpnHeight1(skuPackageDetail.getLpnHeight());
					skuPackageExportDTO.setLpnWeight1(skuPackageDetail.getLpnWeight());
					skuPackageExportDTO.setLpnWidth1(skuPackageDetail.getLpnWidth());
					skuPackageExportDTO.setLpnLength1(skuPackageDetail.getLpnLength());
				}
				//????????????????????????list
				skuPackageExportList.add(skuPackageExportDTO);
			}
		}
		return skuPackageExportList;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
		List<SkuPackage> skuPackageList = super.listByIds(idList);
		String skuName = "??????";
		long cnn = 0;
		for (SkuPackage skuPackage : skuPackageList) {
			//SkuPackage skuPackage = super.getById((Long)id);
			long cnt = skuService.count(Condition.getQueryWrapper(new Sku())
				.lambda()
				.eq(Sku::getWspId, skuPackage.getWspId())
			);
			if (cnt > 0L) {
				skuName = skuName + skuPackage.getWspName() + " ";
				cnn++;
			}
		}
		if (cnn > 0L) {
			throw new ServiceException(skuName + "???????????????????????????????????????????????????????????????");
		}
		/*List<Long> WspIds = NodesUtil.toList(skuPackageList,SkuPackage::getWspId);
		long cnt = skuService.count(Condition.getQueryWrapper(new Sku())
			.lambda()
			.in(Sku::getWspId, WspIds)
		);
		if (cnt > 0L) {
			throw new ServiceException("???????????????????????????????????????????????????????????????");
		}*/
		// ???????????????????????????
		skuPackageDetailService.remove(Condition.getQueryWrapper(new SkuPackageDetail()).lambda()
			.in(SkuPackageDetail::getWspId, idList));
		super.removeByIds(idList);
		return true;
	}

	@Override
	public boolean removeById(Serializable id) {
		List<Long> idList = new ArrayList() {{
			add(id);
		}};
		return this.removeByIds(idList);
	}

}
