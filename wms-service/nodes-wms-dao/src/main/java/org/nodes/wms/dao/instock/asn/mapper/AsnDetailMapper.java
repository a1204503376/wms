package org.nodes.wms.dao.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.instock.asn.entitits.AsnDetail;
import org.springframework.stereotype.Repository;

/**
 * 收货单明细表 Mapper 接口
 */
@Repository("asnDetailRepository")
public interface AsnDetailMapper extends BaseMapper<AsnDetail> {

}
