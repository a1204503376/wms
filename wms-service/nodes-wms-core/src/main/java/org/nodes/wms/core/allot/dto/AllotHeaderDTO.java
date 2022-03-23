
package org.nodes.wms.core.allot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.allot.entity.AllotDetail;
import org.nodes.wms.core.allot.entity.AllotHeader;

import java.util.List;

/**
 * 调拨单头表数据传输对象实体类
 *
 * @author Wangjw
 * @since 2020-01-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AllotHeaderDTO extends AllotHeader {
	private static final long serialVersionUID = 1L;

	/**
	 * 调拨单明细
	 */
	@ApiModelProperty(value = "调拨单明细集合")
	private List<AllotDetail> detailList;

}
