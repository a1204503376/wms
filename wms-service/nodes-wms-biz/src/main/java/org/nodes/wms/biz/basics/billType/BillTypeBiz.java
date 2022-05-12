package org.nodes.wms.biz.basics.billType;

import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectQuery;
import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;

import java.util.List;

/**
 * 单据类型 业务层接口
 */
public interface BillTypeBiz {

	/**
	 * 根据关键词获取最近10个单据类型信息
	 * 根据修改时间倒序
	 *
	 * @param billTypeSelectQuery 关键词对象
	 * @return List<SkuSelectResponse>
	 */
	List<BillTypeSelectResponse> getBillTypeSelectResponseTop10List(BillTypeSelectQuery billTypeSelectQuery);
}
