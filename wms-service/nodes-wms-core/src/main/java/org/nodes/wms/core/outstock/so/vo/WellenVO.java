
package org.nodes.wms.core.outstock.so.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.so.entity.Wellen;

import java.util.ArrayList;
import java.util.List;

/**
 * 波次划分表视图实体类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WellenVO对象", description = "波次划分表")
public class WellenVO extends Wellen {
	private static final long serialVersionUID = 1L;

	private List<WellenDetailVO> detailList = new ArrayList<>();
}
