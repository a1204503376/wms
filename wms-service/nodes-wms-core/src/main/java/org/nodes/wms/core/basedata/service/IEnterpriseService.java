
package org.nodes.wms.core.basedata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.basedata.dto.EnterpriseDTO;
import org.nodes.wms.core.basedata.excel.EnterpriseExcel;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 来往企业 服务类
 *
 * @author pengwei
 * @since 2019-12-06
 */
public interface IEnterpriseService extends BaseService<Enterprise> {

	/**
	 * 条件查询列表
	 *
	 * @param enterpriseDTO 查询条件
	 * @return 企业集合
	 */
	List<Enterprise> selectList(EnterpriseDTO enterpriseDTO);

	/**
	 * 条件查询分页
	 *
	 * @param page          分页信息
	 * @param enterpriseDTO 查询条件
	 * @return 企业分页集合
	 */
	IPage<Enterprise> selectPage(IPage<Enterprise> page, EnterpriseDTO enterpriseDTO);

	/**
	 * 新增来往企业
	 *
	 * @param enterpriseDTO EnterpriseDTO 对象
	 * @return 是否成功
	 */
	boolean save(EnterpriseDTO enterpriseDTO);

	/**
	 * 修改来往企业
	 *
	 * @param enterpriseDTO EnterpriseDTO 对象
	 * @return 是否成功
	 */
	boolean updateById(EnterpriseDTO enterpriseDTO);

	/**
	 * 新增或修改来往企业
	 *
	 * @param enterpriseDTO EnterpriseDTO 对象
	 * @return 是否成功
	 */
	boolean saveOrUpdate(EnterpriseDTO enterpriseDTO);

	/**
	 * 修改删除标记为删除
	 *
	 * @param Ids 多个来往企业主键ID字符串
	 * @return 是否成功
	 */
	boolean removeByIds(String Ids);
	/**
	 * Excel 导出(导出当前查询条件)
	 *
	 * @param params 查询条件
	 * @param response
	 */
	void exportExcel(HashMap<String, Object> params, HttpServletResponse response);

	/**
	 * 验证Excel数据
	 *
	 * @param dataList 待验证的数据
	 */
	List<DataVerify> validExcel(List<EnterpriseExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);
}
