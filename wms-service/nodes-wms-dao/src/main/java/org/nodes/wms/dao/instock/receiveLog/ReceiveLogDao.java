package org.nodes.wms.dao.instock.receiveLog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveLogPageRequest;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogPageResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;

import java.util.List;

/**
 * 清点记录Dao接口
 */
public interface ReceiveLogDao {
	/**
	 * 根据收货单id获取清点记录
	 * @param receiveId 收货单id
	 */
	List<ReceiveLogResponse> getReceiveLogList(Long receiveId);

	/**
	 * 获取7天内入库量前10的物品
	 *
	 * @return List<ReceiveLogIndexResponse>
	 */
    List<ReceiveLogIndexResponse> getReceiveSkuQtyTop10();

	/**
	 * 分页查询
	 *
	 * @param page: 分页参数
	 * @param receiveLogPageRequest: 分页查询条件参数
	 * @return Page<ReceiveLogPageResponse>
	 */
    Page<ReceiveLogPageResponse> page(IPage<?> page, ReceiveLogPageRequest receiveLogPageRequest);
}
