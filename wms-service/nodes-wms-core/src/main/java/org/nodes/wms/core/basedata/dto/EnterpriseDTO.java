
package org.nodes.wms.core.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * 来往企业数据传输对象实体类
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnterpriseDTO extends Enterprise {
	private static final long serialVersionUID = 1L;

	/**
	 * 地址信息集合
	 */
	@ApiModelProperty("地址信息集合")
	private List<Address> addressList = new ArrayList<>();

	/**
	 * 删除的地址集合
	 */
	@ApiModelProperty("地址信息集合")
	private List<Address> addressDeletedList = new ArrayList<>();

	/**
	 * 联系人集合
	 */
	@ApiModelProperty("联系人集合")
	private List<Contacts> contactsList = new ArrayList<>();

	/**
	 * 删除的联系人集合
	 */
	@ApiModelProperty("删除的联系人集合")
	private List<Contacts> contactsDeletedList = new ArrayList<>();

	/**
	 * 货主编码
	 */
	@ApiModelProperty("货主编码")
	@NotBlank(message = "货主编码不能为空！",groups = {Excel.class})
	private String ownerCode;
	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;
	/**
	 * 文件名
	 */
	@ApiModelProperty("文件名")
	private String fileName;
}
