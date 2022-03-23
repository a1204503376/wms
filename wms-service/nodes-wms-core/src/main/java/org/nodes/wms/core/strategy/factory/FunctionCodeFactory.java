package org.nodes.wms.core.strategy.factory;

import org.springblade.core.log.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nodes.core.tool.utils.PackageUtil;
import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.core.strategy.entity.OutstockDetail;
import org.nodes.wms.core.strategy.enums.OutstockFuncEnum;
import org.nodes.wms.core.strategy.factory.annotation.Code;
import org.springblade.core.tool.utils.Func;

import java.util.List;

/**
 * @program: WmsCore
 * @description: 分配策略执行代码工厂类
 * @author: pengwei
 * @create: 2020-12-16 13:38
 **/
@Getter
@AllArgsConstructor
public class FunctionCodeFactory {

	static final String PACKAGE_NAME = "org.nodes.wms.core.strategy.factory.outstock";

	public static IFunctionCode create(SkuOutstock skuOutstock, OutstockDetail detail) {
		OutstockFuncEnum outstockFuncEnum = null;//OutstockFuncEnum.parse(detail.getOutstockFunction());
		IFunctionCode functionCode = null;
		try {
			// 找到所有实现类的完整包名
			List<String> packageList = PackageUtil.getClasses(PACKAGE_NAME);
			// 创建实现类的，反射找到系统配置所设置的类
			for (String packageName : packageList) {
				Class clazz = Class.forName(packageName);
				if (Func.isEmpty(clazz)) {
					continue;
				}
				if (!IFunctionCode.class.isAssignableFrom(clazz)) {
					continue;
				}
				Code code = (Code) clazz.getAnnotation(Code.class);
				if (Func.isNotEmpty(code) && outstockFuncEnum.equals(code.value())) {
					functionCode = (IFunctionCode) clazz.newInstance();
					functionCode.setOutstockDetail(detail);
					functionCode.setSkuOutstock(skuOutstock);
					break;
				}
			}
			if (Func.isEmpty(functionCode)) {
				throw new ServiceException("未知策略");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return functionCode;
	}
}
