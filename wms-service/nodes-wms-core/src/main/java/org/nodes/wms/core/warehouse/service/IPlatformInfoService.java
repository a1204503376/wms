
package org.nodes.wms.core.warehouse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.warehouse.dto.PlatformInfoDTO;
import org.nodes.wms.core.warehouse.entity.PlatformInfo;
import org.nodes.wms.core.warehouse.excel.PlatformInfoExcel;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 *  月台服务类
 *
 * @author liangmei
 * @since 2019-12-06
 */
public interface IPlatformInfoService extends BaseService<PlatformInfo> {

	/**
	 * 条件查询列表
	 *
	 * @param platformInfoDTO 查询条件
	 * @return 月台集合
	 */
	List<PlatformInfo> selectList(PlatformInfoDTO platformInfoDTO);

	/**
	 * 条件查询分页
	 *
	 * @param page          分页信息
	 * @param platformInfoDTO 查询条件
	 * @return 月台分页集合
	 */
	IPage<PlatformInfo> selectPage(IPage<PlatformInfo> page, PlatformInfoDTO platformInfoDTO);
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
	List<DataVerify> validExcel(List<PlatformInfoExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);

}
