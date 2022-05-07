package org.nodes.wms.biz.instock.receive.header;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.instock.receive.header.dto.input.ReceiveHeaderPageQuery;
import org.nodes.wms.dao.instock.receive.header.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.instock.receive.header.dto.output.ReceiveHeaderDetailResponse;
import org.nodes.wms.dao.instock.receive.header.dto.output.ReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.log.dto.output.ReceiveLogResponse;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 收货单业务层接口
 */
public interface ReceiveBiz {


	IPage<ReceiveHeaderResponse> getPage(ReceiveHeaderPageQuery receiveHeaderPageQuery, Query query);

	boolean remove(Long receiveId);

	ReceiveHeaderDetailResponse detail(Long receiveId);

	boolean editBillState(Long receiveDetailId);

	List<ReceiveLogResponse> queryLog(Long receiveId);

	boolean newReceive(NewReceiveRequest newReceiveRequest);
}
