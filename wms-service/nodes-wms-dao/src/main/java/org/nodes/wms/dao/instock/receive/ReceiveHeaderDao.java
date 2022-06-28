package org.nodes.wms.dao.instock.receive;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePdaQuery;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderPdaResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePageQuery;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;

import java.util.List;

/**
 * 收货单 DAO 接口
 */
public interface ReceiveHeaderDao {
	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param receivePageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	IPage<ReceiveHeaderResponse> selectPage(IPage<ReceiveHeaderResponse>page, ReceivePageQuery receivePageQuery);

	boolean delete(List<Long> receiveIdList);

	DetailReceiveHeaderResponse selectHeaderById(Long receiveId);

	boolean updateBillStateById(Long receiveDetailId);

	boolean insert(ReceiveHeader receiveHeader);

    List<ReceiveHeaderResponse> getReceiveHeaderResponseByQuery(ReceivePageQuery receivePageQuery);

	ReceiveHeader selectReceiveHeaderById(Long receiveId);

	void updateReceive(ReceiveHeader receiveHeader);

	ReceiveHeader selectBillStateById(Long receiveId);

	/**
	 * 获取收货单列表页面收货单头
	 * @param receivePdaQuery 请求参数
	 * @return IPage<PageResponse>
	 */
	Page<ReceiveHeaderPdaResponse> getReceiveList(ReceivePdaQuery receivePdaQuery, IPage<ReceiveHeader> page);

	/**
	 * 根据用户id获取收货单集合
	 * @param userId 用户id
	 * @return 收货单集合
	 */
	List<ReceiveHeader> selectReceiveListByNonOrder(Long userId);
	/**
	 * 更新收货单头表
	 * @param header 收货单头表数据
	 * @return 是否成功
	 */
	Boolean updateReceiveHeader(ReceiveHeader header);

	/**
	 * 保存收货单头表实体
	 * @param receiveHeader 收货单头表实体
	 */
	void saveReceiveHeader(ReceiveHeader receiveHeader);
}
