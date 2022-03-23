
package org.nodes.wms.core.count.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.count.entity.CountRecord;

/**
 * 盘点单记录表数据传输对象实体类
 *
 * @author chz
 * @since 2020-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CountRecordDTO extends CountRecord {
	private static final long serialVersionUID = 1L;

	/**
	 * 设置批属性的值
	 * @param index 批属性索引
	 * @param value 值
	 */
	public void skuLotSet(int index, String value) {
		switch (index) {
			case 1:super.setSkuLot1(value); break;
			case 2:super.setSkuLot2(value); break;
			case 3:super.setSkuLot3(value); break;
			case 4:super.setSkuLot4(value); break;
			case 5:super.setSkuLot5(value); break;
			case 6:super.setSkuLot6(value); break;
			case 7:super.setSkuLot7(value); break;
			case 8:super.setSkuLot8(value); break;
			case 9:super.setSkuLot9(value); break;
			case 10:super.setSkuLot10(value); break;
			case 11:super.setSkuLot11(value); break;
			case 12:super.setSkuLot12(value); break;
			case 13:super.setSkuLot13(value); break;
			case 14:super.setSkuLot14(value); break;
			case 15:super.setSkuLot15(value); break;
			case 16:super.setSkuLot16(value); break;
			case 17:super.setSkuLot17(value); break;
			case 18:super.setSkuLot18(value); break;
			case 19:super.setSkuLot19(value); break;
			case 20:super.setSkuLot20(value); break;
			case 21:super.setSkuLot21(value); break;
			case 22:super.setSkuLot22(value); break;
			case 23:super.setSkuLot23(value); break;
			case 24:super.setSkuLot24(value); break;
			case 25:super.setSkuLot25(value); break;
			case 26:super.setSkuLot26(value); break;
			case 27:super.setSkuLot27(value); break;
			case 28:super.setSkuLot28(value); break;
			case 29:super.setSkuLot29(value); break;
			case 30:super.setSkuLot30(value); break;
		}
	}
}
