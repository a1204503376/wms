package org.nodes.wms.dao.count.dto.input;

import lombok.Data;
import org.nodes.wms.dao.count.enums.CountByEnum;
import org.nodes.wms.dao.count.enums.StockCountStateEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 盘点单编辑请求对象
 */
@Data
public class StockCountRequest implements Serializable {

	private static final long serialVersionUID = -7847971547429117878L;
	/**
	 * 盘点单头表主键ID
	 */
	private Long countBillId;
	/**
	 * 盘点单编码
	 */
	private String countBillNo;
	/**
	 * 库房ID
	 */
	@NotNull
	private Long whId;
	/**
	 * 盘点单状态
	 *
	 */
	private StockCountStateEnum countBillState;
	/**
	 * 盘点方式
	 */
	private CountByEnum countBy;
	/**
	 * 备注
	 */
	private String countRemark;
	/**
	 * 单据创建人
	 */
	private String creator;
	/**
	 * 单据创建时间
	 */
	private LocalDateTime procTime;

	private List<StockCountDetailRequest> stockCountDetailRequestList;

}
