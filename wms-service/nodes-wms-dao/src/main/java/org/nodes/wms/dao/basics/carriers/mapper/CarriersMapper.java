package org.nodes.wms.dao.basics.carriers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;


/**
 * 承运商 Mapper 接口
 */
public interface CarriersMapper  extends BaseMapper<BasicsCarriers> {
	Page<CarriersResponse> getPage(IPage<?> page, @Param("query") CarriersPageQuery carriersPageQuery);

}