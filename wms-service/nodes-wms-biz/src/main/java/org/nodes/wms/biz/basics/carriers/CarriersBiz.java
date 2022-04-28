package org.nodes.wms.biz.basics.carriers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarrierPageQuery;
import org.springblade.core.mp.support.Query;
import org.nodes.wms.dao.basics.carriers.dto.input.NewCarrierRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierResponse;


/**
 * 承运商业务层接口
 */
public interface CarriersBiz {
	/**
	 * 分页查询
	 **/
	Page<CarrierResponse> getPage(Query query, CarrierPageQuery carrierPageQuery);
	/**
	 *  保存
	 **/
	boolean newCarrier(NewCarrierRequest newCarrierRequest);
	/**
	 *  逻辑删除
	 **/
	boolean remove(DeleteCarriersRequest deleteRequest);

}
