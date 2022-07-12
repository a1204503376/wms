package org.nodes.wms.biz.outstock.so;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailForDetailResponse;
import org.springblade.core.mp.support.Query;

/**
 * 出库单明细业务接口
 **/
public interface SoDetailBiz {

	/**
	 * 查看明细：根据出库单id分页查询出库单明细信息
	 *
	 * @param soBillIdRequest: 出库单id请求对象
	 * @param query: 分页参数
	 * @return Page<SoDetailForDetailResponse> 出库单明细分页信息
	 */
	Page<SoDetailForDetailResponse> pageSoDetailForDetailBySoBillId(Query query, SoBillIdRequest soBillIdRequest);
}
