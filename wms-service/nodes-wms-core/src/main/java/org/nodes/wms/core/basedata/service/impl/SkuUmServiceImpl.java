
package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.core.basedata.service.ISkuPackageDetailService;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomer;
import org.nodes.wms.dao.basics.sku.SkuUmDao;
import org.nodes.wms.dao.basics.sku.dto.input.SkuUmAddOrEditRequest;
import org.springblade.core.log.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.cache.SkuUmCache;
import org.nodes.wms.core.basedata.dto.SkuUmDTO;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.core.basedata.excel.SkuUmExcel;
import org.nodes.wms.core.basedata.mapper.SkuUmMapper;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.nodes.wms.core.basedata.vo.SkuUmVO;
import org.nodes.wms.core.basedata.wrapper.SkuUmWrapper;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计量单位 服务实现类
 *
 * @author zhongls
 * @since 2019-12-05
 */
@Service
@Primary
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuUmServiceImpl<M extends SkuUmMapper, T extends SkuUm>
	extends BaseServiceImpl<SkuUmMapper, SkuUm>
	implements ISkuUmService {
    private final SkuUmDao skuUmDao;
	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		ISkuPackageDetailService skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		for (Serializable id : idList) {
			if (skuPackageDetailService.count(Condition.getQueryWrapper(new SkuPackageDetail()).lambda()
				.eq(SkuPackageDetail::getWsuId, id)) > 0L) {
				throw new ServiceException("该计量单位正在被使用，不可删除！");
			}
		}
		boolean result = super.removeByIds(idList);
		if (result) {
			//super.removeByIds(idList);
		}
		return true;
	}

	@Override
	public boolean updateById(SkuUm entity) {
		QueryWrapper<SkuUm> skuUmQueryWrapper = new QueryWrapper<>();
		skuUmQueryWrapper.eq("wsu_code", entity.getWsuCode());
		skuUmQueryWrapper.ne("wsu_id", entity.getWsuId());
		if (super.count(skuUmQueryWrapper) > 0) {
			throw new ServiceException("计量编码重复");
		}
		ISkuPackageDetailService skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		if (skuPackageDetailService.count(Condition.getQueryWrapper(new SkuPackageDetail()).lambda()
			.eq(SkuPackageDetail::getWsuId, entity.getWsuId())) > 0L) {
			throw new ServiceException("该计量单位正在被使用，不可修改！");
		}
		boolean result = super.updateById(entity);
		if (result) {
			//super.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean save(SkuUm entity) {
		SkuUm param = new SkuUm();
		param.setWsuCode(entity.getWsuCode());
		//查询货主编码是否重复
		if (super.count(Condition.getQueryWrapper(param).lambda()
			.eq(SkuUm::getWsuCode, entity.getWsuCode()).or(i -> i.eq(SkuUm::getIsDeleted, 1))) > 0) {
			throw new ServiceException("计量编码重复");
		}
		boolean result = super.save(entity);
		if (result) {
			//super.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public IPage<SkuUmVO> selectPage(IPage<SkuUm> page, SkuUm skuUm) {
		IPage<SkuUm> pages = super.page(page, this.getSelectQueryWrapper(skuUm));
		return SkuUmWrapper.build().pageVO(pages);
	}

	@Override
	public List<SkuUmVO> selectList(SkuUm skuUm) {
		return SkuUmWrapper.build().listVO(super.list(this.getSelectQueryWrapper(skuUm)));
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		//查询计量单位信息
		List<SkuUmVO> skuUmList = SkuUmWrapper.build().listVO(this.list(Condition.getQueryWrapper(params, SkuUm.class)));

		List<SkuUmExcel> skuUmExportList = new ArrayList<>();
		if (Func.isNotEmpty(skuUmList)) {
			for (SkuUm skuUm : skuUmList) {
				SkuUmExcel skuUmExcel = BeanUtil.copy(skuUm, SkuUmExcel.class);
				skuUmExportList.add(skuUmExcel);
			}

		}
		ExcelUtil.export(response, "计量单位", "计量单位数据表", skuUmExportList, SkuUmExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<SkuUmExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();

		Map<String, SkuUmDTO> cache = new HashMap<>();
		int index = 1;
		for (SkuUmExcel skuUmExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 封装成DTO
			SkuUmDTO skuUmDTO = SkuUmWrapper.build().entityDTO(skuUmExcel);
			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(skuUmDTO);
			// 计量单位编码唯一性验证
			//SkuUm findSkuUm = SkuUmCache.getByCode(skuUmDTO.getWsuCode());
			SkuUm findSkuUm = super.list(Condition.getQueryWrapper(new SkuUm())
				.lambda()
				.eq(SkuUm::getWsuCode, skuUmDTO.getWsuCode())
			).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(findSkuUm)) {
				validResult.addError("wsuCode", "计量单位编码[" + skuUmDTO.getWsuCode() + "]已存在");
			}
			if (Func.isEmpty(skuUmDTO.getWsuCode())) {
				validResult.addError("wsuCode", "计量单位编码为必填项");
			}
			if (Func.isEmpty(skuUmDTO.getWsuName())) {
				validResult.addError("wsuCode", "计量单位名称为必填项");
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				cache.put(skuUmDTO.getWsuCode(), skuUmDTO);
				dataVerify.setCacheKey(skuUmDTO.getWsuCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				SkuUmCache.put(code, cache.get(code));
			}
		}

		return dataVerifyList;
	}

	@Override
	public String convert(Long wspId, int skuLevel, BigDecimal qty) {
		if (wspId == 0L || skuLevel == 0 || Func.isEmpty(skuLevel)) {
			return StringPool.EMPTY;
		}
		// 降序
		List<SkuPackageDetail> skuPackageDetailList = SkuPackageDetailCache.listByWspId(wspId).stream()
			.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel).reversed())
			.collect(Collectors.toList());
		SkuPackageDetail skuPackageDetail = skuPackageDetailList.stream().filter(u -> {
			return u.getSkuLevel().equals(skuLevel);
		}).findFirst().orElse(null);
		if (Func.isEmpty(skuPackageDetail)) {
			throw new ServiceException("包装[ID:" + wspId + "]中不存在层级为" + skuLevel + "的包装明细");
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < skuPackageDetailList.size(); i++) {
			SkuPackageDetail item = skuPackageDetailList.get(i);
			if (item.getSkuLevel() > skuPackageDetail.getSkuLevel()) {
				continue;
			}
			// 查找下当前明细下的，下个层级
			SkuPackageDetail nextItem = null;
			if (i < skuPackageDetailList.size() - 1) {
				nextItem = skuPackageDetailList.get(i + 1);
			}
			BigDecimal convertQty = new BigDecimal(skuPackageDetail.getConvertQty().toString());
			BigDecimal[] array = qty.divideAndRemainder(convertQty);
			if (BigDecimalUtil.eq(BigDecimal.ZERO, qty)) {
				sb.append("0").append(item.getWsuName());
			} else if (skuLevel == 1) {
				sb.append(BigDecimalUtil.globeScale(qty).toPlainString()).append(item.getWsuName());
			} else if (BigDecimalUtil.lt(qty, convertQty)) {
				if (Func.isEmpty(nextItem)) {
					throw new ServiceException("计量单位转换异常(可能原因:转换数量不能整除且没有基础计量单位)");
				}
				sb.append(BigDecimalUtil.globeScale(qty)).append(nextItem.getWsuName());
			} else {
				if (BigDecimalUtil.eq(array[1], BigDecimal.ZERO)) {
					// 可以整除
					sb.append(BigDecimalUtil.globeScale(qty.divide(convertQty)).toPlainString()).append(item.getWsuName());
				} else {
					if (Func.isEmpty(nextItem)) {
						throw new ServiceException("计量单位转换异常(可能原因:转换数量不能整除且没有基础计量单位)");
					}
					// 不能整除
					sb.append(BigDecimalUtil.globeScale(array[0]).toPlainString())
						.append(item.getWsuName())
						.append(convert(wspId, nextItem.getSkuLevel(), array[1]));
				}
			}
			break;
		}
		return sb.toString();
	}

    @Override
    public String addOrEdit(SkuUmAddOrEditRequest skuUmAddOrEditRequest) {
		boolean isExist = skuUmDao.isExistUmCode(skuUmAddOrEditRequest.getWsuCode());
		SkuUm skuUm = new SkuUm();
		skuUm.setWsuName(skuUmAddOrEditRequest.getWsuName());
		skuUm.setWsuCode(skuUmAddOrEditRequest.getWsuCode());
		if(isExist){
			skuUmDao.update(skuUm);
			return "计量单位编码"+skuUm.getWsuCode()+"修改成功";
		}

		skuUmDao.insert(skuUm);
		return "客户编码"+skuUm.getWsuCode()+"保存成功";


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
			SkuUmDTO skuUmDTO = SkuUmCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(skuUmDTO)) {
				continue;
			}
			if (this.saveOrUpdate(skuUmDTO)) {
				SkuUmCache.remove(skuUmDTO.getWsuCode());
				//super.saveOrUpdate(skuUmDTO);
			}
		}
		return true;
	}

	/**
	 * 获取模糊查询构造器
	 *
	 * @param skuUm 查询条件
	 * @return 查询构造器
	 */
	private QueryWrapper<SkuUm> getSelectQueryWrapper(SkuUm skuUm) {
		QueryWrapper<SkuUm> queryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(skuUm.getWsuCode())) {
			queryWrapper.lambda().like(SkuUm::getWsuCode, skuUm.getWsuCode());
		}
		if (Func.isNotEmpty(skuUm.getWsuName())) {
			queryWrapper.lambda().like(SkuUm::getWsuName, skuUm.getWsuName());
		}
		return queryWrapper;
	}
}
