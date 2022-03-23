
package org.nodes.wms.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.system.entity.Task;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Primary
public interface TaskMapper extends BaseMapper<Task> {

	/**
	 * 根据订单编号模糊查询拣货任务
	 * @param billNo
	 * @return
	 */
	public List<Task> getTakesBybillNo(String billNo);
}
