package org.nodes.wms.core.strategy.service;

import org.nodes.wms.core.strategy.dto.WellenDTO;
import org.nodes.wms.core.strategy.entity.Wellen;
import org.nodes.wms.core.strategy.vo.WellenVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 波次策略 服务类
 *
 * @author NodeX
 * @since 2021-05-26
 */
public interface IWellenService<T> extends BaseService<Wellen> {

	WellenVO getDetail(WellenDTO wellen);

	boolean saveOrUpdate(WellenDTO wellen);

	boolean removeByIds(List<Long> toLongList);
}
