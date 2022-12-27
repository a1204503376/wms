package org.nodes.wms.dao.putaway;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.putaway.dto.input.PutawayPageQuery;
import org.nodes.wms.dao.putaway.dto.output.PutawayLogExcelResponse;
import org.nodes.wms.dao.putaway.entities.PutawayLog;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 上架记录Dao接口
 */

public interface PutawayLogDao extends BaseService<PutawayLog> {

	/**
	 * 获取上架记录日志
	 *
	 * @param page             分页参数
	 * @param putawayPageQuery 条件
	 * @return
	 */
	IPage<PutawayLog> getPage(IPage<PutawayLog> page, PutawayPageQuery putawayPageQuery);

	/**
	 * 根据Query参数查询上架记录
	 *
	 * @param queryParam 查询参数
	 * @return 上架记录
	 */
	List<PutawayLogExcelResponse> getListForExport(PutawayPageQuery queryParam);
}
