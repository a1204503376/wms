package org.nodes.wms.biz.instock.receive;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.common.log.dto.output.LogReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.input.*;
import org.nodes.wms.dao.instock.receive.dto.output.*;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 收货单业务层接口
 */
public interface ReceiveBiz {

	/**
	 * 获取收货单列表(分页)
	 *
	 * @param receivePageQuery 收货单分页查询条件对象
	 * @param query            分页查询参数
	 * @return
	 */
	IPage<ReceiveHeaderResponse> getPage(ReceivePageQuery receivePageQuery, Query query);

	/**
	 * 根据收货单ID删除收货单(逻辑删除)
	 *
	 * @param receiveIdList :收货单ID集合
	 * @return true:删除成功, false:删除失败
	 */
	boolean remove(List<Long> receiveIdList);

	/**
	 * 根据收货单ID获取收货单明细
	 *
	 * @param receiveId :收货单ID
	 * @return ReceiveResponse:收货单明细返回对象
	 */
	ReceiveResponse getReceiveDetail(Long receiveId);

	/**
	 * 根据收货单ID修改收货单状态
	 *
	 * @param receiveId :收货单ID
	 * @return true:修改成功, false:修改失败
	 */
	boolean editBillState(Long receiveId);


	/**
	 * 新建收货单
	 *
	 * @param newReceiveRequest :新增收货单dto
	 * @return true:新增成功, false:新增失败
	 */
	ReceiveHeader newReceive(NewReceiveRequest newReceiveRequest);

	/**
	 * 导出收货单列表
	 *
	 * @param receivePageQuery : 收货单查询条件对象
	 * @param response         : 响应对象
	 */
	void exportExcel(ReceivePageQuery receivePageQuery, HttpServletResponse response);

	/**
	 * 过去编辑页面返回对象
	 *
	 * @param receiveId 收货单id
	 * @return ReceiveEditResponse
	 */
	EditReceiveResponse getEditReceiveResponse(Long receiveId);

	/**
	 * PC端编辑收货
	 *
	 * @param editReceiveRequest
	 * @return
	 */
	String editReceive(EditReceiveRequest editReceiveRequest);

	/**
	 * PDA获取收货单列表
	 *
	 * @param receivePdaQuery 收货单分页查询条件对象
	 * @param query
	 * @return
	 */
	Page<ReceiveHeaderPdaResponse> getReceiveListByReceiveNo(ReceivePdaQuery receivePdaQuery, Query query);

	/**
	 * @param receiveDetailPdaQuery 收货单接收前端请求条件
	 * @return 收货单明细表集合
	 */
	List<DetailReceiveDetailPdaResponse> getDetailListByReceiveId(ReceiveDetailPdaQuery receiveDetailPdaQuery);

	/**
	 * 根据收货单id获取操作日志
	 *
	 * @param receiveId 收货单id
	 */
	List<LogReceiveResponse> getLogList(Long receiveId);

	/**
	 * @param receiveIdPdaQuery 请求参数
	 * @return 当前收货单详情，以及他是否是序列号管理 isSn
	 */
	ReceiveDetailByReceiveIdPdaResponse selectDetailByReceiveDetailId(ReceiveDetailByReceiveIdPdaQuery receiveIdPdaQuery);

	/**
	 * 根据箱码获取lpn
	 *
	 * @param boxCode
	 * @return
	 */
	ReceiveDetailLpnPdaResponse getReceiveDetailLpnByBoxCode(String boxCode);

	/**
	 * 判断是否可以收货，如果不能收货则抛异常
	 *
	 * @param receiveDetailId 收货单明细id
	 * @param receiveQty      本次收货数量
	 */
	void canReceive(Long receiveDetailId, BigDecimal receiveQty);

	/**
	 * 收货时更新收货单明细和收货单头表状态
	 *
	 * @param receiveDetailId
	 * @param receiveQty
	 */
	void updateReceiveDetail(Long receiveDetailId, BigDecimal receiveQty);

	/**
	 * 收货之后更新收货单状态，注意该函数要在更新明细之后调用
	 *
	 * @param receiveDetailId
	 */
	void updateReciveHeader(Long receiveDetailId);

	/**
	 * 记录收货的业务日志，日志格式：[行号]收货[qty],批次[skuLotNumber]
	 *
	 * @param receiveHeaderId 收货单id
	 * @param receiveDetailId 收货单明细
	 * @param qty             收货数量
	 * @param skuLotNumber    批次号
	 */
	void log(Long receiveHeaderId, Long receiveDetailId,
			 BigDecimal qty, String skuLotNumber);

	/**
	 * 根据收货单id获取收货单详情
	 *
	 * @param receiveDetailId 收货单详情的id
	 * @return 收货单详情
	 */
	ReceiveDetail getDetailByReceiveDetailId(Long receiveDetailId);

	/**
	 * 根据收货单id获取收货单
	 *
	 * @param receiveId 收货单id
	 * @return 收货单数据
	 */
	ReceiveHeader selectReceiveHeaderById(Long receiveId);

	/**
	 * 根据用户id获取收货单集合
	 * @param userId 用户id
	 * @return 收货单集合
	 */
	List<ReceiveHeader> getReceiveListByNonOrder(Long userId);

	/**
	 * 保存收货单头表实体
	 * @param receiveHeader 收货单头表实体
	 */
	void newReceiveHeader(ReceiveHeader receiveHeader);

	/**
	 * 根据收货单id查询明细最新一条明细的订单行号
	 *
	 * @param receiveId 收货单id
	 * @return 最后一条明细的订单行号
	 */
	String getReceiveDetailLinNo(Long receiveId);

	/**
	 * 根据id获取lpn实体
	 * @param receiveDetailLpnId  id
	 * @return  lpn实体
	 */
	ReceiveDetailLpn getReceiveDetailLpnById(Long receiveDetailLpnId);

	/**
	 * 新建收货单明细
	 * @param receiveDetail 收货单明细实体
	 */
	void newReceiveDetail(ReceiveDetail receiveDetail);

	/**
	 * 修改lpn
	 * @param lpn 实体
	 */
	void updateReceiveDetailLpn(ReceiveDetailLpn lpn);

	/**
	 * 根据id获取收货单头表实体
	 * @param receiveHeaderId 收货单id
	 * @return  收货单头表实体
	 */
	ReceiveHeader getReceiveHeaderById(Long receiveHeaderId);

	/**
	 * @param receiveDetailId 收货单详情
	 * @param receiveId 收货单
	 * @return 收货是否完成
	 */
	PdaByPcsReceiveResponse checkByPcsReceive(Long receiveDetailId,Long receiveId);
}
