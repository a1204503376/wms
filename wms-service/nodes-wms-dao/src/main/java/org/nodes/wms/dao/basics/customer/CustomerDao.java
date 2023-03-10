package org.nodes.wms.dao.basics.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customer.dto.input.DeleteCustomerRequest;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerResponse;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerSelectResponse;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomer;

import java.util.List;

/**
 * 客户表 DAO 接口
 */
public interface CustomerDao {

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param customerPageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	Page<CustomerResponse> selectPage(IPage<CustomerResponse> page, CustomerPageQuery customerPageQuery);

	boolean  insert(BasicsCustomer basicsCustomer);


	boolean isExistCustomerCode(String code);

	boolean delete(DeleteCustomerRequest deleteRequest);


    List<CustomerResponse> getCustomerResponseByQuery(CustomerPageQuery customerPageQuery);

    List<CustomerSelectResponse> listTop10ByCodeName(String code, String name);

	/**
	 * 导入
	 *
	 * @param customerList: 客户数据集合
	 * @return true: 导入成功，false:导入是啊比
	 */
    boolean importExcel(List<BasicsCustomer> customerList);

	/**
	 * 根据客户id获取客户实体
	 * @param id 客户id
	 * @return  BasicsCustomer 客户实体
	 */
	BasicsCustomer getCustomerById(Long id);

	/**
	 * 根据客户编码获取客户实体
	 * @param code 客户编码
	 * @return  BasicsCustomer 客户实体
	 */
	BasicsCustomer getCustomerByCode(String code);

	/**
	 * 修改客户api
	 * @param basicsCustomer
	 */
	void update(BasicsCustomer basicsCustomer);
}
