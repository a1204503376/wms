/**
 * 扫描解析
 */
import http from '@/http/api.js'
import tool from '@/utils/tool.js'

// 条码类型
const BarcodeType = {
	UnKnow: 0, // 未知类型
	Loc: 10, // 库位
	Lpn: 20, // 容器
	Sku: 30, // 物品
	Serial: 40, // 序列号
	LotNumber: 50, // 批次号
}

// 解析条码, 返回条码对象，属性：type，content
const parseBarcode = (barcode) => {
	if (tool.isEmpty(barcode)) {
		return undefined;
	}

	let barcodeRules = uni.getStorageSync('barcodeRules');
	if (tool.isNotEmpty(barcodeRules)) {
		for (let i = 0; i < barcodeRules.length; i++) {
			let barcodeRuleDesc = new RegExp(barcodeRules[i].barcodeRule);
			if (barcodeRuleDesc.test(barcode)) {
				return {
					type: barcodeRules[i].barcodeType,
					content: parseBarcodeByType(barcodeRules[i].barcodeType, barcode)
				};
			}
		}
	}

	return {
		type: BarcodeType.UnKnow,
		content: barcode
	};
}

const parseLpnBarcode = (barcode) => {
	let scanModel = barcode.split('b:');
	if (scanModel.length > 1) {
		return scanModel[1];
	}

	return barcode;
}

const parseLocBarcode = (barcode) => {
	let scanModel = barcode.split('loc:');
	if (scanModel.length > 1) {
		return scanModel[1];
	}

	return barcode;
}

const parseBarcodeByType = (barcodeType, barcode) => {
	switch (barcodeType) {
		case 10:
			return parseLocBarcode(barcode);
		case 20:
			return parseLpnBarcode(barcode);
		case 30:
			return parseLocBarcode(barcode);
		case 40:
			return parseLpnBarcode(barcode);
		case 50:
			return parseLpnBarcode(barcode);
		case 60:
			return parseLpnBarcode(barcode);
		default:
			return barcode;
	}
}




export default {
	BarcodeType,
	parseBarcode
}
