package org.nodes.wms.core.instock.asn.service;

import org.nodes.wms.core.instock.asn.entity.Sn;
import org.nodes.wms.core.instock.asn.vo.SnVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 入库货品序列号 服务类
 *
 * @author NodeX
 * @since 2020-01-15
 */
public interface ISnService extends BaseService<Sn> {
	/**
	 * 通过单据id和物品编码查询已经清点的序列号
	 *
	 * @return
	 */
	List<Sn> selectSnListBySku(SnVO snVO);
}
