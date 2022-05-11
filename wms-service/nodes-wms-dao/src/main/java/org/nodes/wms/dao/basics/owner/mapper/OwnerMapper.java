package org.nodes.wms.dao.basics.owner.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.springframework.stereotype.Repository;

/**
 * 货主 Mapper 接口
 */
@Repository("ownerRepository")
public interface OwnerMapper extends BaseMapper<Owner> {

}
