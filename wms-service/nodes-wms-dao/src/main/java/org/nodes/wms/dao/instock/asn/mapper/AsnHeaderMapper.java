package org.nodes.wms.dao.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.asn.dto.input.AsnBillPageQuery;
import org.nodes.wms.dao.instock.asn.dto.output.*;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收货单头表 Mapper 接口
 */
@Repository("asnHeaderRepository")
public interface AsnHeaderMapper extends BaseMapper<AsnHeader> {

	/**
	 * 获取ASN单的分页结果，支持明细相关字段查询
	 *
	 * @param page    分页参数
	 * @param asnBillPageQuery 条件参数
	 * @return Page<PageResponse>
	 */
	Page<PageResponse> selectPageAsnBill(IPage<?> page, @Param("params") AsnBillPageQuery asnBillPageQuery);

	/**
	 * 查询ASN单信息
	 *
	 * @param asnBillPageQuery: 条件参数
	 * @return List<AsnExportResponse>
	 */
	List<AsnBillExportResponse> selectAsnBillList(@Param("params") AsnBillPageQuery asnBillPageQuery);

	/**
	 * 根据ASN单id查询ASN单头表信息
	 *
	 * @param id: ASN单id
	 * @return AsnHeader ASN单头表实体
	 */
	AsnHeader getById(@Param("id") Long id);

	/**
	 * 查看明细-根据ASN单id查询ASN单头表信息
	 *
	 * @param id:Asn单id
	 * @return AsnHeaderViewResponse ASN单头表信息
	 */
	AsnHeaderForDetailResponse selectAsnHeaderForDetailById(Long id);
}
