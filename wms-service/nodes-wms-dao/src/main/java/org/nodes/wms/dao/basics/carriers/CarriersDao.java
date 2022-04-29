package org.nodes.wms.dao.basics.carriers;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.carriers.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierExcelResponse;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;

import java.util.HashMap;
import java.util.List;

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

	/***
	 * 导出Excel
	 * @param params 查询条件
	 **/
	List<CarrierExcelResponse> exportExcel(HashMap<String, Object> params);

	/**
	 * 根据ID修改状态
	 * @param basicsCarriers 内含id与状态
	 * @return 是否成功
	 */
	Boolean updateStatus(BasicsCarriers basicsCarriers);
}
