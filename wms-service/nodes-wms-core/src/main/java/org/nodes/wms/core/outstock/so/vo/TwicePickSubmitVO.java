package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 二次分拣提交VO
 * @Author zx
 * @Date 2020/5/8
 **/
@Data
public class TwicePickSubmitVO {
	/**
	 * 容器编码
	 */
	@ApiModelProperty("容器编码")
	private String sourceLpnCode;

	/**
	 * 单据ID
	 */
	@ApiModelProperty("单据ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 单据明细ID
	 */
//	@ApiModelProperty("单据明细ID")
//	private Long billDetailId;

	/**
	 * 目标容器编码
	 */
	@ApiModelProperty("目标容器编码")
	private String targetLpnCode;

	/**
	 * 目标库位编码
	 */
	@ApiModelProperty("目标库位编码")
	private String targetLocCode;



	/**
	 * 明细列表
	 */
	@ApiModelProperty("明细列表")
	private List<TwicePickSoDetailVO> twicePickSoDetailVOList;

	/**
	 * 目标库位id
	 */
	private Long targetLocId;
}
