
package org.nodes.wms.core.strategy.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.basedata.service.ISkuInstockService;
import org.nodes.wms.core.strategy.dto.RelenishmentDTO;
import org.nodes.wms.core.strategy.dto.RelenishmentDetailDTO;
import org.nodes.wms.core.strategy.entity.Relenishment;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;
import org.nodes.wms.core.strategy.mapper.RelenishmentMapper;
import org.nodes.wms.core.strategy.service.IRelenishmentDetailService;
import org.nodes.wms.core.strategy.service.IRelenishmentService;
import org.nodes.wms.core.strategy.vo.RelenishmentVO;
import org.nodes.wms.core.strategy.wrapper.RelenishmentDetailWrapper;
import org.nodes.wms.core.strategy.wrapper.RelenishmentWrapper;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
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
 * 补货策略 服务实现类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class RelenishmentServiceImpl<M extends RelenishmentMapper, T extends Relenishment>
	extends BaseServiceImpl<RelenishmentMapper, Relenishment>
	implements IRelenishmentService {

	@Autowired
	IRelenishmentDetailService relenishmentDetailService;

	/**
	 * 数据验证
	 *
	 * @param relenishmentDTO 补货策略实体DTO
	 */
	private void validateData(RelenishmentDTO relenishmentDTO) {
		// 数据验证
		if (StringUtil.isEmpty(relenishmentDTO.getSsiCode())) {
			throw new ServiceException("策略编号不能为空！");
		} else if (StringUtil.isEmpty(relenishmentDTO.getSsiName())) {
			throw new ServiceException("策略名称不能为空！");
		} else if (StringUtil.isEmpty(relenishmentDTO.getWhId())) {
			throw new ServiceException("库房不能为空！");
		} else {
			Relenishment relenishmentQuerCondition = new Relenishment();
			relenishmentQuerCondition.setSsiCode(relenishmentDTO.getSsiCode());
			Relenishment relenishment = super.getOne(Condition.getQueryWrapper(relenishmentQuerCondition));
			if (ObjectUtil.isNotEmpty(relenishment) && !relenishment.getSsiId().equals(relenishmentDTO.getSsiId())) {
				throw new ServiceException(String.format("策略编号：%s 已存在！", relenishmentDTO.getSsiCode()));
			}
			Relenishment relenishmentQuery = new Relenishment();
			relenishmentQuery.setWhId(relenishmentDTO.getWhId());
			Relenishment find = super.getOne(Condition.getQueryWrapper(relenishmentQuery));
			if (ObjectUtil.isNotEmpty(find) && !find.getSsiId().equals(relenishmentDTO.getSsiId())) {
				Warehouse warehouse = WarehouseCache.getById(relenishmentDTO.getWhId());
				if(Func.isEmpty(warehouse)){
					throw new ServiceException("库房不存在！");
				}
				throw new ServiceException(String.format("库房[%s]已存在补货策略！", warehouse.getWhName()));
			}
		}
	}

	/**
	 * 添加补货策略
	 *
	 * @param relenishmentDTO
	 * @return
	 */
	@Override
	public boolean save(RelenishmentDTO relenishmentDTO) {
		this.validateData(relenishmentDTO);

		boolean result = super.save(relenishmentDTO);
		if (result) {
			//RelenishmentCache.saveOrUpdate(relenishmentDTO);
		}
		//添加补货策略明细
		if (ObjectUtil.isNotEmpty(relenishmentDTO.getRelenishmentDetailList())) {

			for (RelenishmentDetailDTO detail : relenishmentDTO.getRelenishmentDetailList()) {
				detail.setSsiId(relenishmentDTO.getSsiId());
				detail.setSsidProcOrder(-1);
				detail.setSkuLevel(-1);
				relenishmentDetailService.save(detail);
			}
		}
		return result;
	}

	/**
	 * 修改补货策略
	 *
	 * @param relenishmentDTO
	 * @return
	 */
	@Override
	public boolean updateById(RelenishmentDTO relenishmentDTO) {

		this.validateData(relenishmentDTO);

		boolean result = super.updateById((Relenishment) relenishmentDTO);

		// 更新补货策略明细
		if (Func.isNotEmpty(relenishmentDTO.getRelenishmentDetailList())) {
			relenishmentDTO.getRelenishmentDetailList().stream().forEach(detail -> {
				detail.setSsiId(relenishmentDTO.getSsiId());
				detail.setSsidProcOrder(-1);
				detail.setSkuLevel(-1);
				relenishmentDetailService.saveOrUpdate(detail);
			});
		}

		// 删除客户端删除的明细
		if (Func.isNotEmpty(relenishmentDTO.getRelenishmentDetailDeletedList())) {
			relenishmentDTO.getRelenishmentDetailDeletedList().stream().forEach(detail -> {
				// 删除补货策略明细
				relenishmentDetailService.removeById(detail.getSsidId());
			});
		}
		return true;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		IRelenishmentService relenishmentService = SpringUtil.getBean(IRelenishmentService.class);
		IRelenishmentDetailService relenishmentDetailService = SpringUtil.getBean(IRelenishmentDetailService.class);
		for (Serializable id : idList) {
			Relenishment relenishment = relenishmentService.getById(id);
			if (Func.isEmpty(relenishment)) {
				throw new ServiceException(String.format(
					"补货策略[编码:%s, 名称:%s]不存在! ", relenishment.getSsiCode(), relenishment.getSsiName()));
			}
			ISkuInstockService skuInstockService = SpringUtil.getBean(ISkuInstockService.class);
			long cnt = skuInstockService.count(Condition.getQueryWrapper(new SkuInstock())
				.lambda()
				.eq(SkuInstock::getSkuId,id));
			if (cnt > 0L) {

				throw new ServiceException(String.format(
					"该补货策略[编码:%s, 名称:%s]被占用，请先删除占用信息", relenishment.getSsiCode(), relenishment.getSsiName()));
			}
			//删除子表
			//List<RelenishmentDetail> relenishmentDetails = RelenishmentDetailCache.list(id);
			List<RelenishmentDetail> relenishmentDetails = relenishmentDetailService.list(Condition.getQueryWrapper(new RelenishmentDetail())
			.lambda()
			.eq(RelenishmentDetail::getSsiId,id)
			);
			if (ObjectUtil.isNotEmpty(relenishmentDetails)) {
				relenishmentDetails.stream().forEach(detail -> {
					// 删除补货策略明细
					relenishmentDetailService.removeById(detail.getSsidId());
				});
			}
			this.removeById(id);
		}
		return true;
	}

	/**
	 * 添加或更新补货策略
	 *
	 * @param relenishmentDTO 补货策略
	 * @return 是否成功
	 */
	@Override
	public boolean saveOrUpdate(RelenishmentDTO relenishmentDTO) {
		if (ObjectUtil.isEmpty(relenishmentDTO.getSsiId())) {
			return this.save(relenishmentDTO);
		} else {
			return this.updateById(relenishmentDTO);
		}
	}

	/**
	 * 查询补货策略详情
	 *
	 * @param wrapper 查询条件
	 * @return 补货策略详情
	 */
	@Override
	public RelenishmentVO getOne(Wrapper<Relenishment> wrapper) {
		RelenishmentVO relenishmentVO = RelenishmentWrapper.build().entityVO(super.getOne(wrapper));

		IRelenishmentDetailService relenishmentDetailService = SpringUtil.getBean(IRelenishmentDetailService.class);
		// 明细
		/*List<RelenishmentDetail> relenishmentDetailList = relenishmentDetailService.list(relenishmentVO.getSsiId()).stream()
			.sorted(Comparator.comparing(RelenishmentDetail::getSsidProcOrder))
			.collect(Collectors.toList());*/
		List<RelenishmentDetail> relenishmentDetailList = relenishmentDetailService.list(Condition.getQueryWrapper(new RelenishmentDetail())
			.lambda()
			.eq(RelenishmentDetail::getSsiId, relenishmentVO.getSsiId())
		).stream()
			.sorted(Comparator.comparing(RelenishmentDetail::getSsidProcOrder))
			.collect(Collectors.toList());
		relenishmentVO.setRelenishmentDetailList(RelenishmentDetailWrapper.build().listVO(relenishmentDetailList));

		return relenishmentVO;
	}

}
