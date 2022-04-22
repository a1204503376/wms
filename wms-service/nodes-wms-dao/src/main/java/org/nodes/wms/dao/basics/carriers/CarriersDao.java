package org.nodes.wms.dao.basics.carriers;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersDeleteRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;

/**
 * 承运商 DAO 接口
 */
public interface CarriersDao {
	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param carriersPageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	Page<CarriersResponse> getcarriersPage(IPage<?> page, CarriersPageQuery carriersPageQuery);


	int save(BasicsCarriers basicscarriers);

	int update(BasicsCarriers basicscarriers);


	String findByCode(String code);

	int delete(CarriersDeleteRequest deleteRequest);
}
