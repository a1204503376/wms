package org.nodes.wms.dao.instock.asn.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
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
	 * @param wrapper mybatis-plus 内置参数
	 * @return IPage<PageResponse>
	 */
	Page<PageResponse> selectPageAsnBill(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

	/**
	 * 获取ASN单和对应的收货单信息
	 *
	 * @param id: ASN单id
	 * @return DetailResponse
	 */
	AsnDetailResponse selectAsnDetailById(List<Long> id);

	/**
	 * 查询收货单中是否存在含有Asn单明细id的记录
	 *
	 * @param asnDetailId: ASN单明细id
	 * @return java.lang.Integer
	 */
	Integer selectCountForReceiveDetailByAsnDetaillId(List<Long> asnDetailId);
}
