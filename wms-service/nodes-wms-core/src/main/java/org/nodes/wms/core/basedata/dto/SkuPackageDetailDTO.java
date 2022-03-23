
package org.nodes.wms.core.basedata.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;

/**
 * 数据传输对象实体类
 *
 * @author NodeX
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuPackageDetailDTO extends SkuPackageDetail {
	private static final long serialVersionUID = 1L;
}
