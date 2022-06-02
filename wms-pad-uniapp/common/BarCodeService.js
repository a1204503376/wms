import http from '@/http/api.js'
import tool from '@/utils/tool.js'

// 返回条码类型
const ToBarcodeType = (text) => {
	switch (text) {
		case 10: //"货位条码":
			return BarcodeType.HuoWei;
		case 20: //"容器编码":
			return BarcodeType.TuoPan;
		case 30: //"物品编码":
			return BarcodeType.SKU;
		case 40: //"序列号":
			return BarcodeType.SerialNumber;
		case 50: //"批次号":
			return BarcodeType.BatchNo;
		case 60: //"二维码标签":
			return BarcodeType.QRCode;
		case 70: //"成品打包标签":
			return BarcodeType.ProductPackageCode;
		case 80: // 成品专用二维码标签
			return BarcodeType.CPCode;
		default:
			return BarcodeType.BoxCode;
	}
}
// 托盘，货位，SKU，序列号, 员工账号，错误的条码
const BarcodeType = {
	TuoPan: 0,
	HuoWei: 1,
	SKU: 2,
	SerialNumber: 3,
	YuanGongZhangHao: 4,
	BatchNo: 6,
	Error: 5,
	BoxCode: 7,
	QRCode: 8,
	CPCode: 9,
	ProductPackageCode: 10,
	OKSubmit: 11,
}

// 解析条码
const GetBarcodeType = (text, that) => {
	if (tool.isEmpty(text)) {
		return BarcodeType.Error;
	}
	if (text == '$end$') {
		return BarcodeType.OKSubmit;
	}
	let barcodeRules = that.$store.state.barcodeRules;
	if (barcodeRules) {
		for (let i = barcodeRules.length - 1; i >= 0; i--) {
			let barcodeDesc = barcodeRules[i].barcodeType;
			let rule = barcodeRules[i].barcodeRule;
			let regexp = tool.clearSpace(rule) || "";
			if (tool.isNotEmpty(rule)) {
				if (text == barcodeDesc.toString()) {
					return ToBarcodeType(barcodeDesc);
				}
			}
		}
	}
	// 无法解析到条码时,都属于物品
	return BarcodeType.BoxCode;
}


/*
 * 解析库位 
 * 把要解析的库位条码放入到这个方法中，将会返回库位,
 * 如果不是loc:.*格式的那将返回空
 */
const ScanLocCode = (barcode) => {
	if (tool.isEmpty(barcode)) {
		return '';
	}
	//二维码绑定规则
	let scanModel = [];
	scanModel = barcode.split('loc:');
	if (scanModel.length > 1) {
		return scanModel[1];
	}
	return '';
}


export default {
	GetBarcodeType,
	ScanLocCode
}
