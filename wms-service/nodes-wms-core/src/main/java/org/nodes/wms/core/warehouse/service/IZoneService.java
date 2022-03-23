
package org.nodes.wms.core.warehouse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.warehouse.dto.ZoneDTO;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.excel.ZoneExcel;
import org.nodes.wms.core.warehouse.vo.ZoneVO;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 服务类
 *
 * @author NodeX
 * @since 2019-12-06
 */
public interface IZoneService extends BaseService<Zone> {
	/**
	 * 修改
	 *
	 * @param zoneDTO
	 * @return
	 */
	boolean saveOrUpdate(ZoneDTO zoneDTO);

	/**
	 * 修改
	 *
	 * @param zoneDTO
	 * @return
	 */
	boolean updateById(ZoneDTO zoneDTO);

	/**
	 * @param zone
	 * @return
	 */
	boolean save(ZoneDTO zone);

	List<ZoneVO> selectList(Zone zone);

	/**
	 * 模糊分页查询
	 *
	 * @param page  分页信息
	 * @param zone 查询条件
	 * @return 库区分页信息
	 */
	IPage<ZoneVO> selectPage(IPage<Zone> page, Zone zone);


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
	List<DataVerify> validExcel(List<ZoneExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importData(List<DataVerify> dataVerifyList);


}
