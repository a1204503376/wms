package org.nodes.wms.biz.basics.carriers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.carrier.dto.input.*;
import org.nodes.wms.dao.basics.carrier.dto.output.CarrierDropDownResponse;
import org.nodes.wms.dao.basics.carrier.dto.output.CarrierResponse;
import org.nodes.wms.dao.basics.carrier.entites.BasicsCarrier;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


/**
 * 承运商业务层接口
 */
public interface CarrierBiz {
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

	/**
	 * 获取承运商下拉框
	 * @return 承运商集合
	 */
	List<CarrierDropDownResponse> getDropDown(CarrierDropDownRequest carrierDropDownRequest);

	/**
	 * 导入
	 *
	 * @param importDataList: excel中的数据集合
	 * @return true: 导入成功，false: 导入失败
	 */
    boolean importExcel(List<CarrierExcelRequest> importDataList);

	/**
	 * 根据承运商id获取承运商实体
	 * @param id 承运商id
	 * @return BasicsCarrier 承运商实体
	 */
	BasicsCarrier findCarrierById(Long id);

	/**
	 * 根据承运商编码获取承运商实体
	 * @param code 承运商编码
	 * @return BasicsCarrier 承运商实体
	 */
	BasicsCarrier findCarrierByCode(String code);
}
