package org.nodes.wms.dao.outstock.logSoPick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
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
}
