package org.nodes.wms.biz.outstock.so.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.outstock.so.SoHeaderBiz;
import org.nodes.wms.dao.outstock.so.SoHeaderDao;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderPageResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发货单业务接口实现类
 **/
@Service
@RequiredArgsConstructor
public class SoHeaderBizImpl implements SoHeaderBiz {

	private final SoHeaderDao soHeaderDao;

	@Override
	public Page<SoHeaderPageResponse> getPage(Query query, SoHeaderPageQuery soHeaderPageQuery) {
		return soHeaderDao.page(Condition.getPage(query), soHeaderPageQuery);
	}
}
