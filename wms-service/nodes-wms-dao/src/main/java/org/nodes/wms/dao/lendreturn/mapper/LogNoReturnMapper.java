package org.nodes.wms.dao.lendreturn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.lendreturn.entities.LogNoReturn;

import java.util.List;

/**
 * 未归还日志Mapper接口
 */
public interface LogNoReturnMapper extends BaseMapper<LogNoReturn> {

	/**
	 * 根据发货明细id删除未归还记录
	 *
	 * @param soDetailId 发货明细id
	 * @return true: 删除成功 false: 删除失败
	 */
    boolean deleteBySoDetailId(@Param("soDetailId") Long soDetailId);

	/**
	 * 根据未归还列表id删除未归还列表
	 *
	 * @param noReturnIdList 未归还明细id
	 * @return true: 删除成功, false: 删除失败
	 */
    boolean deleteByIds(@Param("ids") List<Long> noReturnIdList);
}
