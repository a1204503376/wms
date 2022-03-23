package org.nodes.core.tool.entity;

import java.math.BigDecimal;

/**
 * @program: WmsCore
 * @description: 批属性验证接口
 * @author: pengwei
 * @create: 2020-12-18 14:45
 **/
public interface ISkuLotValBase {

	Integer skuLotMustGet(int index);

	Integer skuLotDispGet(int index);

	Integer skuLotMixGet(int index);

	String skuLotMixMaskGet(int index);

	Integer skuLotMaskGet(int index);

	Integer skuLotEditTypeGet(int index);

	Integer skuLenGet(int index);
}
