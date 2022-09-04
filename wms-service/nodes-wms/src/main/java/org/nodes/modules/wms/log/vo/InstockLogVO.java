package org.nodes.modules.wms.log.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.putaway.entities.StInstockLog;

/**
 * 上架记录视图实体类
 *
 * @author NodeX
 * @since 2020-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InstockLogVO对象", description = "上架策略执行记录")
public class InstockLogVO extends StInstockLog {
	private static final long serialVersionUID = 1L;

	/**
	 * 上架策略计算代码名称
	 */
	private String instockFunctionName;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 是否成功
	 */
	private String isSuccessName;

	/**
	 * 执行时间范围
	 */
	private String aplStartAndEndTime;

	private String createUserCode;

	private String createUserName;


}
