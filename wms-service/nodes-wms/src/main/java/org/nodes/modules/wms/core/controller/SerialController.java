package org.nodes.modules.wms.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.core.stock.core.service.ISerialService;
import org.nodes.wms.core.stock.core.vo.SerialVO;
import org.nodes.wms.core.stock.core.wrapper.SerialWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 序列号 控制器
 *
 * @author zx
 * @since 2020-02-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/core/serial")
@Api(value = "序列号", tags = "序列号接口")
public class SerialController extends BladeController {

	private ISerialService serialService;

	/**
	 * 自定义分页 序列号
	 */
	@GetMapping("/page")
	@ApiOperation(value = "序列号分页", notes = "传入serial")
	@ApiIgnore
	public R<IPage<SerialVO>> page(Serial serial, Query query) {
		IPage<Serial> pages = serialService.page(Condition.getPage(query), Condition.getQueryWrapper(serial));
		return R.data(SerialWrapper.build().pageVO(pages));
	}
}
