package org.nodes.wms.dao.receive.log.dto.output;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 清点记录 返回前端视图类
 **/
@Data
public class LogResponse {
	/**
	 *  清点表 ID
	 */
	private Long id;
	/**
	 * 订单行号
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
	 * 数量
	 */
	private BigDecimal qty;
	/**
	 * 计量单位名称
	 */
	private String um_name;
}
