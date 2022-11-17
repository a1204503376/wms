
package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.transfer.entity.TransferRecord;
import org.nodes.wms.core.stock.transfer.service.ITransferRecordService;
import org.nodes.wms.core.stock.transfer.vo.TransferRecordVO;
import org.nodes.wms.core.stock.transfer.wrapper.TransferRecordWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * 移动记录表 控制器
 *
 * @author wangjw
 * @since 2020-02-27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/transferRecord")
@Api(value = "移动记录表", tags = "移动记录表接口")
public class TransferRecordController extends BladeController {

	private ITransferRecordService transferRecordService;


	/**
	 * 自定义分页 移动记录表
	 */
	@GetMapping("/page")
	@ApiOperation(value = "移动记录自定义分页", notes = "传入transferRecordVO")
	public R<IPage<TransferRecordVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<TransferRecord> pages = transferRecordService.page(Condition.getPage(query), Condition.getQueryWrapper(params, TransferRecord.class));
		return R.data(TransferRecordWrapper.build().pageVO(pages));
	}
}
