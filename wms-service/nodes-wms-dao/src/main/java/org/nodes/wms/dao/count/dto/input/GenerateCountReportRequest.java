package org.nodes.wms.dao.count.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 生成盘点差异报告请求对象
 *
 * @author nodes
 */
@Data
public class GenerateCountReportRequest implements Serializable {
	private static final long serialVersionUID = 3816726549211435153L;
	/**
	 * 用户手动所提交的数据
	 */
	List<GenerateCountReport> countReportList;
}
