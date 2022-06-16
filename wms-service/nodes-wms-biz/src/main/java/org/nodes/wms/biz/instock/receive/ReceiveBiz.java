package org.nodes.wms.biz.instock.receive;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.instock.receive.dto.input.EditReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePageQuery;
import org.nodes.wms.dao.instock.receive.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePdaQuery;
import org.nodes.wms.dao.instock.receive.dto.output.EditReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderPdaResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 收货单业务层接口
 */
public interface ReceiveBiz {

	/**
	 * 获取收货单列表(分页)
	 * @param receivePageQuery :收货单分页查询条件对象
	 * @param query :分页查询参数
	 */
	IPage<ReceiveHeaderResponse> getPage(ReceivePageQuery receivePageQuery, Query query);

	/**
	 * 根据收货单ID删除收货单(逻辑删除)
	 * @param receiveIdList :收货单ID集合
	 * @return true:删除成功, false:删除失败
	 */
	boolean remove(List<Long> receiveIdList);

	/**
	 * 根据收货单ID获取收货单明细
	 * @param receiveId :收货单ID
	 * @return ReceiveResponse:收货单明细返回对象
	 */
	ReceiveResponse getReceiveDetail(Long receiveId);

	/**
	 * 根据收货单ID修改收货单状态
	 * @param receiveId :收货单ID
	 * @return  true:修改成功, false:修改失败
	 */
	boolean editBillState(Long receiveId);


	/**
	 * 新建收货单
	 * @param newReceiveRequest :新增收货单dto
	 * @return true:新增成功, false:新增失败
	 */
	ReceiveHeader newReceive(NewReceiveRequest newReceiveRequest);

	/**
	 * 导出收货单列表
	 * @param receivePageQuery : 收货单查询条件对象
	 * @param response : 响应对象
	 */
    void exportExcel(ReceivePageQuery receivePageQuery, HttpServletResponse response);

	/**
	 * 过去编辑页面返回对象
	 * @param receiveId 收货单id
	 * @return ReceiveEditResponse
	 */
	EditReceiveResponse getEditReceiveResponse(Long receiveId);

	String editReceive(EditReceiveRequest editReceiveRequest);


	/**
	 * PDA获取收货单列表
	 * @param receivePageQuery :收货单分页查询条件对象
	 */
	List<ReceiveHeaderPdaResponse> getReceiveListByReceiveNo(ReceivePdaQuery receivePdaQuery);
}
