package org.nodes.wms.dao.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收货单明细表 Mapper 接口
 */
@Repository("asnDetailRepository")
public interface AsnDetailMapper extends BaseMapper<AsnDetail> {
	/**
	 * 根据Asn单id查询Asn单明细
	 *
	 * @param asnBillId: ASN单id
	 * @return List<AsnDetail>
	 */
	List<AsnDetail> selectAsnDetailByAsnBillId(@Param("asnBillId") Long asnBillId);
}
