package org.nodes.wms.dao.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收货单明细表 Mapper 接口
 */
@Repository("asnDetailRepository")
public interface AsnDetailMapper extends BaseMapper<AsnDetail> {
	/**
	 * 根据Asn单id查询Asn单明细id
	 *
	 * @param asnBillIdList:
	 * @return java.util.List<java.lang.Long>
	 */
	List<Long> selectAsnDetailId(List<Long> asnBillIdList);
}
