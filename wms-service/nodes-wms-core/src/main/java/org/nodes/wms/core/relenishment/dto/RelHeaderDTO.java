
package org.nodes.wms.core.relenishment.dto;

import lombok.Data;
import org.nodes.wms.core.relenishment.entity.RelHeader;

/**
 * 补货表头数据传输对象实体类
 *
 * @author whj
 * @since 2019-12-24
 */
@Data
public class RelHeaderDTO extends RelHeader {
	String ids;
	Long whId;
	Long woId;
	String woCode;
}
