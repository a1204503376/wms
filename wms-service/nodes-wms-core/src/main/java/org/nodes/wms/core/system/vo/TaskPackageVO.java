
package org.nodes.wms.core.system.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.system.entity.TaskPackage;

/**
 * 工作任务包视图实体类
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TaskPackageVO对象", description = "工作任务包")
public class TaskPackageVO extends TaskPackage {
	private static final long serialVersionUID = 1L;

}
