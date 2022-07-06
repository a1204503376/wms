package org.nodes.wms.biz.putway.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.putway.PutwayBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.putway.dto.input.NewPutawayByBoxlRequest;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PutwayBizImpl implements PutwayBiz {

	@Override
	public void addByBoxShelf(NewPutawayByBoxlRequest request) {
		// 调用库存移动，如果关联了序列号需要获取序列号
		// 生成上架记录
	}
}
