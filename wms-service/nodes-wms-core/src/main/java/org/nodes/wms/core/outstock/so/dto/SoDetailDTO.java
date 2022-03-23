
package org.nodes.wms.core.outstock.so.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.so.entity.SoDetail;

/**
 * 数据传输对象实体类
 *
 * @author NodeX
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SoDetailDTO extends SoDetail {
	private static final long serialVersionUID = 1L;
	private SoHeaderDTO soHeader;
	/**
	 * 包装名称
	 */
	private String wspName;
}
