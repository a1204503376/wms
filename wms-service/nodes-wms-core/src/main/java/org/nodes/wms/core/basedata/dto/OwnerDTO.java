package org.nodes.wms.core.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.basedata.entity.Owner;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.dto.AddressDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 货主管理 数据传输对象实体类
 *
 * @author zhongls
 * @since 2019-12-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OwnerDTO extends Owner {
	private static final long serialVersionUID = 1L;

	/**
	 * 货主地址详情
	 */
	@ApiModelProperty(value = "货主地址详情")
	private List<AddressDTO> addressList = new ArrayList<>();

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
	 * 文件名
	 */
	@ApiModelProperty("文件名")
	private String fileName;
}
