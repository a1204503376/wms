package org.nodes.wms.dao.instock.receive;

import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveByPcQuery;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailPdaQuery;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailPdaResponse;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveDetailResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailByPcResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;

import java.util.List;

/**
 * 收货单明细 DAO 接口
 */
public interface ReceiveDetailDao {
	List<DetailReceiveDetailResponse> selectDetailById(Long receiveId);


	List<ReceiveDetailRequest> selectDetailByHeaderId(Long receiveId);

	boolean delete(List<Long> ids);

	List<Long> selectDetailIdByReceiveId(Long receiveId);

	boolean insert(ReceiveDetail receiveDetail);

	List<ReceiveDetail> selectReceiveDetailById(Long receiveId);


	void saveOrUpdateReceiveDetail(ReceiveDetail receiveDetail);

	/**
	 * @param receiveDetailPdaQuery 收货单接收前端请求条件
	 * @return 收货单明细表集合
	 */
	List<DetailReceiveDetailPdaResponse> selectDetailListByReceiveId(@Param("query") ReceiveDetailPdaQuery receiveDetailPdaQuery);

	/**
	 * @param receiveDetailId 收货明细单主键id
	 * @return pda收货页面获取收货的详细信息
	 */
	ReceiveDetail getDetailByReceiveDetailId(Long receiveDetailId);

	/**
	 * 根据id修改收货单详情
	 *
	 * @param detail 收货单详情
	 * @return 是否成功
	 */
	void updateReceiveDetail(ReceiveDetail detail);

	/**
	 * 根据收货单id查询最新一条明细的订单行号
	 *
	 * @param receiveId 收货单id
	 * @return 最新一条明细的订单行号
	 */
	String selectReceiveDetailLinNo(Long receiveId);

	/**
	 * pc收货获取返回前端明细对象
	 *
	 * @param receiveByPcQuery 查询参数
	 */
	ReceiveDetailByPcResponse getReceiveDetailByPcResponse(ReceiveByPcQuery receiveByPcQuery);

	/**
	 * 根据明细id集合获取明细集合
	 *
	 * @param detailIdList 明细id集合
	 * @return 明细实体集合
	 */
	List<ReceiveDetail> findByIDList(List<Long> detailIdList);

	/**
	 * 根据id删除收货明细
	 *
	 * @param id: 收货明细id
	 * @return true: 删除成功，false: 删除失败
	 */
	boolean deleteById(Long id);


}
