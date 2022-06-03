package org.nodes.wms.core.basedata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.dto.OwnerDTO;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.excel.OwnerExcel;
import org.nodes.wms.core.basedata.vo.OwnerVO;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhugx
 * @program 货主管理服务接口
 * @description 货主管理服务接口
 * @create 20191128
 */
public interface IOwnerService extends BaseService<Owner> {

	/**
	 * 根据主键ID更新货主信息
	 *
	 * @param ownerDTO 货主信息
	 * @return 是否成功
	 */
	boolean updateById(OwnerDTO ownerDTO);

	/**
	 * 保存货主信息
	 *
	 * @param ownerDTO 货主信息
	 * @return 是否成功
	 */
	boolean save(OwnerDTO ownerDTO);

	/**
	 * 保存或者更新货主信息
	 *
	 * @param entity 货主信息
	 * @return 是否成功
	 */
	boolean saveOrUpdate(OwnerDTO entity);

	/**
	 * 删除货主信息
	 *
	 * @param ids 货主id
	 * @return 是否成功
	 */
	//boolean removeByIds(List<Long> ids);

	/**
	 * 模糊分页查询
	 *
	 * @param page  分页信息
	 * @param owner 查询条件
	 * @return 货主分页信息
	 */
	IPage<OwnerVO> selectPage(IPage<Owner> page, Owner owner);

	/**
	 * 模糊列表查询
	 *
	 * @param owner 查询条件
	 * @return 货主列表信息
	 */
	List<OwnerVO> selectList(Owner owner);

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
	List<DataVerify> validExcel(List<OwnerExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);
}
