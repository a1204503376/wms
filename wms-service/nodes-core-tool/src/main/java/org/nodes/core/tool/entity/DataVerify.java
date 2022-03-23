package org.nodes.core.tool.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengwei
 * @program WmsCore
 * @description 数据验证DTO
 * @create 20200304
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "数据验证对象", description = "数据验证对象（包括：数据行，异常描述）")
public class DataVerify {

	/**
	 * 索引行
	 */
	@ApiModelProperty("索引行")
	private Integer index;

	/**
	 * 缓存键
	 */
	@ApiModelProperty("缓存键")
	private String cacheKey;

	/**
	 * 对象
	 */
	@ApiModelProperty("对象")
	private Object obj;

	/**
	 * 异常消息
	 */
	@ApiModelProperty("异常消息")
	private String message;
}
