package org.nodes.wms.dao.outstock.logSoPick;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;

import java.util.List;

/**
 * 拣货记录日志Dao接口
 **/
public interface LogSoPickDao {

	/**
	 * 获取7天内出库量前10的物品
	 *
	 * @return List<LogSoPickIndexResponse>
	 */
	List<LogSoPickIndexResponse> getPickSkuQtyTop10();

	/**
	 * 出库单查看明细：根据出库单id分页查询获取拣货记录日志信息
	 *
	 * @param soBillId: 出库单id
	 * @param page 分页参数
	 * @return Page<LogSoPickForSoDetailResponse> 出库单查看明细 拣货记录日志信息分页响应对象
	 */
	Page<LogSoPickForSoDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, Long soBillId);
}
