
package org.nodes.wms.core.basedata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.common.vo.AddressVO;
import org.nodes.wms.core.common.vo.ContactsVO;

import java.util.List;

/**
 * 来往企业视图实体类
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "EnterpriseVO对象", description = "来往企业")
public class EnterpriseVO extends Enterprise {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业类型描述
	 */
	@ApiModelProperty("企业类型描述")
	private String enterpriseTypeDesc;

	/**
	 * 地址信息集合
	 */
	@ApiModelProperty("地址信息集合")
	private List<AddressVO> addressList;

	/**
	 * 联系人集合
	 */
	@ApiModelProperty("联系人集合")
	private List<ContactsVO> contactsList;

	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;
}
