
package org.nodes.wms.core.count.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.count.entity.CountReport;

/**
 * 盘点差异报告表数据传输对象实体类
 *
 * @author chz
 * @since 2020-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CountReportDTO extends CountReport {
	private static final long serialVersionUID = 1L;

}
