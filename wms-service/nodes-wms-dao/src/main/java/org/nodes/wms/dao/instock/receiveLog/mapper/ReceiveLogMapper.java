package org.nodes.wms.dao.instock.receiveLog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveLogPageQuery;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogExcelResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogPageResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;

import java.util.List;

public interface ReceiveLogMapper extends BaseMapper<ReceiveLog> {
	List<ReceiveLogResponse> selectReceiveLogList(Long receiveId);

	/**
	 * 获取7天内入库量前10的物品
	 *
	 * @return List<ReceiveLogIndexResponse>
	 */
	List<ReceiveLogIndexResponse> selectReceiveSkuQtyTop10();

	/**
	 * 分页查询
	 *
	 * @param page: 分页参数
	 * @param receiveLogPageQuery: 分页查询条件参数
	 * @return Page<ReceiveLogPageResponse>
	 */
    Page<ReceiveLogPageResponse> page(IPage<?> page, @Param("param") ReceiveLogPageQuery receiveLogPageQuery);

	/**
	 * 根据若干条件查询库位信息
	 *
	 * @param receiveLogPageQuery: 查询条件
	 * @return List<ReceiveLogPageResponse>
	 */
	List<ReceiveLogExcelResponse> selectReceiveLogListByQuery(@Param("param") ReceiveLogPageQuery receiveLogPageQuery);
}
