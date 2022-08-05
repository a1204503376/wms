package org.nodes.wms.dao.common.esayExcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.nodes.core.enums.BaseEnum;

public class EnumConverter implements Converter<BaseEnum> {
	@Override
	public Class supportJavaTypeKey() {
		return null;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return null;
	}

	@Override
	public BaseEnum convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		return null;
	}


	@Override
	public CellData convertToExcelData(BaseEnum baseEnum, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		return new CellData(baseEnum.getDesc());
	}
}
