package org.nodes.wms.dao.basics.zone.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 库区下拉选择 请求对象
 */
@Data
public class ZoneSelectQuery implements Serializable {

	private static final long serialVersionUID = -2365923419859920723L;

	/**
	 * 库房idList，idList为空:查询所有库区 否则查询idList集合中的id对应的库区
	 */
	private List<Long> whIdList;
}
