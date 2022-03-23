package org.nodes.wms.core.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;

/**
 * 打印模板实体类
 *
 * @author NodeX
 * @since 2020-04-21
 */
@Data
@TableName("sys_print_template")
@ApiModel(value = "PrintTemplate对象", description = "打印模板")
public class PrintTemplate extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 打印模板ID
	 */
	@ApiModelProperty(value = "打印模板ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "spt_id", type = IdType.ASSIGN_ID)
	private Long sptId;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID")
	private Long woId;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	private Long whId;
	/**
	 * 打印模板类别
	 */
	@ApiModelProperty(value = "打印模板类别")
	private Integer sptType;
	/**
	 * 打印模板编码
	 */
	@ApiModelProperty(value = "打印模板编码")
	private String sptCode;
	/**
	 * 打印模板描述
	 */
	@ApiModelProperty(value = "打印模板描述")
	private String sptDesc;
	/**
	 * 打印模板版本
	 */
	@ApiModelProperty(value = "打印模板版本")
	private String sptVer;
	/**
	 * 模板数据
	 */
	@ApiModelProperty(value = "模板数据")
	private String sptData;
	/**
	 * 打印模板备注
	 */
	@ApiModelProperty(value = "打印模板备注")
	private String sptRemark;
	/**
	 * 文件名称
	 */
	@ApiModelProperty(value = "文件名称")
	private String sptName;

}
