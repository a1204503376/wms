package org.nodes.wms.biz.receive.header;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.receive.header.dto.input.HeaderPageQuery;
import org.nodes.wms.dao.receive.header.dto.input.NewReceiveRequest;
import org.nodes.wms.dao.receive.header.dto.output.HeaderDetailResponse;
import org.nodes.wms.dao.receive.header.dto.output.HeaderResponse;
import org.nodes.wms.dao.receive.log.dto.output.LogResponse;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 收货单业务层接口
 */
public interface ReceiveBiz {


	IPage<HeaderResponse> getPage(HeaderPageQuery headerPageQuery, Query query);

	boolean remove(Long receiveId);

	HeaderDetailResponse detail(Long receiveId);

	boolean edit(Long receiveDetailId);

	List<LogResponse> queryLog(Long receiveId);

	boolean newReceive(NewReceiveRequest newReceiveRequest);
}
