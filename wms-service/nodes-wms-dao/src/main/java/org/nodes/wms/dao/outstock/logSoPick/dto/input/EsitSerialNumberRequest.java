package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 判断当前物品是否是序列号管理请求对象
 **/
@Data
public class EsitSerialNumberRequest implements Serializable {
	private static final long serialVersionUID = 262818672988593070L;
	private String skuCode;
}
