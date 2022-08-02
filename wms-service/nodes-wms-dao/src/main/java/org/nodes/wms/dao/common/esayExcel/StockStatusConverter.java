package org.nodes.wms.dao.common.esayExcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

public class StockStatusConverter implements Converter<StockStatusEnum> {
	@Override
	public Class supportJavaTypeKey() {
		return null;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return null;
	}

	@Override
	public StockStatusEnum convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		if (cellData.toString().equals("0")) {
			return StockStatusEnum.NORMAL;
		} else if (cellData.toString().equals("1")) {
			return StockStatusEnum.FREEZE;
		} else if (cellData.toString().equals("-1")) {
			return StockStatusEnum.SYSTEM_FREEZE;
		}
		return null;
	}

	@Override
	public CellData convertToExcelData(StockStatusEnum stockStatusEnum, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		return new CellData(stockStatusEnum.getDesc());
	}
}
