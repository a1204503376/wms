package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.dto.SkuLogDTO;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuLog;
import org.nodes.wms.dao.basics.sku.enums.SkuLogTypeEnum;
import org.nodes.wms.core.basedata.mapper.SkuLogMapper;
import org.nodes.wms.core.basedata.service.ISkuLogService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 物品操作记录表 服务实现类
 *
 * @author pengwei
 * @since 2020-06-29
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuLogServiceImpl<M extends SkuLogMapper, T extends SkuLog>
	extends BaseServiceImpl<SkuLogMapper, SkuLog>
	implements ISkuLogService {

	@Override
	public synchronized boolean saveOrUpdate(@NotNull @Validated SkuLogDTO skuLogDTO) {
		boolean result = false;
		// 获取物品信息
		Sku sku = SkuCache.getById(skuLogDTO.getSkuId());
		if (Func.isEmpty(sku)) {
			return false;
		}
		SkuLog skuLog = super.getOne(Condition.getQueryWrapper(new SkuLog())
			.lambda()
			.eq(SkuLog::getSkuId, skuLogDTO.getSkuId()));
		LocalDateTime nowTime = LocalDateTime.now();
		if (Func.isNotEmpty(skuLog)) {
			UpdateWrapper<SkuLog> updateWrapper = new UpdateWrapper<>();
			if (skuLogDTO.getSkuLogTypeEnum().equals(SkuLogTypeEnum.INSTOCK)) {
				updateWrapper.lambda().set(SkuLog::getLastInTime, nowTime);
			} else if (skuLogDTO.getSkuLogTypeEnum().equals(SkuLogTypeEnum.OUTSTOCK)) {
				updateWrapper.lambda()
					.set(SkuLog::getOutCount, skuLog.getOutCount() + 1)
					.set(SkuLog::getLastOutTime, nowTime);
				if (Func.isEmpty(skuLog.getFirstOutTime())) {
					updateWrapper.lambda().set(SkuLog::getFirstOutTime, nowTime);
				}
			}
			updateWrapper.lambda().eq(SkuLog::getWslId, skuLog.getWslId());
			result = super.update(updateWrapper);
		} else {
			skuLog = new SkuLog();
			skuLog.setSkuId(sku.getSkuId());
			skuLog.setSkuCode(sku.getSkuCode());
			skuLog.setSkuName(sku.getSkuName());
			skuLog.setFirstInTime(nowTime);
			skuLog.setLastInTime(nowTime);
			result = super.save(skuLog);
		}

		return result;
	}
}
