package org.nodes.wms.biz.outstock.so;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.nodes.wms.dao.outstock.so.dto.output.LineNoAndSkuSelectResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailForDetailResponse;
import org.nodes.wms.dao.stock.dto.output.SerialSelectResponse;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 发货单明细业务接口
 **/
public interface SoDetailBiz {

	/**
	 * 查看明细：根据发货单id分页查询发货单明细信息
	 *
	 * @param soBillIdRequest: 发货单id请求对象
	 * @param query:           分页参数
	 * @return Page<SoDetailForDetailResponse> 发货单明细分页信息
	 */
	Page<SoDetailForDetailResponse> pageSoDetailForDetailBySoBillId(Query query, SoBillIdRequest soBillIdRequest);

	/**
	 * 分页查询未发货记录
	 *
	 * @param query:              分页参数
	 * @param notSoPickPageQuery: 分页查询条件
	 * @return Page<NotSoPickPageResponse> 未发货记录分页对象
	 */
	Page<NotSoPickPageResponse> pageNotSoPick(Query query, NotSoPickPageQuery notSoPickPageQuery);

	/**
	 * 导出未发货记录
	 *
	 * @param notSoPickPageQuery: 导出时条件
	 * @param response:           响应对象
	 */
	void exportNotSoPick(NotSoPickPageQuery notSoPickPageQuery, HttpServletResponse response);

	/**
	 * 获取出库明细行号和物料编码下拉列表集合
	 *
	 * @param soBillId 发货单id
	 * @return 行号下拉列表数据
	 */
	List<LineNoAndSkuSelectResponse> getLineNoAndSkuSelectList(Long soBillId);

	/**
	 * 获取序列号下拉列表
	 *
	 * @param stockId 仓库id
	 * @return 序列号集合
	 */
	List<SerialSelectResponse> getSerialSelectResponseList(Long stockId);
}
