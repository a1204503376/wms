package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.stock.dto.input.SerialPageQuery;
import org.nodes.wms.dao.stock.dto.output.SerialExcelResponse;
import org.nodes.wms.dao.stock.dto.output.SerialPageResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 序列号mapper接口
 **/
@Repository("stockSerialRepository")
public interface SerialMapper extends BaseMapper<Serial> {


	/**
	 * 分页查询
	 *
	 * @param serialPageQuery 条件参数
	 * @param page            分页参数
	 * @return 序列号分页对象
	 */
	Page<SerialPageResponse> getPage(@Param("param") SerialPageQuery serialPageQuery, IPage<?> page);

	/**
	 * 导出Excel
	 *
	 * @param serialPageQuery 条件参数
	 * @return 导出的数据
	 */
	List<SerialExcelResponse> listByQuery(@Param("param") SerialPageQuery serialPageQuery);
}
