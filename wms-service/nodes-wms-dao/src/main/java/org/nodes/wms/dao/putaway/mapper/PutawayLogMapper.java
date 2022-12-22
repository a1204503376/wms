package org.nodes.wms.dao.putaway.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.putaway.dto.output.PutawayLogExcelResponse;
import org.nodes.wms.dao.putaway.entities.PutawayLog;

import java.util.List;

/**
 * 上架记录mapper层接口
 */
public interface PutawayLogMapper extends BaseMapper<PutawayLog> {

	/**
	 * 根据条件构造器中的条件查询工作任务
	 *
	 * @param wrapper 条件构造器
	 * @return 工作任务
	 */
    List<PutawayLogExcelResponse> listByWrapper(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
