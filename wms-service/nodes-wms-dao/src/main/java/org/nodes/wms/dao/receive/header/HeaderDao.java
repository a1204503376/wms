package org.nodes.wms.dao.receive.header;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.receive.header.dto.input.HeaderPageQuery;
import org.nodes.wms.dao.receive.header.dto.output.HeaderResponse;
import org.nodes.wms.dao.receive.header.entities.ReceiveHeader;

/**
 * 收货单 DAO 接口
 */
public interface HeaderDao {
	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param headerPageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	IPage<HeaderResponse> selectPage(IPage<HeaderResponse>page, HeaderPageQuery headerPageQuery);

	boolean delete(Long receiveId);

	HeaderResponse selectHeaderById(Long receiveId);

	boolean updateStatusById(Long receiveDetailId);

	boolean insert(ReceiveHeader receiveHeader);
}
