
package org.nodes.wms.core.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.nodes.core.tool.validation.Add;
import org.nodes.core.tool.validation.Excel;
import org.nodes.core.tool.validation.Update;
import org.springblade.core.tenant.mp.TenantEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 联系人实体类
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Data
@TableName("pub_contacts")
@ApiModel(value = "Contacts对象", description = "Contacts对象")
public class Contacts extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 联系人ID
	 */
	@ApiModelProperty(value = "联系人ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "pc_id", type = IdType.ASSIGN_ID)
	private Long pcId;
	/**
	 * 数据ID
	 */
	@ApiModelProperty(value = "数据ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long dataId;
	/**
	 * 联系人数据类型
	 */
	@NotNull(message = "联系人数据类型不能为空")
	@ApiModelProperty(value = "联系人数据类型", required = true)
	private Integer contactsDataType;
	/**
	 * 联系人姓名
	 */
	@NotBlank(message = "联系人姓名不能为空!",groups = {Excel.class, Add.class, Update.class})
	@Length(min = 1, max = 200)
	@ApiModelProperty(value = "联系人姓名", required = true)
	private String contactsName;
	/**
	 * 联系人Email
	 */
	@ApiModelProperty(value = "联系人Email")
	@Length(max = 200, message = "字符长度不能超过200")
	private String contactsEmail;
	/**
	 * 联系人电话
	 */
	@ApiModelProperty(value = "联系人电话")
	@NotBlank(message = "联系人电话不能为空!",groups = {Excel.class})
	@Length(max = 200, message = "字符长度不能超过200")
	private String contactsNumber;
	/**
	 * 联系人传真
	 */
	@ApiModelProperty(value = "联系人传真")
	@Length(max = 200, message = "字符长度不能超过200")
	private String contactsFax;
	/**
	 * 默认试用标记
	 */
	@ApiModelProperty(value = "默认试用标记")
	private Integer defaultFlag;
}
