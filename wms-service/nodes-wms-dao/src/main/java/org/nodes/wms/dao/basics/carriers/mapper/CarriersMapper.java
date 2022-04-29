package org.nodes.wms.dao.basics.carriers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.carriers.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierExcelResponse;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;

import java.util.HashMap;
import java.util.List;


/**
 * 承运商 Mapper 接口
 */
public interface CarriersMapper  extends BaseMapper<BasicsCarriers> {
	Page<CarrierResponse> getPage(IPage<?> page, @Param("query") CarrierPageQuery carrierPageQuery);
	List<CarrierExcelResponse> getCarriers(@Param("query") HashMap<String, Object> params);
}
