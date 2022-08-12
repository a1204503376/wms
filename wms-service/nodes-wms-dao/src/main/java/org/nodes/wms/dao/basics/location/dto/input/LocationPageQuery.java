package org.nodes.wms.dao.basics.location.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 库区查询条件dto
 **/
@Data
public class LocationPageQuery implements Serializable {

	private static final long serialVersionUID = -6819803594398735910L;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库房id
	 */
	private List<Long> whIdList;

	/**
	 * 库区id
	 */
	private List<Long> zoneIdList;

	/**
	 * 库位类型
	 */
	private List<Integer> locTypeList;

	/**
	 * 库位种类
	 */
	private List<Integer> locCategoryList;

	/**
	 * 库位处理
	 */
	private List<Integer> locHandlingList;

	/**
	 * 容器类别
	 */
	private List<Long> lpnTypeIdList;

	/**
	 * 使用类型
	 */
	private List<Integer> locFlagList;
}
