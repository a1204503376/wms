package org.nodes.wms.dao.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillExportResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillViewResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
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
	 * 获取ASN单信息和对应的收货单信息
	 *
	 * @param asnBillId:Asn单id
	 * @return AsnBillViewResponse
	 */
	AsnBillViewResponse selectAsnBillViewDetailById(Long asnBillId);

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
}
