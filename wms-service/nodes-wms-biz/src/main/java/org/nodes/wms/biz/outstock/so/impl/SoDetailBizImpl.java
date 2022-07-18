package org.nodes.wms.biz.outstock.so.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.outstock.so.SoDetailBiz;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.SoDetailDao;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailForDetailResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 **/
@Service
@RequiredArgsConstructor
public class SoDetailBizImpl implements SoDetailBiz {

	private final SoDetailDao soDetailDao;

	@Override
	public Page<SoDetailForDetailResponse> pageSoDetailForDetailBySoBillId(Query query, SoBillIdRequest soBillIdRequest) {
		return soDetailDao.pageForSoDetailBySoBillId(Condition.getPage(query), soBillIdRequest.getSoBillId());
	}

    @Override
    public Page<NotSoPickPageResponse> pageNotSoPick(Query query, NotSoPickPageQuery notSoPickPageQuery) {
        return soDetailDao.pageNotSoPick(Condition.getPage(query), notSoPickPageQuery);
    }

	@Override
	public void exportNotSoPick(NotSoPickPageQuery notSoPickPageQuery, HttpServletResponse response) {
		List<NotSoPickExcelResponse> notSoPickList = soDetailDao.notSoPickListByQuery(notSoPickPageQuery);
		ExcelUtil.export(response, notSoPickList, NotSoPickExcelResponse.class);
	}
}
