package org.nodes.wms.dao.basics.carriers;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.carriers.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;

/**
 * 承运商 DAO 接口
 */
public interface CarriersDao {
	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param carrierPageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	Page<CarrierResponse> selectPage(IPage<?> page, CarrierPageQuery carrierPageQuery);

	boolean insert(BasicsCarriers basicscarriers);

	boolean isExistCarrierCode(String code);

	boolean delete(DeleteCarriersRequest deleteRequest);

}
