package org.nodes.wms.dao.basics.lpntype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.lpntype.dto.input.LpnTypePageQuery;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeExcelResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypePageResponse;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;

import java.util.HashMap;
import java.util.List;

public interface LpnTypeMapper extends BaseMapper<LpnType> {
	/**
	 * @param page 分页对象
	 * @param lpnTypePageQuery 查询条件
	 * @return 分好页的数据
	 */
	Page<LpnTypePageResponse> getPage(IPage<?> page, @Param("query")LpnTypePageQuery lpnTypePageQuery);

	/**
	 * @param params 查询条件
	 * @return 导出的数据
	 */
	@SuppressWarnings("MybatisXMapperMethodInspection")
	List<LpnTypeExcelResponse> getLpnTypes(@Param("query") HashMap<String, Object> params);
}
