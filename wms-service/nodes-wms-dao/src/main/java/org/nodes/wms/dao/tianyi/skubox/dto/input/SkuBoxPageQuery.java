package org.nodes.wms.dao.tianyi.skubox.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 天宜物品和型号对应箱型标识 分页查询请求类
 **/
@Data
public class SkuBoxPageQuery implements Serializable {

	private static final long serialVersionUID = -611301630152563682L;

	/**
	 * 识别码
	 */
	private Integer id;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 物品规格
	 */
	private String skuSpec;
}
