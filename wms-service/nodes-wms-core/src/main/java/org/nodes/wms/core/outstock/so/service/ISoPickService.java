package org.nodes.wms.core.outstock.so.service;

import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.vo.PickLogVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 拣货记录日志 服务类
 *
 * @author zx
 * @since 2020-03-04
 */
public interface ISoPickService extends BaseService<SoPick> {
	/**
	 * 获得拣货日志
	 * @param list
	 * @return
	 */
	List<PickLogVO> getPickLog(List<SoPick> list);

}
