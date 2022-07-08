package org.nodes.wms.core.instock.asn.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.core.tool.validation.Add;
import org.nodes.core.tool.validation.Update;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 收货明细DTO
 *
 * @Author zx
 * @Date 2020/1/15
 **/
@Data
public class AsnDTO extends SkuLotBaseEntity {

	@NotNull(message = "参数 skuId 不能为空")
	@ApiModelProperty(value = "物品id")
	private Long skuId;

	@ApiModelProperty(value = "订单id")
	private Long asnBillId;

	@ApiModelProperty(value = "订单明细id")
	private Long asnDetailId;

	@ApiModelProperty(value = "物品编码")
	@NotNull(message = "参数 skuCode 物品编码不能为空")
	@Length(max = 30, message = "最大长度为30位", groups = {Add.class, Update.class})
	private String skuCode;

	@NotNull(message = "参数 isSn 是否为序列号管理不能为空")
	@ApiModelProperty(value = "序列号管理（1：序列号管理  0：非序列号管理）")
	private Integer isSn;

	@NotNull(message = "参数 whId 库房ID不能为空")
	private Long whId;

	@NotNull(message = "参数 planCountQty 计划数量总数不能为空")
	@ApiModelProperty(value = "计划总数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal planCountQty;

	@NotNull(message = "参数 scanCountQty 实际数量总数不能为空")
	@ApiModelProperty(value = "实绩总数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal scanCountQty;

	@ApiModelProperty(value = "包装id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;

	@ApiModelProperty(value = "包装明细id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspdId;

	@ApiModelProperty(value = "序列号编码")
	private String snDetailCode;

	@ApiModelProperty(value = "容器明细id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnLpnId;

	@ApiModelProperty(value = "容器编码")
	private String lpnCode = StringPool.EMPTY;

	@ApiModelProperty(value = "托盘集合")
	private List<String> lpnCodes = new ArrayList<>();

	@ApiModelProperty(value = "单据编码")
	@NotNull(message = "参数 asnBillNo 单据编码不能为空")
	@Length(max = 60, message = "单据编码最大长度为60位")
	private String asnBillNo;

	@NotNull(message = "参数 scanQty 收货数量不能为空")
	@Range(min = 1, message = "请输入有效数量")
	@ApiModelProperty(value = "实际数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal scanQty;

	@ApiModelProperty(value = "库位ID")
	private Long locId;

	@NotNull(message = "参数 locCode 不能为空")
	@ApiModelProperty(value = "库位编码")
	private String locCode;

	@ApiModelProperty(value = "箱号")
	private String boxCode = StringPool.EMPTY;

	@ApiModelProperty(value = "供应商名称")
	private String sName;

	@ApiModelProperty(value = "规格")
	private String skuSpec;

	@ApiModelProperty(value = "单个明细计划量")
	private BigDecimal planQty;

	@ApiModelProperty("任务id")
	private Long taskId;
}
