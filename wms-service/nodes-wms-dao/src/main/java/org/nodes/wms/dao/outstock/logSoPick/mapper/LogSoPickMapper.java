package org.nodes.wms.dao.outstock.logSoPick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.LogSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPicExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickPageResponse;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 拣货记录日志mapper接口
 **/
@Repository
public interface LogSoPickMapper extends BaseMapper<LogSoPick> {

	/**
	 * 查询7天内拣货数量前10的物品
	 *
	 * @return List<LogSoPickIndexResponse>
	 */
	List<LogSoPickIndexResponse> selectPickSkuQtyTop10();

	/**
	 * 出库单查看明细：根据出库单id分页查询获取拣货记录日志信息
	 *
	 * @param soBillId: 出库单id
	 * @param page 分页参数
	 * @return Page<LogSoPickForSoDetailResponse> 出库单查看明细 拣货记录日志信息分页响应对象
	 */
    Page<LogSoPickForSoDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, @Param("soBillId") Long soBillId);

	/**
	 * 分页查询
	 *
	 * @param page:              分页参数
	 * @param logSoPickPageQuery: 分页条件参数
	 * @return Page<LogSoPickPageResponse> 拣货记录发分页查询响应对象
	 */
    Page<LogSoPickPageResponse> page(IPage<?> page, @Param("param") LogSoPickPageQuery logSoPickPageQuery);

	/**
	 * 根据Query条件查询发货日志记录
	 *
	 * @param logSoPickPageQuery: 查询条件
	 * @return List<LogSoPicExcelResponse>
	 */
	List<LogSoPicExcelResponse> listByQuery(@Param("param") LogSoPickPageQuery logSoPickPageQuery);
}
