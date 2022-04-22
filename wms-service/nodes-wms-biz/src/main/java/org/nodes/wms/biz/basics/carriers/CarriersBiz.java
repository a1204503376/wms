package org.nodes.wms.biz.basics.carriers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersDeleteRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;


/**
 * 承运商业务层接口
 */
public interface CarriersBiz {
	/**
	 * 分页查询
	 **/
	Page<CarriersResponse> page(IPage<CarriersResponse> page, CarriersPageQuery carriersPageQuery);
	/**
	 *  保存
	 **/
	int save(CarriersRequest carriersRequest);
	/**
	 *  修改
	 **/
	int update(CarriersRequest carriersRequest);
	/**
	 *  逻辑删除
	 **/
	int delete(CarriersDeleteRequest deleteRequest);
}
