package org.nodes.wms.dao.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
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
	 * @param pageParamsQuery 条件参数
	 * @return Page<PageResponse>
	 */
	Page<PageResponse> selectPageAsnBill(IPage<?> page, @Param("params") PageParamsQuery pageParamsQuery);

	/**
	 * 查询ASN单信息
	 *
	 * @param pageParamsQuery: 条件参数
	 * @return List<AsnExportResponse>
	 */
	List<AsnBillExportResponse> selectAsnBillList(@Param("params") PageParamsQuery pageParamsQuery);

	/**
	 * 根据ASN单id查询ASN单头表信息
	 *
	 * @param asnBillId:
	 * @return AsnHeader
	 */
	AsnHeader selectAsnHeaderByAsnBillId(@Param("asnBillId") Long asnBillId);

	/**
	 * 查看明细-根据ASN单id查询ASN单头表信息
	 *
	 * @param id:Asn单id
	 * @return AsnHeaderViewResponse
	 */
	AsnHeaderViewResponse selectAsnHeaderViewById(Long id);

	/**
	 * 查看明细-根据ASN单id查询ASN单明细表信息
	 *
	 * @param asnBillId:Asn单id
	 * @return AsnDetailViewResponse
	 */
	List<AsnDetailViewResponse> selectAsnDetailViewByAsnBillId(Long asnBillId);

	/**
	 * 根据ASN单id查询审计日志
	 *
	 * @param id: ASD单id
	 * @return ListAsnLogActionViewResponse>
	 */
	List<AsnLogActionViewResponse> selectLogActionById(Long id);
}
