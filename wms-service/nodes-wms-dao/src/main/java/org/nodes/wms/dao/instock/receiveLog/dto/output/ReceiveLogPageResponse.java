package org.nodes.wms.dao.instock.receiveLog.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.nodes.wms.dao.common.entitits.BaseSkuLotEntity;

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
	 * 收货单编码
	 */
	@ExcelProperty("收货单编码")
	private String receiveNo;

	/**
	 * 行号
	 */
	@ExcelProperty("行号")
	private String lineNo;

	/**
	 * 物品编码
	 */
	@ExcelProperty("物品编码")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ExcelProperty("物品名称")
	private String skuName;

	/**
	 * 收货数量
	 */
	@ExcelProperty("收货数量")
	private BigDecimal qty;

	/**
	 * 包装单位名称
	 */
	@ExcelProperty("包装单位")
	private String wsuName;

	/**
	 * 箱号
	 */
	@ExcelProperty("箱号")
	private String boxCode;

	/**
	 * LPN
	 */
	@ExcelProperty("LPN")
	private String lpnCode;

	/**
	 * 序列号
	 */
	@ExcelProperty("序列号")
	private String snCode;

	/**
	 * 库位编码
	 */
	@ExcelProperty("库位编码")
	private String locCode;

	/**
	 * 库房名称
	 */
	@ExcelProperty("库房")
	private String whName;

	/**
	 * 货主
	 */
	@ExcelProperty("货主")
	private String ownerName;

	/**
	 * 收货人
	 */
	@ExcelProperty("收货人")
	private String createUserName;

	/**
	 * 收货时间
	 */
	@ExcelProperty("收货时间")
	private Date createTime;
}
