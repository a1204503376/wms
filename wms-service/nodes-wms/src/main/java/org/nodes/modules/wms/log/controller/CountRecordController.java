
package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.count.entity.CountRecord;
import org.nodes.wms.core.count.service.ICountRecordService;
import org.nodes.wms.core.count.vo.CountRecordVO;
import org.nodes.wms.core.count.wrapper.CountRecordWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * 盘点单记录表 控制器
 *
 * @author chz
 * @since 2020-02-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/countRecord")
@Api(value = "盘点单记录表", tags = "盘点单记录表接口")
public class CountRecordController extends BladeController {

	private ICountRecordService countRecordService;

	/**
	 * 自定义分页 盘点单记录表
	 */
	@ApiLog("盘点单记录表接口-自定义分页")
	@GetMapping("/page")
	@ApiOperation(value = "自定义分页 盘点单记录表", notes = "传入countRecord")
	public R<IPage<CountRecordVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {

		IPage<CountRecord> pages = countRecordService.page(Condition.getPage(query), Condition.getQueryWrapper(params, CountRecord.class));
		return R.data(CountRecordWrapper.build().pageVO(pages));
	}
	/**
	 * 获取随机盘点单记录
	 */
	@ApiLog("盘点单记录表接口-获取随机盘点单记录")
	@PostMapping("/randomList")
	@ApiOperation(value = "获取随机盘点单记录", notes = "CountRecordVO")
	public R<List<CountRecordVO>> randomList(@RequestBody CountRecordVO countRecordVO) {
		return R.data(CountRecordWrapper.build().listVO(countRecordService.listAll(countRecordVO, 2)));
	}
}
