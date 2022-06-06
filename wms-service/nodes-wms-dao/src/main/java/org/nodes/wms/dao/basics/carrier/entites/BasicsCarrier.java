package org.nodes.wms.dao.basics.carrier.entites;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

/**
 * 承运商表 实体类
 **/
@Data
@TableName("basics_carriers")
public class BasicsCarrier extends BaseEntity {
	private static final long serialVersionUID = -1091858350663621722L;
	/**
	 * 承运商表ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 承运商编码
	 */
	private String code;
	/**
	 * 承运商名称
	 */
	private String name;
	/**
	 * 承运商简称
	 */
	private String  simpleName;
	/**
	 * 货主id
	 */
	private Long woId;
	/**
	 * 备注
	 */
	private String remark;
}
