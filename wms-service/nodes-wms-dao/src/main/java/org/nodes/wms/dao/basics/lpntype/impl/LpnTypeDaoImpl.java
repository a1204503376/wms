package org.nodes.wms.dao.basics.lpntype.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.lpntype.LpnTypeDao;
import org.nodes.wms.dao.basics.lpntype.dto.input.DeleteLpnTypeRequest;
import org.nodes.wms.dao.basics.lpntype.dto.input.LpnTypePageQuery;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeExcelResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypePageResponse;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.lpntype.mapper.LpnTypeMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 容器Dao实现
 */
@Repository
@RequiredArgsConstructor
public class LpnTypeDaoImpl  extends BaseServiceImpl<LpnTypeMapper,LpnType> implements LpnTypeDao {
  private final LpnTypeMapper lpnTypeMapper;

	/**
	 * 容器分页查询
	 *
	 * @param page            分页对象
	 * @param lpnTypePageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	@Override
	public IPage<LpnTypePageResponse> selectPage(IPage<?> page, LpnTypePageQuery lpnTypePageQuery) {
           return lpnTypeMapper.getPage(page,lpnTypePageQuery);
	}

	/**
	 * 新增容器
	 * @param lpnType 容器实体
	 * @return 是否成功
	 */
	@Override
	public boolean insert(LpnType lpnType) {
		return super.save(lpnType);
	}

	/**
	 * 判断当前容器对象是否存在
	 * @param code 编码
	 * @return 是否成功
	 */
	@Override
	public boolean isExistCarrierCode(String code) {
		LambdaQueryWrapper<LpnType> lambdaQueryWrapper=new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(LpnType::getCode,code);
		return super.count(lambdaQueryWrapper)>0;
	}

	/**
	 * 批量或者单个容器对象
	 * @param deleteRequest 请求对象
	 * @return 是否成功
	 */
	@Override
	public boolean delete(DeleteLpnTypeRequest deleteRequest) {
		return super.deleteLogic(deleteRequest.getList());
	}

	/**
	 * 获取容器 对象
	 * @param params 导出条件
	 * @return 容器集合
	 */
	@Override
	public List<LpnTypeExcelResponse> getLpnTypes(HashMap<String, Object> params) {
		return lpnTypeMapper.getLpnTypes(params);
	}
}
