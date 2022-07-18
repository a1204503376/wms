package org.nodes.wms.biz.instock.receiveLog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.receive.dto.input.PdaByPieceReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaRequest;
import org.nodes.wms.dao.instock.receive.dto.output.EditReceiveDetailResponse;
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
	 * 是否可以撤销
	 *
	 * @param receiveIdList: 清点记录id
	 * @return List<ReceiveLog> 清点记录
	 */
	List<ReceiveLog> canCancelReceive(List<Long> receiveIdList);

	/**
	 * 生成清点记录
	 *
	 * @param receiveLogList: 清点记录
	 * @return List<ReceiveLog> 清点记录
	 */
	List<ReceiveLog> newReceiveLog(List<ReceiveLog> receiveLogList);

	List<ReceiveLog> findReceiveLog(List<Long> receiveIdList);

	/**
	 * 根据拣货日志记录id查找收货日志信息响应给前端
	 *
	 * @param lsopIdList: 拣货日志记录id
	 * @return List<EditReceiveDetailResponse> 收货单信息响应对象
	 */
	List<EditReceiveDetailResponse> findReceiveLogBylsopIds(List<Long> lsopIdList);

	/**
	 * 保存清点记录
	 *
	 * @param receiveLog 清点记录实体
	 */
	void saveReceiveLog(ReceiveLog receiveLog);
}
