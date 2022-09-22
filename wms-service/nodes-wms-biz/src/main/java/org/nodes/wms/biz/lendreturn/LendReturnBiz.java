package org.nodes.wms.biz.lendreturn;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.output.LendReturnResponse;
import org.nodes.wms.dao.lendreturn.dto.output.NoReturnResponse;
import org.nodes.wms.dao.lendreturn.entities.LogLendReturn;
import org.nodes.wms.dao.lendreturn.entities.LogNoReturn;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 借出归还 业务类
 */
public interface LendReturnBiz {

	/**
	 * 保存借出归还记录
	 */
	void saveLog(LendReturnRequest lendReturnRequest);

	/**
	 * 分页查询借出归还记录
	 */
	Page<LendReturnResponse> pageLendReturn(Page<LogLendReturn> page, LendReturnQuery lendReturnQuery);

	/**
	 * 分页查询未归还记录
	 */
	Page<NoReturnResponse> pageNoReturn(Page<LogNoReturn> page, LendReturnQuery lendReturnQuery);

	/**
	 * 未归还记录： 服务端导出
	 *
	 * @param lendReturnQuery 导出时条件
	 * @param response 响应对象
	 */
	void exportNoReturn(LendReturnQuery lendReturnQuery, HttpServletResponse response);

	/**
	 * 根据发货明细id删除借出归还记录和未归还记录
	 *
	 * @param soDetailId 发货明细id
	 * @return true: 删除成功，false: 删除失败
	 */
    boolean removeBySoDetailId(Long soDetailId);

	/**
	 * 归还入库，保存借出归还记录
	 *
	 * @param billType 收货单单据类型
	 * @param receiveLogList 清点记录
	 */
	void saveReturnLog(String billType, List<ReceiveLog> receiveLogList);

	/**
	 * 借出出库，保存借出归还记录
	 *
	 * @param billType 发货单单据类型
	 * @param logSoPickList 拣货记录
	 */
	void saveLendLog(String billType, List<LogSoPick> logSoPickList);
}
