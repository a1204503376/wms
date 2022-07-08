package org.nodes.wms.biz.outstock.so;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillAddOrEditRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderPageResponse;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 发货单业务接口
 **/
public interface SoHeaderBiz {

	/**
	 * 分页查询
	 *
	 * @param query: 分页参数
	 * @param soHeaderPageQuery: 分页查询条件请求对象
	 * @return Page<SoHeaderPageResponse> 发货单头表分页响应对象
	 */
	Page<SoHeaderPageResponse> getPage(Query query, SoHeaderPageQuery soHeaderPageQuery);

	/**
	 * 新增发货单
	 *
	 * @param soBillAddOrEditRequest: 新增或编辑发货单请求对象
	 * @return SoHeader 发货单对象
	 */
    SoHeader add(SoBillAddOrEditRequest soBillAddOrEditRequest);
}
