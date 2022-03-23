
package org.nodes.wms.core.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.common.entity.Contacts;

/**
 * 数据传输对象实体类
 *
 * @author NodeX
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContactsDTO extends Contacts {
	private static final long serialVersionUID = 1L;

}
