package org.nodes.wms.dao.basics.zone.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 库区下拉选择 请求对象
 */
@Data
public class ZoneSelectQuery implements Serializable {

	private static final long serialVersionUID = -2365923419859920723L;

	/**
	 * 关键词
	 * 一般为：zoneCode ,zoneName
	 */
	private String key;

	/**
	 * 库房id，id为空:查询所有库区 否则查询id指定库房下的库区
	 */
	private String whId;
}
