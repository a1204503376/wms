package org.nodes.wms.biz.instock.receiveLog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.receive.dto.input.PdaByPieceReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaRequest;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnItemDto;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveLogPageQuery;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogPageResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 清点记录业务层接口
 */
public interface ReceiveLogBiz {
	/**
	 * 根据收货单id获取清点记录集合
	 *
	 * @param receiveId 收货单id
	 */
	List<ReceiveLogResponse> getReceiveLogList(Long receiveId);

	/**
	 * 查找7天内入库量前10的物品
	 *
	 * @return List<ReceiveLogIndexResponse>
	 */
	List<ReceiveLogIndexResponse> findReceiveSkuQtyTop10();

	/**
	 * 生成清点记录
	 *
	 * @param request 请求对象
	 * @return 生成的清点记录
	 */
	ReceiveLog newReceiveLog(PdaByPieceReceiveRequest request, ReceiveHeader receiveHeader, ReceiveDetail detail);

	/**
	 * 分页查询
	 *
	 * @param query:               分页参数
	 * @param receiveLogPageQuery: 分页查询条件参数
	 * @return Page<ReceiveLogPageResponse>
	 */
	Page<ReceiveLogPageResponse> page(Query query, ReceiveLogPageQuery receiveLogPageQuery);

	/**
	 * 导出
	 *
	 * @param receiveLogPageQuery: 导出时条件参数
	 * @param response:            响应对象
	 */
	void exportExcel(ReceiveLogPageQuery receiveLogPageQuery, HttpServletResponse response);

	/**
	 * 按箱收货生成清点记录
	 *
	 * @param request 传入参数
	 * @param item    明细相关信息
	 * @param lpn
	 * @return
	 */
	ReceiveLog newReceiveLog(ReceiveDetailLpnPdaRequest request, ReceiveDetailLpnItemDto item, ReceiveDetailLpn lpn, ReceiveHeader receiveHeader, ReceiveDetail detail);

	/**
	 * 撤销收货
	 *
	 * @param idList: 收货记录id集合
	 * @return true: 撤销成功，false: 撤销失败
	 */
	boolean cancelReceive(List<Long> idList);
}
