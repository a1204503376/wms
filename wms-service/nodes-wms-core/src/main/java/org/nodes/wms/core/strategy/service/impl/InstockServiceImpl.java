
package org.nodes.wms.core.strategy.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.basedata.service.ISkuInstockService;
import org.nodes.wms.dao.putway.entities.*;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.instock.InstockFactory;
import org.nodes.wms.core.strategy.service.*;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.wms.core.strategy.dto.InstockDTO;
import org.nodes.wms.core.strategy.dto.InstockDetailDTO;
import org.nodes.wms.core.strategy.mapper.InstockMapper;
import org.nodes.wms.core.strategy.vo.InstockDetailVO;
import org.nodes.wms.core.strategy.vo.InstockVO;
import org.nodes.wms.core.strategy.wrapper.InstockConfigLotWrapper;
import org.nodes.wms.core.strategy.wrapper.InstockConfigWrapper;
import org.nodes.wms.core.strategy.wrapper.InstockDetailWrapper;
import org.nodes.wms.core.strategy.wrapper.InstockWrapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 上架策略 服务实现类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class InstockServiceImpl<M extends InstockMapper, T extends Instock>
	extends BaseServiceImpl<InstockMapper, Instock>
	implements IInstockService {

	@Autowired
	IInstockDetailService instockDetailService;
	@Autowired
	IInstockConfigLotService instockConfigLotService;
	@Autowired
	IInstockConfigService instockConfigService;
	@Autowired
	IInstockLogService instockLogService;

	/**
	 * 数据验证
	 *
	 * @param instock 上架策略实体DTO
	 */
	private void validateData(InstockDTO instock) {
		// 数据验证
		if (StringUtil.isEmpty(instock.getSsiCode())) {
			throw new ServiceException("策略编号不能为空！");
		} else if (StringUtil.isEmpty(instock.getSsiName())) {
			throw new ServiceException("策略名称不能为空！");
		} else if (StringUtil.isEmpty(instock.getWhId())) {
			throw new ServiceException("库房不能为空！");
		} else {
			Instock instockQuery = new Instock();
			instockQuery.setSsiCode(instock.getSsiCode());
			Instock find = super.getOne(Condition.getQueryWrapper(instockQuery));
			if (ObjectUtil.isNotEmpty(find) && !find.getSsiId().equals(instock.getSsiId())) {
				throw new ServiceException(String.format("策略编号：%s 已存在！", instock.getSsiCode()));
			}
		}
	}

	/**
	 * 添加上架策略
	 *
	 * @param instockDTO
	 * @return
	 */
	@Override
	public boolean save(InstockDTO instockDTO) {
		this.validateData(instockDTO);

		boolean result = super.save(instockDTO);
		//添加缓存
		Instock instock = super.getOne(new LambdaQueryWrapper<Instock>().eq(Instock::getSsiId, instockDTO.getSsiId()));
		//InstockCache.saveOrUpdate(instock);

		//添加上架策略明细
		if (ObjectUtil.isNotEmpty(instockDTO.getInstockDetailList())) {

			for (InstockDetailDTO detail : instockDTO.getInstockDetailList()) {
				detail.setSsiId(instockDTO.getSsiId());
				instockDetailService.save(detail);
			}
		}
		return result;
	}

	/**
	 * 修改上架策略
	 *
	 * @param instockDTO
	 * @return
	 */
	@Override
	public boolean updateById(InstockDTO instockDTO) {
		this.validateData(instockDTO);

		boolean result = super.updateById((Instock) instockDTO);
		//InstockCache.saveOrUpdate(instockDTO);

		// 更新上架策略明细
		if (Func.isNotEmpty(instockDTO.getInstockDetailList())) {
			instockDTO.getInstockDetailList().stream().forEach(detail -> {
				detail.setSsiId(instockDTO.getSsiId());
				instockDetailService.saveOrUpdate(detail);
				//更新单据类型明细
				if (Func.isNotEmpty(detail.getInstockConfigList())) {
					detail.getInstockConfigList().forEach(u -> u.setSsidId(detail.getSsidId()));
					instockConfigService.saveOrUpdateBatch(
						detail.getInstockConfigList(), detail.getInstockConfigList().size());
				}
				if (Func.isNotEmpty(detail.getInstockConfigDeletedList())) {
					// 删除单据类型明细
					detail.getInstockConfigDeletedList().stream().forEach(instockConfig -> {
						instockConfigService.removeById(instockConfig.getSsicId());
					});
				}
				//更新上架策略批属性设定
				if (Func.isNotEmpty(detail.getInstockConfigLotList())) {
					detail.getInstockConfigLotList().forEach(u -> u.setSsidId(detail.getSsidId()));
					instockConfigLotService.saveOrUpdateBatch(
						detail.getInstockConfigLotList(), detail.getInstockConfigLotList().size());
				}
				if (Func.isNotEmpty(detail.getInstockConfigLotDeletedList())) {
					// 删除批属性设置
					detail.getInstockConfigLotDeletedList().stream().forEach(configLot -> {
						instockConfigLotService.removeById(configLot.getSsiclId());
					});
				}
			});
		}

		// 删除客户端删除的明细
		if (Func.isNotEmpty(instockDTO.getInstockDetailDeletedList())) {
			instockDTO.getInstockDetailDeletedList().stream().forEach(detail -> {
				// 删除批属性设置
				/*InstockConfigLotCache.listBySsidId(detail.getSsiId()).stream().map(configLot -> {
					return instockConfigLotService.removeById(configLot.getSsiclId());
				});*/
				// 删除单据明细
				/*InstockConfigCache.listByssidId(detail.getSsidId()).stream().map(instockConfig -> {
					return instockConfigService.removeById(instockConfig.getSsicId());
				});*/

				IInstockConfigLotService instockConfigLotService = SpringUtil.getBean(IInstockConfigLotService.class);
				instockConfigLotService.removeById(detail.getSsiId());

				IInstockConfigService instockConfigService = SpringUtil.getBean(IInstockConfigService.class);
				instockConfigService.removeById(detail.getSsidId());


				// 删除上架策略明细
				instockDetailService.removeById(detail.getSsidId());
			});
		}
		return true;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		IInstockService instockService = SpringUtil.getBean(IInstockService.class);
		IInstockDetailService instockDetailService = SpringUtil.getBean(IInstockDetailService.class);
		for (Serializable id : idList) {

			Instock instock = instockService.getById(id);
			if (Func.isEmpty(instock)) {
				throw new ServiceException(String.format(
					"上架策略[编码:%s, 名称:%s]不存在! ", instock.getSsiCode(), instock.getSsiName()));
			}
			if (instock.getIsDefault() != 0) {
				throw new ServiceException(String.format(
					"上架策略[编码:%s, 名称:%s]为系统默认策略，不允许删除! ", instock.getSsiCode(), instock.getSsiName()));
			}

			ISkuInstockService skuInstockService = SpringUtil.getBean(ISkuInstockService.class);
			long cnt = skuInstockService.count(Condition.getQueryWrapper(new SkuInstock())
				.lambda()
				.eq(SkuInstock::getSkuId, id));
			if (cnt > 0L) {

				throw new ServiceException(String.format(
					"该上架策略[编码:%s, 名称:%s]被占用，请先删除占用信息", instock.getSsiCode(), instock.getSsiName()));
			}
			//删除子表
			List<InstockDetail> instockDetails = instockDetailService.list(Condition.getQueryWrapper(new InstockDetail())
			.lambda()
			.eq(InstockDetail::getSsiId,id)
			);
			if (ObjectUtil.isNotEmpty(instockDetails)) {
				instockDetails.stream().forEach(detail -> {
					// 删除批属性设置
					/*InstockConfigLotCache.listBySsidId(detail.getSsidId()).stream().map(configLot -> {
						return instockConfigLotService.removeById(configLot.getSsiclId());
					});*/
					// 删除单据明细
					/*InstockConfigCache.listByssidId(detail.getSsidId()).stream().map(instockConfig -> {
						return instockConfigService.removeById(instockConfig.getSsicId());
					});*/
					IInstockConfigLotService instockConfigLotService = SpringUtil.getBean(IInstockConfigLotService.class);
					instockConfigLotService.removeById(detail.getSsidId());
					IInstockConfigService instockConfigService = SpringUtil.getBean(IInstockConfigService.class);
					instockConfigService.removeById(detail.getSsidId());
					// 删除上架策略明细
					instockDetailService.removeById(detail.getSsidId());
				});
			}
			this.removeById(id);
			//删除缓存
			//InstockCache.remove(id);

		}
		return true;
	}

	/**
	 * 添加或更新上架策略
	 *
	 * @param instockDTO 上架策略
	 * @return 是否成功
	 */
	@Override
	public boolean saveOrUpdate(InstockDTO instockDTO) {
		if (ObjectUtil.isEmpty(instockDTO.getSsiId())) {
			return this.save(instockDTO);
		} else {
			return this.updateById(instockDTO);
		}
	}

	/**
	 * 查询上架策略详情
	 *
	 * @param wrapper 查询条件
	 * @return 上架策略详情
	 */
	@Override
	public InstockVO getOne(Wrapper<Instock> wrapper) {
		InstockVO instockVO = InstockWrapper.build().entityVO(super.getOne(wrapper));
		IInstockConfigService instockConfigService = SpringUtil.getBean(IInstockConfigService.class);
		IInstockConfigLotService instockConfigLotService = SpringUtil.getBean(IInstockConfigLotService.class);
		IInstockDetailService instockDetailService = SpringUtil.getBean(IInstockDetailService.class);
		// 明细
		/*List<InstockDetail> instockDetailList = InstockDetailCache.list(instockVO.getSsiId()).stream()
			.sorted(Comparator.comparing(InstockDetail::getSsidProcOrder))
			.collect(Collectors.toList());*/
		List<InstockDetail> instockDetailList = instockDetailService.list(Condition.getQueryWrapper(new InstockDetail())
			.lambda()
			.eq(InstockDetail::getSsiId, instockVO.getSsiId())
		).stream()
			.sorted(Comparator.comparing(InstockDetail::getSsidProcOrder)).collect(Collectors.toList());
		instockVO.setInstockDetailList(InstockDetailWrapper.build().listVO(instockDetailList));

		for (InstockDetailVO detail : instockVO.getInstockDetailList()) {
			// 批属性设定
			//List<InstockConfigLot> configLotList = InstockConfigLotCache.listBySsidId(detail.getSsidId());
			List<InstockConfigLot> configLotList = instockConfigLotService.list(Condition.getQueryWrapper(new InstockConfigLot())
				.lambda()
				.eq(InstockConfigLot::getSsidId, detail.getSsidId())
			);
			detail.setInstockConfigLotList(InstockConfigLotWrapper.build().listVO(configLotList));
			// 物品明细
			//List<InstockConfig> instockConfigs = InstockConfigCache.listByssidId(detail.getSsidId());
			List<InstockConfig> instockConfigs = instockConfigService.list(Condition.getQueryWrapper(new InstockConfig())
				.lambda()
				.eq(InstockConfig::getSsidId, detail.getSsidId()));
			detail.setInstockConfigList(InstockConfigWrapper.build().listVO(instockConfigs));
		}

		return instockVO;
	}

	@Override
	public InstockExecuteVO execute(List<Stock> stockList, String billTypeCd) {
		// 上架策略执行结果
		InstockExecuteVO instockExecute = new InstockExecuteVO();
		try {
			for (Stock stock : stockList) {
				Sku sku = SkuCache.getById(stock.getSkuId());
				if (Func.isEmpty(sku)) {
					throw new ServiceException("物品不存在或已删除！");
				}
				// 获取入库设置
				ISkuInstockService skuInstockService = SpringUtil.getBean(ISkuInstockService.class);
				//SkuInstock skuInstock = SkuInstockCache.getOne(stock.getSkuId(), stock.getWhId());
				SkuInstock skuInstock = skuInstockService.list(Condition.getQueryWrapper(new SkuInstock())
					.lambda()
					.eq(SkuInstock::getSkuId, stock.getSkuId())
					.eq(SkuInstock::getWhId, stock.getWhId())
				).stream().findFirst().orElse(null);

				if (Func.isEmpty(skuInstock)) {
					throw new ServiceException("物品：" + sku.getSkuName() + " 未配置入库设置！");
				}
				IInstockService instockService = SpringUtil.getBean(IInstockService.class);
				Instock instock = instockService.getById(skuInstock.getSsiId());
				if (Func.isEmpty(instock)) {
					throw new ServiceException("物品：" + sku.getSkuName() + " 指定的上架策略不存在！");
				}
				instockExecute.setSsiId(instock.getSsiId());
				instockExecute.setSsiCode(instock.getSsiCode());
				instockExecute.setSsiName(instock.getSsiName());
				instockExecute.setWhId(instock.getWhId());
				InstockDetail instockDetail = instockDetailService.find(instock, billTypeCd, sku.getSkuTypeId(), stock);
				if (ObjectUtil.isEmpty(instockDetail)) {
					throw new ServiceException("物品：" + sku.getSkuName() + " 没有可执行上架策略明细！");
				}
				instockExecute.setSsidProcOrder(instockDetail.getSsidProcOrder());
				instockExecute.setInstockFunction(instockDetail.getInstockFunction());
				instockExecute.setInstockFunctionDesc(
					DictCache.getValue(DictConstant.INSTOCK_FUNCTION, instockDetail.getInstockFunction()));
				InstockFactory.getInstance().create(instockDetail.getInstockFunction())
					.execute(stock, instockDetail, instockExecute);
				instockExecute.setSuccess(true);
			}
		} catch (Exception e) {
			instockExecute.setSuccess(false);
			instockExecute.setMsg(e.getMessage());
		}
		InstockLog instockLog = new InstockLog();
		instockLog.setSsiId(instockExecute.getSsiId());
		instockLog.setSsiCode(instockExecute.getSsiCode());
		instockLog.setSsiName(instockExecute.getSsiName());
		instockLog.setWhId(instockExecute.getWhId());
		instockLog.setSsidProcOrder(instockExecute.getSsidProcOrder());
		instockLog.setInstockFunction(instockExecute.getInstockFunction());
		instockLog.setAplProcLog(instockExecute.getMsg());
		instockLog.setExecuteOrder(instockExecute.getSsidProcOrder());
		instockLog.setIsSuccess(instockExecute.getSuccess() ? 1 : 0);
		instockLogService.save(instockLog);
		return instockExecute;
	}
}
