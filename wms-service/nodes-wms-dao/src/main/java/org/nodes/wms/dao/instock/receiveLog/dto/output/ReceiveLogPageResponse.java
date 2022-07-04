package org.nodes.wms.dao.instock.receiveLog.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;
import org.nodes.wms.dao.instock.receive.enums.ReceiveHeaderStateEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收货单清点记录分页返回dto类
 **/
@Data
public class ReceiveLogPageResponse extends BaseSkuLotEntity implements Serializable {

	private static final long serialVersionUID = -4270898178645936584L;

	/**
	 * 收货清点记录id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 收货单id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveId;

	/**
	 * 收货单编码
	 */
	private String receiveNo;

	/**
	 * 收货单状态
	 */
	private ReceiveHeaderStateEnum billState;

	/**
	 * 行号
	 */
	private String lineNo;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 收货数量
	 */
	private BigDecimal qty;

	/**
	 * 包装单位名称
	 */
	private String wsuName;

	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * LPN
	 */
	private String lpnCode;

	/**
	 * 序列号
	 */
	private String snCode;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 货主
	 */
	private String ownerName;

	/**
	 * 收货人
	 */
	private String createUserName;

	/**
	 * 收货时间
	 */
	private Date createTime;
}
