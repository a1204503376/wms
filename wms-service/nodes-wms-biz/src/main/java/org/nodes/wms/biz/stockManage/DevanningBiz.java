package org.nodes.wms.biz.stockManage;

import org.nodes.wms.dao.stock.dto.input.DevanningSubmitRequest;
import org.nodes.wms.dao.stock.dto.input.FindAllSerialNumberManageRequest;
import org.nodes.wms.dao.stock.dto.output.FindAllSerialNumberManageResponse;

public interface DevanningBiz {
	/**
	 * 根据箱码查询序列号列表/库存列表
	 *
	 * @param request 箱码
	 * @return 响应对象
	 */
	FindAllSerialNumberManageResponse getAllSerialNumberManage(FindAllSerialNumberManageRequest request);

	/**
	 * 根据序列号列表/物品集合 拆箱
	 *
	 * @param request 包含序列号集合 目标库位编码/物品集合 箱码 是否整箱上架
	 */
	void devanning(DevanningSubmitRequest request);
}
