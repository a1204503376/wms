package org.nodes.wms.dao.lendreturn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.lendreturn.entities.LogLendReturn;
import org.springframework.stereotype.Repository;

/**
 * 借出归还日志mapper接口
 **/
@Repository
public interface LogLendReturnMapper extends BaseMapper<LogLendReturn> {

	/**
	 * 根据发货明细id删除借出归还记录
	 *
	 * @param soDetailId: 发货明细id
	 * @return true: 删除成功 false: 删除失败
	 */
    boolean deleteBySoDetailId(@Param("soDetailId") Long soDetailId);
}
