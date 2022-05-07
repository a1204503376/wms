package org.nodes.wms.dao.instock.receive.header;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.instock.receive.header.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.header.dto.input.ReceiveHeaderPageQuery;
import org.nodes.wms.dao.instock.receive.header.dto.output.ReceiveHeaderResponse;

/**
 * 收货单 DAO 接口
 */
public interface ReceiveHeaderDao {
	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param receiveHeaderPageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	IPage<ReceiveHeaderResponse> selectPage(IPage<ReceiveHeaderResponse>page, ReceiveHeaderPageQuery receiveHeaderPageQuery);

	boolean delete(Long receiveId);

	ReceiveHeaderResponse selectHeaderById(Long receiveId);

	boolean updateBillStateById(Long receiveDetailId);

	boolean insert(ReceiveHeader receiveHeader);
}
