package org.nodes.wms.dao.basics.skulot.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * PDA批属性组件请求对象
 **/
@Data
public class FindAllSkuLotRequest implements Serializable {
	private static final long serialVersionUID = 8533350909557269080L;
	private Long woId;
}
