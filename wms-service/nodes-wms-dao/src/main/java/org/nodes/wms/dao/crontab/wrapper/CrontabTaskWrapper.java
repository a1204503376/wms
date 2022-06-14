package org.nodes.wms.dao.crontab.wrapper;

import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.nodes.wms.dao.crontab.vo.CrontabTaskVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;


/**
 * 任务表包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-01-22
 */
public class CrontabTaskWrapper extends BaseEntityWrapper<CrontabTask, CrontabTaskVO>  {

	public static CrontabTaskWrapper build() {
		return new CrontabTaskWrapper();
 	}

	@Override
	public CrontabTaskVO entityVO(CrontabTask crontabTask) {
		CrontabTaskVO taskVO = BeanUtil.copy(crontabTask, CrontabTaskVO.class);

		return taskVO;
	}
}
