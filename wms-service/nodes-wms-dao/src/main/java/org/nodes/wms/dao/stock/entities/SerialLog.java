package org.nodes.wms.dao.stock.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 序列号日志
 */
@Data
@TableName("wms_serial_log")
public class SerialLog extends TenantEntity {

	private static final long serialVersionUID = 6423261395564626517L;

	@TableId(type = IdType.ASSIGN_ID)
	private Long wlsnlId;

	private Long stockId;

	private Long billId;

	private String billNo;

	private String lineNo;

	private Long whId;

	private String serialNumber;

	private SerialStateEnum serialState;

	private Long skuId;

	private String skuCode;

	private String skuName;

	private String lotNumber;

	private Integer proType;

	private Long systemProcId;
}
