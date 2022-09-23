package org.nodes.wms.dao.instock.receiveLog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveLogPageQuery;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogExcelResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogPageResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 清点记录Dao接口
 */
public interface ReceiveLogDao extends BaseService<ReceiveLog> {
	/**
	 * 根据收货单id获取清点记录
	 * @param receiveId 收货单id
	 */
	List<ReceiveLogResponse> getReceiveLogList(Long receiveId);

	/**
	 * 获取7天内入库量前10的物品
	 *
	 * @return List<ReceiveLogIndexResponse>
	 */
    List<ReceiveLogIndexResponse> getReceiveSkuQtyTop10();

	/**
	 * 分页查询
	 *
	 * @param page: 分页参数
	 * @param receiveLogPageQuery: 分页查询条件参数
	 * @return Page<ReceiveLogPageResponse>
	 */
    Page<ReceiveLogPageResponse> page(IPage<?> page, ReceiveLogPageQuery receiveLogPageQuery);

	/**
	 * 根据若干条件查询库位信息
	 *
	 * @param receiveLogPageQuery: 查询条件
	 * @return List<ReceiveLogExcelResponse>
	 */
	 List<ReceiveLogExcelResponse> getReceiveLogListByQuery(ReceiveLogPageQuery receiveLogPageQuery);

	/**
	 * 生成清点记录
	 * @param receiveLog 清点记录
	 * @return 是否成功
	 */
	boolean save(ReceiveLog receiveLog);

	/**
	 * 批量保存
	 *
	 * @param receiveLogList: 清点记录
	 * @return true保存成功，false保存失败
	 */
	boolean saveBatch(List<ReceiveLog> receiveLogList);

	/**
	 * 根据id集合获取收货记录集合
	 *
	 * @param idList: id集合
	 * @return 收货记录集合
	 */
	List<ReceiveLog> getByIds(List<Long> idList);

	/**
	 * 根据收货单id查询清点记录
	 *
	 * @param receiveId 收货单头表id
	 * @return 清点记录集合
	 */
    List<ReceiveLog> getByReceiveId(Long receiveId);

	/**
	 * 标记收货记录为已撤销
	 *
	 * @param receiveLog 收货记录
	 */
	void setCanceled(ReceiveLog receiveLog);
}
