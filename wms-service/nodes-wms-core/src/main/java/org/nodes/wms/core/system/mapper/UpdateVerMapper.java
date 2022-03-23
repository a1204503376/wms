
package org.nodes.wms.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.system.entity.UpdateVer;
import org.nodes.wms.core.system.vo.UpdateVerVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author NodeX
 * @since 2020-02-20
 */
@Primary
public interface UpdateVerMapper extends BaseMapper<UpdateVer> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param updateVer
	 * @return
	 */
	List<UpdateVerVO> selectUpdateVerPage(IPage page, UpdateVerVO updateVer);

}
