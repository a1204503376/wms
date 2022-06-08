package org.nodes.wms.dao.basics.suppliers.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierExportResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 供应商表 mapper  接口
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

	/**
	 * 导出-查询供应商信息
	 *
	 * @param wrapper: 条件构造器
	 * @return SupplierExportResponse
	 */
    List<SupplierExportResponse> selectListByWrapper(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

	/**
	 * 根据供应商编码或者供应商名称查询前10个供应商信息
	 *
	 * @param code: 供应商编码
	 * @param name: 供应商名称
	 * @return List<SupplierSelectResponse>
	 */
	List<SupplierSelectResponse> listTop10ByCodeName(@Param("code") String code, @Param("name") String name);
}
