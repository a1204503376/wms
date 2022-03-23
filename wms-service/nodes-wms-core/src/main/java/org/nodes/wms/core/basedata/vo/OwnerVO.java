package org.nodes.wms.core.basedata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.core.basedata.entity.Owner;
import org.nodes.wms.core.common.vo.AddressVO;
import org.nodes.wms.core.common.vo.ContactsVO;

import java.util.List;

/**
 * 货主管理 视图实体类
 *
 * @author zhongls
 * @since 2019-12-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OwnerVO对象", description = "OwnerVO对象")
public class OwnerVO extends Owner {
	private static final long serialVersionUID = 1L;

	/**
	 * 货主地址详情
	 */
	@ApiModelProperty(value = "货主地址详情")
	private List<AddressVO> addressList;

	/**
	 * 货主联系人详情
	 */
	@ApiModelProperty(value = "货主联系人详情")
	private List<ContactsVO> contactsList;
}
