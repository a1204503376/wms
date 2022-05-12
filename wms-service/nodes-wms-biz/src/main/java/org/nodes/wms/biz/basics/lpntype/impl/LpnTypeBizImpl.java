package org.nodes.wms.biz.basics.lpntype.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.lpntype.modular.LpnTypeFactory;
import org.nodes.wms.dao.basics.lpntype.LpnTypeDao;
import org.nodes.wms.dao.basics.lpntype.dto.input.DeleteLpnTypeRequest;
import org.nodes.wms.dao.basics.lpntype.dto.input.LpnTypePageQuery;
import org.nodes.wms.dao.basics.lpntype.dto.input.NewLpnTypeRequest;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeExcelResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypePageResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


/**
 * 容器管理业务类
 */
@Service
@RequiredArgsConstructor
public class LpnTypeBizImpl implements LpnTypeBiz {
	private final LpnTypeDao lpnTypeDao;
	private final LpnTypeFactory lpnTypeFactory;

	/**
	 * @param query            分页参数
	 * @param lpnTypePageQuery 条件
	 * @return page
	 */
	@Override
	public IPage<LpnTypePageResponse> getPage(Query query, LpnTypePageQuery lpnTypePageQuery) {
		IPage<LpnTypePageResponse> page = Condition.getPage(query);
		return lpnTypeDao.selectPage(page,lpnTypePageQuery);
	}

	/**
	 * @param lpnTypeRequest 新增容器的参数
	 * @return 成功/失败
	 */
	@Override
	public boolean newLpnType(NewLpnTypeRequest lpnTypeRequest) {
		boolean existCarrierCode = lpnTypeDao.isExistCarrierCode(lpnTypeRequest.getCode());
		if(existCarrierCode)
		{
			throw new ServiceException("新增容器失败，编码重复");
		}
		return lpnTypeDao.insert(lpnTypeFactory.createLpnType(lpnTypeRequest));
	}

	/**
	 * @param deleteRequest 删除容器对象id的集合
	 * @return 成功/失败
	 */
	@Override
	public boolean remove(DeleteLpnTypeRequest deleteRequest) {
		return lpnTypeDao.delete(deleteRequest);
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<LpnTypeExcelResponse> lpntype = lpnTypeDao.getLpnTypes(params);
		ExcelUtil.export(response, "容器管理", "容器管理数据表", lpntype, LpnTypeExcelResponse.class);
	}
}
