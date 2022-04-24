package org.nodes.wms.dao.basics.suppliers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SuppliersPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Suppliers;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Repository;

/**
 * 供应商表 mapper  接口
 *
 * @author 彭永程
 * @date 2022-04-20 10:49
 **/
@Repository
public interface SuppliersMapper extends BaseMapper<Suppliers> {
	/**
	 * 分页查询供应商
	 *
	 * @param page: 分页对象
	 * @param suppliersPagesQuery: 分页参数
	 * @return Page<SuppliersPageResponse>
	 */
	Page<SuppliersPageResponse> selectPageSuppliers(IPage<?> page, @Param("query") SuppliersPageQuery suppliersPagesQuery);
}
