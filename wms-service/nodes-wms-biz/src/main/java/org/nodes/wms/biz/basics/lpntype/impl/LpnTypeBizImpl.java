package org.nodes.wms.biz.basics.lpntype.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.dao.putway.dto.modular.LpnTypeFactory;
import org.nodes.wms.dao.basics.lpntype.LpnTypeDao;
import org.nodes.wms.dao.basics.lpntype.dto.input.*;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeByIdResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeExcelResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypePageResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeSelectResponse;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
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
			throw new ServiceException("新增容器失败，容器类型编码重复");
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

	/**
	 * @param params   查询条件
	 * @param response 响应对象
	 */
	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<LpnTypeExcelResponse> lpntype = lpnTypeDao.getLpnTypes(params);
		ExcelUtil.export(response, "容器管理", "容器管理数据表", lpntype, LpnTypeExcelResponse.class);
	}

	/**
	 * 容器ById查询
	 * @param lpnTypeByIdRequest 容器ById请求对象
	 * @return 容器ById查询返回对象
	 */
	@Override
	public LpnTypeByIdResponse getLpnTypeById(LpnTypeByIdRequest lpnTypeByIdRequest) {
		return lpnTypeFactory.createLpnTypeByIdResponse(lpnTypeDao.getById(lpnTypeByIdRequest.getId()));
	}

	/**
	 * 根据id修改容器
	 * @param lpnTypeRequest 修改请求对象
	 * @return 是否成功
	 */
	@Override
	public boolean updateLpnTypeById(UpdateLpnTypeRequest lpnTypeRequest) {
		return lpnTypeDao.updateById(lpnTypeFactory.createLpnType(lpnTypeRequest));
	}

    @Override
    public LpnType findLpnTypeById(Long id) {
		return lpnTypeDao.getLpnTypeById(id);
    }

	@Override
	public LpnType findLpnTypeByCode(String code) {
		return lpnTypeDao.getLpnTypeByCode(code);
	}

    @Override
    public List<LpnTypeSelectResponse> findLpnTypeSelectList() {
        return lpnTypeDao.getLpnTypeSelectList();
    }

    @Override
    public LpnTypeCodeEnum parseBoxCode(String boxCode) {
        LpnTypeCodeEnum[] lpnTypeCodeEnum = LpnTypeCodeEnum.values();
		for (LpnTypeCodeEnum item:lpnTypeCodeEnum){
			if(item.getCode().equals(boxCode.substring(0,1))){
				return item;
			}
		}
	   throw new ServiceException("查询失败,箱码输入错误");
    }

    @Override
    public LpnType findLpnTypeByBoxCode(String boxCode) {
        return lpnTypeDao.getLpnTypeByCode(boxCode);
    }
}
