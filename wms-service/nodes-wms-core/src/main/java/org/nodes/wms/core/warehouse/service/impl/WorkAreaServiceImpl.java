
package org.nodes.wms.core.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.warehouse.entity.WorkArea;
import org.nodes.wms.core.warehouse.mapper.WorkAreaMapper;
import org.nodes.wms.core.warehouse.service.IWorkAreaService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作区服务实现类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class WorkAreaServiceImpl<M extends WorkAreaMapper, T extends WorkArea>
	extends BaseServiceImpl<WorkAreaMapper, WorkArea>
	implements IWorkAreaService {

	@Override
	public boolean save(WorkArea workArea) {
		LambdaQueryWrapper queryWrapper = Condition.getQueryWrapper(new WorkArea()).lambda()
			.eq(WorkArea::getWhId, workArea.getWhId())
			.eq(WorkArea::getWwaName, workArea.getWwaName());
		if (super.count(queryWrapper) > 0) {
			throw new ServiceException("同一库房内工作区名称不可重复！");
		}
		workArea.setSyncState(0);

		return super.save(workArea);
	}
	@Override
	public boolean updateById(WorkArea workArea) {
		QueryWrapper<WorkArea> workAreaQueryWrapper = new QueryWrapper<>();
		workAreaQueryWrapper.eq("wh_id", workArea.getWhId());
		workAreaQueryWrapper.eq("wwa_name", workArea.getWwaName());
		workAreaQueryWrapper.ne("wwa_id", workArea.getWwaId());
		if (baseMapper.selectList(workAreaQueryWrapper).size() > 0) {
			throw new ServiceException("同一库房内工作区名称不可重复！");
		}
		super.updateById(workArea);
		return true;
	}

	@Override
	public boolean saveOrUpdate(WorkArea workArea) {
		if (Func.isEmpty(workArea.getWwaId())) {
			this.save(workArea);
		} else {
			this.updateById(workArea);
		}
		return true;
	}
}
