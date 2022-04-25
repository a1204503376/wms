package org.nodes.wms.biz.basics.carriers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.springblade.core.mp.support.Query;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;


/**
 * 承运商业务层接口
 */
public interface CarriersBiz {
	/**
	 * 分页查询
	 **/
	Page<CarriersResponse> getPage(Query query, CarriersPageQuery carriersPageQuery);
	/**
	 *  保存
	 **/
	boolean saveCarriers(CarriersRequest carriersRequest);
	/**
	 *  逻辑删除
	 **/
	boolean remove(DeleteCarriersRequest deleteRequest);

}
