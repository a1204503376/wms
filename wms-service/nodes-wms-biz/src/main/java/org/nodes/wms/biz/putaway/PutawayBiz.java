package org.nodes.wms.biz.putaway;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.putaway.dto.input.CallAgvRequest;
import org.nodes.wms.dao.putaway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.putaway.dto.input.PutawayPageQuery;
import org.nodes.wms.dao.putaway.dto.input.PutwayByBoxRequest;
import org.nodes.wms.dao.putaway.dto.output.LocResponse;
import org.nodes.wms.dao.putaway.entities.PutawayLog;
import org.springblade.core.mp.support.Query;

import java.util.List;

public interface PutawayBiz {
	/**
	 * 提交按箱上架
	 *
	 * @param request 请求对象
	 */
	void putawayByBox(PutwayByBoxRequest request);

	/**
	 * 天宜定制：呼叫Agv
	 *
	 * @param request 前端传入参数
	 */
	void callAgv(CallAgvRequest request);

	/**
	 * 天宜定制：呼叫Agv 根据库房id和箱型获取库位信息
	 *
	 * @param request 包含库房id和箱型
	 */
	List<LocResponse> findLocByLpnType(LpnTypeRequest request);

	/**
	 * 获取上架记录日志
	 *
	 * @param query            分页参数
	 * @param putawayPageQuery 条件
	 * @return
	 */
	IPage<PutawayLog> getPutawayLogPage(Query query, PutawayPageQuery putawayPageQuery);

}
