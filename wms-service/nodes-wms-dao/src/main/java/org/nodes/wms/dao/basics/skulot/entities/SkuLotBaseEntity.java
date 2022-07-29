package org.nodes.wms.dao.basics.skulot.entities;

import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;

/**
 * 批属性基类
 */
@Data
public class SkuLotBaseEntity extends TenantEntity implements ISkuLotBase {

	private static final long serialVersionUID = -6438722673012243114L;
	/**
	 * 批属性1
	 */
	private String skuLot1;
	/**
	 * 批属性2
	 */
	private String skuLot2;
	/**
	 * 批属性3
	 */
	private String skuLot3;
	/**
	 * 批属性4
	 */
	private String skuLot4;
	/**
	 * 批属性5
	 */
	private String skuLot5;
	/**
	 * 批属性6
	 */
	private String skuLot6;
	/**
	 * 批属性7
	 */
	private String skuLot7;
	/**
	 * 批属性8
	 */
	private String skuLot8;
	/**
	 * 批属性9
	 */
	private String skuLot9;
	/**
	 * 批属性10
	 */
	private String skuLot10;
	/**
	 * 批属性11
	 */
	private String skuLot11;
	/**
	 * 批属性12
	 */
	private String skuLot12;
	/**
	 * 批属性13
	 */
	private String skuLot13;
	/**
	 * 批属性14
	 */
	private String skuLot14;
	/**
	 * 批属性15
	 */
	private String skuLot15;
	/**
	 * 批属性16
	 */
	private String skuLot16;
	/**
	 * 批属性17
	 */
	private String skuLot17;
	/**
	 * 批属性18
	 */
	private String skuLot18;
	/**
	 * 批属性19
	 */
	private String skuLot19;
	/**
	 * 批属性20
	 */
	private String skuLot20;
	/**
	 * 批属性21
	 */
	private String skuLot21;
	/**
	 * 批属性22
	 */
	private String skuLot22;
	/**
	 * 批属性23
	 */
	private String skuLot23;
	/**
	 * 批属性24
	 */
	private String skuLot24;
	/**
	 * 批属性25
	 */
	private String skuLot25;
	/**
	 * 批属性26
	 */
	private String skuLot26;
	/**
	 * 批属性27
	 */
	private String skuLot27;
	/**
	 * 批属性28
	 */
	private String skuLot28;
	/**
	 * 批属性29
	 */
	private String skuLot29;
	/**
	 * 批属性30
	 */
	private String skuLot30;

	/**
	 * 根据编号获取对应的批属性值
	 *
	 * @param i 编号
	 * @return 批属性值
	 */
	@Override
	public String skuLotGet(int i) {
		String skuLotRlt = null;
		switch (i) {
			case 1:
				skuLotRlt = getSkuLot1();
				break;
			case 2:
				skuLotRlt = getSkuLot2();
				break;
			case 3:
				skuLotRlt = getSkuLot3();
				break;
			case 4:
				skuLotRlt = getSkuLot4();
				break;
			case 5:
				skuLotRlt = getSkuLot5();
				break;
			case 6:
				skuLotRlt = getSkuLot6();
				break;
			case 7:
				skuLotRlt = getSkuLot7();
				break;
			case 8:
				skuLotRlt = getSkuLot8();
				break;
			case 9:
				skuLotRlt = getSkuLot9();
				break;
			case 10:
				skuLotRlt = getSkuLot10();
				break;
			case 11:
				skuLotRlt = getSkuLot11();
				break;
			case 12:
				skuLotRlt = getSkuLot12();
				break;
			case 13:
				skuLotRlt = getSkuLot13();
				break;
			case 14:
				skuLotRlt = getSkuLot14();
				break;
			case 15:
				skuLotRlt = getSkuLot15();
				break;
			case 16:
				skuLotRlt = getSkuLot16();
				break;
			case 17:
				skuLotRlt = getSkuLot17();
				break;
			case 18:
				skuLotRlt = getSkuLot18();
				break;
			case 19:
				skuLotRlt = getSkuLot19();
				break;
			case 20:
				skuLotRlt = getSkuLot20();
				break;
			case 21:
				skuLotRlt = getSkuLot21();
				break;
			case 22:
				skuLotRlt = getSkuLot22();
				break;
			case 23:
				skuLotRlt = getSkuLot23();
				break;
			case 24:
				skuLotRlt = getSkuLot24();
				break;
			case 25:
				skuLotRlt = getSkuLot25();
				break;
			case 26:
				skuLotRlt = getSkuLot26();
				break;
			case 27:
				skuLotRlt = getSkuLot27();
				break;
			case 28:
				skuLotRlt = getSkuLot28();
				break;
			case 29:
				skuLotRlt = getSkuLot29();
				break;
			case 30:
				skuLotRlt = getSkuLot30();
				break;
			default:
				break;
		}
		if (Func.isEmpty(skuLotRlt)) {
			skuLotRlt = StringPool.EMPTY;
		}

		return skuLotRlt;
	}

	/**
	 * 检查指定编号的批属性值是否为空
	 *
	 * @param i 批属性编号
	 * @return 批属性值是否为空
	 */
	@Override
	public Boolean skuLotChk(int i) {
		switch (i) {
			case 1:
				return !Func.isBlank(getSkuLot1());
			case 2:
				return !Func.isBlank(getSkuLot2());
			case 3:
				return !Func.isBlank(getSkuLot3());
			case 4:
				return !Func.isBlank(getSkuLot4());
			case 5:
				return !Func.isBlank(getSkuLot5());
			case 6:
				return !Func.isBlank(getSkuLot6());
			case 7:
				return !Func.isBlank(getSkuLot7());
			case 8:
				return !Func.isBlank(getSkuLot8());
			case 9:
				return !Func.isBlank(getSkuLot9());
			case 10:
				return !Func.isBlank(getSkuLot10());
			case 11:
				return !Func.isBlank(getSkuLot11());
			case 12:
				return !Func.isBlank(getSkuLot12());
			case 13:
				return !Func.isBlank(getSkuLot13());
			case 14:
				return !Func.isBlank(getSkuLot14());
			case 15:
				return !Func.isBlank(getSkuLot15());
			case 16:
				return !Func.isBlank(getSkuLot16());
			case 17:
				return !Func.isBlank(getSkuLot17());
			case 18:
				return !Func.isBlank(getSkuLot18());
			case 19:
				return !Func.isBlank(getSkuLot19());
			case 20:
				return !Func.isBlank(getSkuLot20());
			case 21:
				return !Func.isBlank(getSkuLot21());
			case 22:
				return !Func.isBlank(getSkuLot22());
			case 23:
				return !Func.isBlank(getSkuLot23());
			case 24:
				return !Func.isBlank(getSkuLot24());
			case 25:
				return !Func.isBlank(getSkuLot25());
			case 26:
				return !Func.isBlank(getSkuLot26());
			case 27:
				return !Func.isBlank(getSkuLot27());
			case 28:
				return !Func.isBlank(getSkuLot28());
			case 29:
				return !Func.isBlank(getSkuLot29());
			case 30:
				return !Func.isBlank(getSkuLot30());
			default:
				return true;
		}
	}

	/**
	 * 通过索引存入批属性
	 *
	 */
	@Override
	public void skuLotSet(int i, String skuLotValue) {
		switch (i) {
			case 1:
				setSkuLot1(skuLotValue);
				break;
			case 2:
				setSkuLot2(skuLotValue);
				break;
			case 3:
				setSkuLot3(skuLotValue);
				break;
			case 4:
				setSkuLot4(skuLotValue);
				break;
			case 5:
				setSkuLot5(skuLotValue);
				break;
			case 6:
				setSkuLot6(skuLotValue);
				break;
			case 7:
				setSkuLot7(skuLotValue);
				break;
			case 8:
				setSkuLot8(skuLotValue);
				break;
			case 9:
				setSkuLot9(skuLotValue);
				break;
			case 10:
				setSkuLot10(skuLotValue);
				break;
			case 11:
				setSkuLot11(skuLotValue);
				break;
			case 12:
				setSkuLot12(skuLotValue);
				break;
			case 13:
				setSkuLot13(skuLotValue);
				break;
			case 14:
				setSkuLot14(skuLotValue);
				break;
			case 15:
				setSkuLot15(skuLotValue);
				break;
			case 16:
				setSkuLot16(skuLotValue);
				break;
			case 17:
				setSkuLot17(skuLotValue);
				break;
			case 18:
				setSkuLot18(skuLotValue);
				break;
			case 19:
				setSkuLot19(skuLotValue);
				break;
			case 20:
				setSkuLot20(skuLotValue);
				break;
			case 21:
				setSkuLot21(skuLotValue);
				break;
			case 22:
				setSkuLot22(skuLotValue);
				break;
			case 23:
				setSkuLot23(skuLotValue);
				break;
			case 24:
				setSkuLot24(skuLotValue);
				break;
			case 25:
				setSkuLot25(skuLotValue);
				break;
			case 26:
				setSkuLot26(skuLotValue);
				break;
			case 27:
				setSkuLot27(skuLotValue);
				break;
			case 28:
				setSkuLot28(skuLotValue);
				break;
			case 29:
				setSkuLot29(skuLotValue);
				break;
			case 30:
				setSkuLot30(skuLotValue);
				break;
			default:
				break;
		}
	}

	/**
	 * 比较批属性是否一致
	 *
	 * @param skuLotBase 批属性基类
	 * @return 批属性是否完全相等
	 */
	@Override
	public Boolean skuLotCompare(ISkuLotBase skuLotBase) {
		for (int i = 1; i <= 30; i++) {
			if (!this.skuLotChk(i)) {
				continue;
			}
			if (!this.skuLotGet(i).equals(skuLotBase.skuLotGet(i))) {
				return false;
			}
		}
		return true;
	}
}
