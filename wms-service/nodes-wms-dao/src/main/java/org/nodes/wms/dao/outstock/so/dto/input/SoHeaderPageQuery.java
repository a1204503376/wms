package org.nodes.wms.dao.outstock.so.dto.input;

import lombok.Data;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 发货单分页请求类
 **/
@Data
public class SoHeaderPageQuery implements Serializable {

	private static final long serialVersionUID = -7934210653032102376L;

	/**
	 * 发货单编码
	 */
	private String soBillNo;

	/**
	 * 上游编码
	 */
	private String orderNo;

	/**
	 * 单据类型
	 */
	private List<String> billTypeCdList;

	/**
	 * 单据状态
	 */
	private List<Integer> soBillStateList;

	/**
	 * 客户
	 */
	private List<Long> customerIdList;

	/**
	 * 库房名称
	 */
	private List<Long> whIdList;

	/**
	 * 创建时间-开始
	 */
	private Date createTimeBegin;

	/**
	 * 创建时间-结束
	 */
	private Date createTimeEnd;

	/**
	 * 创建人
	 */
	private String createUser;
}
