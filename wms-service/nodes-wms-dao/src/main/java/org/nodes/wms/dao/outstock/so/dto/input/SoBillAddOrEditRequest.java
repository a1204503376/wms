package org.nodes.wms.dao.outstock.so.dto.input;

import lombok.Data;
import org.nodes.core.tool.validation.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 发货单新增请求类
 **/
@Data
public class SoBillAddOrEditRequest implements Serializable {

	private static final long serialVersionUID = 6156256757211977804L;

	/**
	 * 发货单头表id
	 */
	@NotNull(message = "出库单id不能为空", groups = Update.class)
	private Long soBillId;

	/**
	 * 出库单编码
	 */
	@NotNull(message = "出库单编码不能为空", groups = Update.class)
	private String soBillNo;

	/**
	 * 单据类型
	 */
	@NotNull(message = "单据类型不能为空")
	private String billTypeCd;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 客户id
	 */
	private Long customerId;

	/**
	 * 发货方式
	 */
	private Integer transportCode;

	/**
	 * 出库方式
	 */
	private Integer outstockType;

	/**
	 * 发货单备注
	 */
	private String soBillRemark;

	/**
	 * 发货明细
	 */
	private List<SoDetailAddOrEditRequest> soDetailList;

	/**
	 * 被删除的发货明细id
	 */
	private List<Long> removeIdList;
}
