package org.nodes.wms.core.strategy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.mapper.SkuMapper;
import org.nodes.wms.core.strategy.dto.WellenDTO;
import org.nodes.wms.core.strategy.dto.WellenDetailDTO;
import org.nodes.wms.core.strategy.entity.Wellen;
import org.nodes.wms.core.strategy.entity.WellenDetail;
import org.nodes.wms.core.strategy.mapper.WellenMapper;
import org.nodes.wms.core.strategy.service.IWellenService;
import org.nodes.wms.core.strategy.vo.WellenVO;
import org.nodes.wms.core.strategy.wrapper.WellenDetailWrapper;
import org.nodes.wms.core.strategy.wrapper.WellenWrapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 波次策略 服务实现类
 *
 * @author wangYN
 * @since 2021-05-26
 */
@Service("stWellenServiceImpl")
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class WellenServiceImpl<M extends SkuMapper, T extends Sku> extends BaseServiceImpl<WellenMapper, Wellen> implements IWellenService<T> {

	@Autowired
	WellenDetailServiceImpl wellenDetailService;

	@Override
	public WellenVO getDetail(WellenDTO wellen) {
		Wellen detail = this.getOne(Condition.getQueryWrapper(wellen));
		WellenVO wellenVO = null;
		if (Func.isNotEmpty(detail)) {
			wellenVO = WellenWrapper.build().entityVO(detail);
			// 获取波次策略明细信息
			List<WellenDetail> wellenDetailList = wellenDetailService.list(new LambdaQueryWrapper<WellenDetail>().eq(WellenDetail::getWellenId, detail.getId()));
			if (Func.isNotEmpty(wellenVO)) {
				wellenVO.setDetailList(WellenDetailWrapper.build().listVO(wellenDetailList));
			}
		}
		return wellenVO;
	}

	@Override
	public boolean saveOrUpdate(WellenDTO wellen) {
		if (Func.isEmpty(wellen.getId())) {
			return this.save(wellen);
		} else {
			return this.update(wellen);
		}
	}


	public boolean save(WellenDTO wellen) {
		verifyWellenDetail(wellen);
		List<WellenDetailDTO> detailList = wellen.getDetailList();
		if (super.save(wellen)) {
			// 保存明细表
			detailList.stream().forEach(o -> o.setWellenId(wellen.getId()));
		}
		return wellenDetailService.saveBatch(detailList);
	}

	public boolean update(WellenDTO wellen) {
		//验证
		verifyWellenDetail(wellen);
		List<WellenDetailDTO> detailList = wellen.getDetailList();
		//添加or修改主表
		super.updateById(wellen);
		List<Long> detailIdList = detailList.stream()
			.peek(o -> o.setWellenId(wellen.getId()))//新增明细添加主表id
			.filter(o -> Func.notNull(o.getId()))
			.map(WellenDetailDTO::getId)         //过滤后的id字段的集合
			.collect(Collectors.toList());
		//条件删除明细
		wellenDetailService.remove(new LambdaQueryWrapper<WellenDetail>()
			.notIn(WellenDetail::getId, detailIdList)
			.eq(WellenDetail::getWellenId, wellen.getId()));
		//添加or修改明细
		return wellenDetailService.saveOrUpdateBatch(detailList);
	}


	public void verifyWellenDetail(WellenDTO wellen) {
		//验证
		List<WellenDetailDTO> detailList = wellen.getDetailList();
		if (Func.isEmpty(detailList)) {
			throw new ServiceException("明细不能为空,请添加明细");
		}
	}

	@Override
	public boolean removeByIds(List<Long> ids) {
//		boolean isDefault = ReflectUtil.hasField(new Wellen().getClass(), "isDefault");
//		if (!isDefault) {
//			throw new ServiceException("实体中没有isDefault字段");
//		}
		super.removeByIds(ids);
		wellenDetailService.remove(new LambdaQueryWrapper<WellenDetail>().in(WellenDetail::getWellenId, ids));
		return true;
	}

}

