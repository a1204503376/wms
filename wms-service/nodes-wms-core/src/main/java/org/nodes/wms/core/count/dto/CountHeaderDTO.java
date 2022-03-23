
package org.nodes.wms.core.count.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.entity.CountHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * 盘点单头表数据传输对象实体类
 *
 * @author NodeX
 * @since 2020-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CountHeaderDTO extends CountHeader {
	private static final long serialVersionUID = 1L;

	private List<CountDetail> detailList = new ArrayList<>();
}
