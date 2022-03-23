import { Utils } from './../services/Utils';
import { AppConstant } from '../utils/appConstant';
export class SkuLotValResultModel {
	constructor() {
		this.getLotEditVal();
	}
    /**
	 * 批属性名
	 */
	skuLotIndex: string;
    /**
	 * 批属性值
	 */
	LotValue: string;
    /**
	 * PDA批属性标签
	 */
	skuLotLabel: string;
    /**
	 * PC批属性标签
	 */
	skuLot: string;
    /**
	 * 是否可见
	 */
	skuLotDisp: string;
    /**
	 * 是否必须
	 */
	skuLotMust: string;
	/**
   * 混合存放
   */
	skuLotMix: string;
    /**
	 * 格式化规则
	 */
	skuLotMixMask: string;
	/**
	* 长度上限
	*/
	skuLen: number;
    /**
	 * 批掩码生成规则
	 */
	skuLotEditType: string;
    /**
	 * 批属性类型数据
	 */
	CtlLotEditVal: string;
    /**
	 * 批属性类型
	 */
	CtlLotEditBoole: Boolean;

	
	getLotEditVal(): string {
		if (Utils.isNotEmpty(this.skuLotMixMask)) {
			if (this.skuLotMixMask.indexOf('YYYY') != -1 || this.skuLotMixMask.indexOf('yyyy') != -1) {
				return this.CtlLotEditVal = AppConstant.Date;
			} else {
				return this.CtlLotEditVal = AppConstant.Text;
			}
		}
	}
}
export class SkuLot {
	skuLot1: string;
	skuLot2: string;
	skuLot3: string;
	skuLot4: string;
	skuLot5: string;
	skuLot6: string;
	skuLot7: string;
	skuLot8: string;
	skuLot9: string;
	skuLot10: string;
	skuLot11: string;
	skuLot12: string;
	skuLot13: string;
	skuLot14: string;
	skuLot15: string;
	skuLot16: string;
	skuLot17: string;
	skuLot18: string;
	skuLot19: string;
	skuLot20: string;
	skuLot21: string;
	skuLo22: string;
	skuLo23: string;
	skuLo24: string;
	skuLo25: string;
	skuLo26: string;
	skuLo27: string;
	skuLo28: string;
	skuLo29: string;
	skuLo30: string;
}