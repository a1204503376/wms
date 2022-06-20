
package org.nodes.wms.dao.system.updateVer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerPageQuery;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerExportResponse;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerPageResponse;
import org.nodes.wms.dao.system.updateVer.entities.UpdateVer;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author NodeX
 * @since 2020-02-20
 */
public interface UpdateVerMapper extends BaseMapper<UpdateVer> {

	/**
	 * 分页查询
	 *
	 * @param page: 分页对象
	 * @param updateVerPageQuery: 查询条件dto对象
	 * @return List<UpdateVerPageResponse>
	 */
	Page<UpdateVerPageResponse> selectUpdateVerPage(IPage<?> page, UpdateVerPageQuery updateVerPageQuery);

	List<UpdateVerExportResponse> selectUpdateByQuery(UpdateVerPageQuery updateVerPageQuery);
}
