
package org.nodes.wms.core.warehouse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.warehouse.dto.LpnDTO;
import org.nodes.wms.core.warehouse.entity.Lpn;
import org.nodes.wms.core.warehouse.excel.LpnExcel;
import org.nodes.wms.core.warehouse.vo.LpnVO;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 *  容器服务类
 *
 * @author liangmei
 * @since 2019-12-10
 */
public interface ILpnService extends BaseService<Lpn> {

	/**
	 * 模糊列表查询
	 *
	 * @param lpn 查询条件
	 * @return 容器列表信息
	 */
	List<LpnVO> selectList(Lpn lpn);

	/**
	 * 模糊分页查询
	 *
	 * @param page  分页信息
	 * @param lpn 查询条件
	 * @return 容器分页信息
	 */
	IPage<LpnVO> selectPage(IPage<Lpn> page, Lpn lpn);


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
	List<DataVerify> validExcel(List<LpnExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);

	/**
	 * 创建lpnCode
	 * @return 经过判断正常生成的LpnCode;
	 */
	String create();
}
