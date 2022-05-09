package org.nodes.wms.biz.instock.receive.header;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveHeaderPageQuery;
import org.nodes.wms.dao.instock.receive.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 收货单业务层接口
 */
public interface ReceiveBiz {


	IPage<ReceiveHeaderResponse> getPage(ReceiveHeaderPageQuery receiveHeaderPageQuery, Query query);

	boolean remove(Long receiveId);

	ReceiveResponse detail(Long receiveId);

	boolean editBillState(Long receiveDetailId);



	boolean newReceive(NewReceiveRequest newReceiveRequest);

    void exportExcel(ReceiveHeaderPageQuery receiveHeaderPageQuery, HttpServletResponse response);
}
