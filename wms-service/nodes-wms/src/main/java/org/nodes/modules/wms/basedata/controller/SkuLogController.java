package org.nodes.modules.wms.basedata.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.basedata.entity.SkuLog;
import org.nodes.wms.core.basedata.service.ISkuLogService;
import org.nodes.wms.core.basedata.vo.SkuLogVO;
import org.nodes.wms.core.basedata.wrapper.SkuLogWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;

import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物品操作记录表 控制器
 *
 * @author pengwei
 * @since 2020-06-29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/skulog")
@Api(value = "物品操作记录表", tags = "物品操作记录表接口")

public class SkuLogController extends BladeController {

	private ISkuLogService skuLogService;

	/**
	 * 物品操作记录表列表
	 */
	@ApiLog("物品操作记录表接口-物品操作记录表列表")
	@GetMapping("/list")
	@ApiOperationSupport(order = 1)
	public R<List<SkuLogVO>> list() {
		final Date date = Date.valueOf(LocalDate.now());
		List<SkuLog> list = skuLogService.list().stream().filter(skuLog->{
			Long diffTime = skuLog.getUpdateTime().getTime() - skuLog.getCreateTime().getTime() / 1000 * 2;
			Long diffLast = date.getTime() - skuLog.getUpdateTime().getTime() / 1000;

			if (skuLog.getOutCount().equals(0)) {
				skuLog.setUpdateTime(null);
			}
			return diffTime < diffLast;
		}).collect(Collectors.toList());

		return R.data(SkuLogWrapper.build().listVO(list));
	}
}
