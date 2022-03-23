package org.nodes.wms.core.strategy.instock;

import org.nodes.wms.core.strategy.instock.impl.DefaultFunctionCode;
import org.nodes.wms.core.strategy.instock.impl.LocationFunctionCode;
import org.nodes.wms.core.strategy.instock.impl.MergeFunctionCode;
import org.nodes.wms.core.strategy.instock.impl.RandomFunctionCode;
import org.nodes.wms.core.warehouse.entity.Location;

/**
 * author: pengwei
 * date: 2021/5/26 10:33
 * description: InstockFactory
 */
public class InstockFactory {

	public static InstockFactory instance;

	public static InstockFactory getInstance() {
		if (instance == null) {
			instance = new InstockFactory();
		}
		return instance;
	}

	/**
	 * 根据策略代码创建策略代码执行对象
	 *
	 * @param functionCode 策略代码[参见 InstockFunctionCodeEnum]
	 * @return IFunctionCode
	 */
	public IFunctionCode create(Integer functionCode) {
		switch (functionCode) {
			case 1:
				return new RandomFunctionCode();
			case 2:
				return new LocationFunctionCode();
			case 3:
				return new MergeFunctionCode();
			default:
				return new DefaultFunctionCode();
		}
	}
}
