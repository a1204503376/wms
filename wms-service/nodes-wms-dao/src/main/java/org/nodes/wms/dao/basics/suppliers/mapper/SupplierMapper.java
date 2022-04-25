package org.nodes.wms.dao.basics.suppliers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.springframework.stereotype.Repository;

/**
 * 供应商表 mapper  接口
 *
 * @author 彭永程
 * @date 2022-04-20 10:49
 **/
@Repository
public interface SupplierMapper extends BaseMapper<Supplier> {
	/**
	 * 分页查询供应商
	 *
	 * @param page: 分页对象
	 * @param supplierPagesQuery: 分页参数
	 * @return Page<SupplierPageResponse>
	 */
	Page<SupplierPageResponse> selectPageSupplier(IPage<?> page, @Param("query") SupplierPageQuery supplierPagesQuery);
}
