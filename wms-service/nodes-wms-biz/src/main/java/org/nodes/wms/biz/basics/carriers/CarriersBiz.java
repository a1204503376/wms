package org.nodes.wms.biz.basics.carriers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.UpdateStatusRequest;
import org.springblade.core.mp.support.Query;
import org.nodes.wms.dao.basics.carriers.dto.input.NewCarrierRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


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
	/**
	 * 导出Excel
	 * @param params 查询条件
	 * @param response 响应对象
	 */
	void exportExcel(HashMap<String, Object> params, HttpServletResponse response);
	/**
	 * 根据ID修改状态
	 * @param updateStatusRequest 内含id与状态
	 * @return 是否成功
	 */
	Boolean updateStatusById(UpdateStatusRequest updateStatusRequest);
}
