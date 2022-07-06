package org.nodes.wms.dao.outstock.so;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderPageResponse;

import java.util.List;

/**
 * 发货单Dao接口
 **/
public interface SoHeaderDao {

	/**
	 * 分页
	 *
	 * @param page: 分页参数
	 * @param soHeaderPageQuery: 分页查询请求对象
	 * @return Page<SoHeaderPageResponse>
	 */
	Page<SoHeaderPageResponse> page(IPage<?> page, SoHeaderPageQuery soHeaderPageQuery);
}
