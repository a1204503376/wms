package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
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
 * 包装ID 服务实现类
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
	 * 验证包装明细
	 *
	 * @param skupackageDTO
	 */
	protected void verifyPackageDetail(SkuPackageDTO skupackageDTO) {

		//验证
		List<SkuPackageDetailDTO> list = skupackageDTO.getSkuPackageDetailDTOList();
		if (ObjectUtil.isEmpty(list)) {
			throw new ServiceException("包装明细不能为空,请创建包装明细");
		}

		QueryWrapper<SkuPackage> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("wsp_name", skupackageDTO.getWspName());
		queryWrapper.ne("wsp_id", skupackageDTO.getWspId());
		if (super.count(queryWrapper) > 0) {
			throw new ServiceException("包装名称：" + skupackageDTO.getWspName() + " 已存在！");
		}

		//是否有基础计量单位
		boolean ifBase = false;
		int countBase = 0;
		int[] levels = new int[list.size()];
		int i = 0;
		for (SkuPackageDetailDTO detailDTO : list) {
			if (detailDTO.getConvertQty() <= 0) {
				throw new ServiceException("换算倍率必须是正整数");
			}
			if (Func.equals(1, detailDTO.getSkuLevel())) {
				ifBase = true; // 判断有基础计量单位
				countBase++; // 计算基础计量单位个数
				if (detailDTO.getConvertQty() > 1) {
					throw new ServiceException("基础计量单位的换算倍率必须是1");
				}
			}
			// 层级与包装联合唯一
			if (Arrays.binarySearch(levels, detailDTO.getSkuLevel()) > 0) {
				throw new ServiceException("同一个包装层级不可重复");
			} else {
				levels[i] = detailDTO.getSkuLevel();
			}
			String skuLevel = DictCache.getValue(DictConstant.SKU_LEVEL, detailDTO.getSkuLevel());
			if (Func.isEmpty(skuLevel)) {
				throw new ServiceException("层级不存在,只包含:1.基础计量单位;10.内包装;20.箱;30.托盘");
			}
			i++;
		}
		if (countBase > 1) {
			throw new ServiceException("只能创建一个基础计量单位");
		}
		if (!ifBase) {
			throw new ServiceException("请先创建基础计量单位");
		}
		//按照层级倒叙排序
		Collections.sort(list, (SkuPackageDetailDTO s1, SkuPackageDetailDTO s2) -> {
			return s2.getSkuLevel().compareTo(s1.getSkuLevel());
		});
		//验证换算倍率整除
		int converQty = 0;
		for (SkuPackageDetailDTO detailDTO : list) {
			if (0 == converQty) {
				converQty = detailDTO.getConvertQty();
			} else {
				if (converQty == detailDTO.getConvertQty())
					throw new ServiceException("换算倍率不能相等");
				converQty = detailDTO.getConvertQty();
			}
		}
	}

	@Override
	public boolean updateById(SkuPackageDTO entity) {
		if (Func.isEmpty(super.getById(entity.getWspId()))) {
			throw new ServiceException("包装主键不存在:wspId");
		}
		this.verifyPackageDetail(entity);
		//删除的包装明细列表
		List<SkuPackageDetailDTO> packageDetailDeleteList = entity.getPageDeletedList();
		//删除包装明细
		if (Func.isNotEmpty(packageDetailDeleteList)) {
			for (SkuPackageDetailDTO detailDTO : packageDetailDeleteList) {
				skuPackageDetailService.removeById(detailDTO.getWspdId());
			}
		}
		//新增或修改的包装明细列表
		List<SkuPackageDetailDTO> packageDetailList = entity.getSkuPackageDetailDTOList()
			.stream()
			.sorted(Comparator.comparing(SkuPackageDetailDTO::getSkuLevel)).collect(Collectors.toList());
		;
		if (Func.isEmpty(packageDetailList)) {
			throw new ServiceException("包装明细不能为空");
		}
		SkuPackageDetailDTO defaultDetail = packageDetailList.get(0);
		//判断层级是否重复
		Map<Integer, Object> map = new HashMap<>();
		for (SkuPackageDetailDTO detailDTO : packageDetailList) {
			//如果map.get()为空则没有重复
			if (Func.isEmpty(map.get(detailDTO.getSkuLevel()))) {
				map.put(detailDTO.getSkuLevel(), detailDTO);
			} else {
				String skuLevelDesc = DictCache.getValue(DictConstant.SKU_LEVEL, detailDTO.getSkuLevel());
				throw new ServiceException(
					"包装：" + entity.getWspName() + " 明细层级“" + skuLevelDesc + "”已存在！");
			}
		}

		//保存包装明细信息开始
		packageDetailList = packageDetailList.stream().sorted(
			Comparator.comparing(SkuPackageDetailDTO::getSkuLevel).reversed()).collect(Collectors.toList());
		SkuPackageDetailDTO maxPackageDetail = packageDetailList.get(0);
		//规格
		StringBuffer spec = new StringBuffer(StringPool.ONE + maxPackageDetail.getWsuName());
		for (SkuPackageDetailDTO detailDTO : packageDetailList) {
			//包装id与包装层级查询条件
			SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(
				detailDTO.getWspId(), detailDTO.getSkuLevel());

			//判断是新增还是修改
			if (Func.isEmpty(detailDTO.getWspdId())) { // 新增
				//判断包装id与层级是否联合唯一
				if (Func.isNotEmpty(packageDetail)) { // 存在该层级
					String skuLevelDesc = DictCache.getValue(DictConstant.SKU_LEVEL, detailDTO.getSkuLevel());
					throw new ServiceException(
						"包装：" + entity.getWspName() + " 明细层级“" + skuLevelDesc + "”已存在！");
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
			throw new ServiceException("包装主键已存在！");
		}
		verifyPackageDetail(entity);

		//查询单据编号是否编号
		SkuPackage param = new SkuPackage();
		param.setWspName(entity.getWspName());
		if (super.count(Condition.getQueryWrapper(param).lambda()
			.eq(SkuPackage::getWspName, entity.getWspName()).or(i -> i.eq(SkuPackage::getIsDeleted, 1))) > 0) {
			throw new ServiceException("包装名称已存在！");
		}
		if (super.save(entity)) {
			//super.saveOrUpdate(entity);
		}
		List<Integer> specList = new ArrayList();
		if (ObjectUtil.isNotEmpty(entity.getSkuPackageDetailDTOList())) {
			// 根据层级先排序
			List<SkuPackageDetailDTO> packageDetailList = entity.getSkuPackageDetailDTOList()
				.stream()
				.sorted(Comparator.comparing(SkuPackageDetailDTO::getSkuLevel).reversed()).collect(Collectors.toList());
			SkuPackageDetailDTO minSkuPackageDetail = packageDetailList.get(packageDetailList.size() - 1);
			SkuPackageDetailDTO maxSkuPackageDetail = packageDetailList.get(0);
			StringBuffer specBuffer = new StringBuffer(StringPool.ONE + maxSkuPackageDetail.getWsuName());
			for (SkuPackageDetailDTO packageDetail : packageDetailList) {
				if (Func.isNotEmpty(packageDetail.getWspdId())) {
					if (Func.isNotEmpty(SkuPackageDetailCache.getById(packageDetail.getWspdId())))
						throw new ServiceException("包装明细主键已存在！");
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
							"计量单位[%s]不存在", packageDetail.getWsuCode()));
					}
				}
				//拼接规格
				String spec = String.format("%s%s-%s%s",
					minSkuPackageDetail.getConvertQty(), packageDetail.getWsuName(),
					packageDetail.getConvertQty(), minSkuPackageDetail.getWsuName());
				packageDetail.setSkuSpec(spec);
				packageDetail.setWspId(entity.getWspId());
				if (!skuPackageDetailService.saveOrUpdate(packageDetail)) {
					throw new ServiceException("包装明细新增失败！");
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
			// 更新包装规格
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
			ExcelUtil.export(response, "物品包装", "物品包装表", skuPackageExcels, SkuPackageExcel.class);
		}
	}

	/**
	 * 获取物品包装信息
	 *
	 * @param name 包装名称
	 * @return 包装信息
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
			// 封装成DTO
			SkuPackageDTO skuPackageDTO = SkuPackageWrapper.build().entityDTO(skuPackageExcel);

			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(skuPackageDTO);
			// 包装名称唯一性验证
			SkuPackage findObj = this.getByName(skuPackageDTO.getWspName());
			if (ObjectUtil.isNotEmpty(findObj)) {
				validResult.addError(
					"wspName",
					"包装名称" + skuPackageDTO.getWspName() + "已存在");
			}
			if (Func.isNotEmpty(skuPackageDTO.getSkuPackageDetailDTOList()) && !NodesUtil.isAllNull(skuPackageDTO.getSkuPackageDetailDTOList().get(0))) {
				ValidationUtil.ValidResult validResult1 = ValidationUtil.validateBean(skuPackageDTO.getSkuPackageDetailDTOList().get(0), Excel.class);
				validResult.getAllErrors().addAll(validResult1.getAllErrors());
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				if (cache.containsKey(skuPackageExcel.getWspName())) {
					// 更新地址、联系人信息
					cache.get(skuPackageDTO.getWspName()).getSkuPackageDetailDTOList()
						.addAll(skuPackageDTO.getSkuPackageDetailDTOList());
				} else {
					// 记录到缓存map中
					cache.put(skuPackageDTO.getWspName(), skuPackageDTO);
				}
				dataVerify.setCacheKey(skuPackageDTO.getWspName());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				SkuPackageCache.put(code, cache.get(code));
			}
		}

		return dataVerifyList;
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
	 * 获得导出所需要的包装数据
	 *
	 * @param skuPackageList
	 * @return
	 */
	private List<SkuPackageExcel> getSkuPackageExportDTOList(List<SkuPackage> skuPackageList) {
		// 用来导出的最终包装列表
		List<SkuPackageExcel> skuPackageExportList = new ArrayList<>();
		List<Long> wspIdList = NodesUtil.toList(skuPackageList, SkuPackage::getWspId);
		// 查询所有包装明细
		ISkuPackageDetailService skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		List<SkuPackageDetail> skuPackageDetailAll = skuPackageDetailService.list(Condition.getQueryWrapper(
			new SkuPackageDetail())
			.lambda()
			.in(SkuPackageDetail::getWspId, wspIdList));
		// 循环查询出来的包装列表
		for (SkuPackage skuPackage : skuPackageList) {
			// 查询当前包装的所有层级信息
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

				// 封装包装明细数据
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
				//将包装明细装入新list
				skuPackageExportList.add(skuPackageExportDTO);
			}
		}
		return skuPackageExportList;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
		List<SkuPackage> skuPackageList = super.listByIds(idList);
		String skuName = "包装";
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
			throw new ServiceException(skuName + "已关联物品信息，请删除关联后再执行删除操作");
		}
		/*List<Long> WspIds = NodesUtil.toList(skuPackageList,SkuPackage::getWspId);
		long cnt = skuService.count(Condition.getQueryWrapper(new Sku())
			.lambda()
			.in(Sku::getWspId, WspIds)
		);
		if (cnt > 0L) {
			throw new ServiceException("已关联物品信息，请删除关联后再执行删除操作");
		}*/
		// 删除该包装下的明细
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
