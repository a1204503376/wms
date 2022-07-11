package org.nodes.wms.core.instock.asn.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.ISkuLotBase;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AsnHeaderExcel implements ISkuLotBase {

	@ExcelProperty("入库单编码")
	@ColumnWidth(15)
	@NotBlank(message = "入库单编码不能为空！")
	private String asnBillNo;

	@ExcelProperty("单据类型")
	@ColumnWidth(15)
	@NotBlank(message = "单据类型不能为空！")
	private String billTypeCd;

	@ExcelProperty("入库方式")
	@ColumnWidth(15)
	@NotBlank(message = "入库方式不能为空！")
	private String instoreType;

	@ExcelProperty("所属货主编码")
	@NotBlank(message = "所属货主编码不能为空！")
	@ColumnWidth(15)
	private String woCode;

	@ExcelProperty("所属货主名称")
	@ColumnWidth(15)
	private String woName;

	@ExcelProperty("所属仓库编码")
	@NotBlank(message = "所属仓库编码不能为空！")
	@ColumnWidth(15)
	private String whCode;

	@ExcelProperty("所属仓库名称")
	@ColumnWidth(15)
	private String whName;

	@ExcelProperty("所属部门编码")
	@NotBlank(message = "所属部门编码不能为空！")
	@ColumnWidth(15)
	private String deptCode;

	@ExcelProperty("所属部门名称")
	@ColumnWidth(15)
	private String deptName;

	@ExcelProperty("预计到货时间")
	@ColumnWidth(15)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Date scheduledArrivalDate;

	@ExcelProperty("WMS备注")
	@ColumnWidth(15)
	private String asnBillRemark;

	@ExcelProperty("上位系统单编号")
	@ColumnWidth(15)
	private String orderNo;

	@ExcelProperty("上位系统单备注")
	@ColumnWidth(15)
	private String asnRemark;

	@ExcelProperty({"供应商","供应商编码"})
	@NotBlank(message = "供应商编码不能为空！")
	@ColumnWidth(15)
	private String suCode;

	@ExcelProperty({"供应商", "供应商名称"})
	@ColumnWidth(15)
	private String suName;

	@ExcelProperty({"供应商","供应商联系人"})
	@ColumnWidth(15)
	private String contact;

	@ExcelProperty({"供应商", "供应商联系电话"})
	@ColumnWidth(15)
	private String phone;

	@ExcelProperty({"供应商","供应商地址"})
	@ColumnWidth(15)
	private String suAddress;

	@ExcelProperty({"明细", "物品行号"})
	@ColumnWidth(15)
	private String asnLineNo;

	@ExcelProperty({"明细", "物品编码"})
	@NotBlank(message = "物品编码不能为空！")
	@ColumnWidth(15)
	private String skuCode;

	@ExcelProperty({"明细", "物品名称"})
	@ColumnWidth(15)
	private String skuName;

	@ExcelProperty({"明细", "包装名称"})
	@NotBlank(message = "包装名称不能为空！")
	@ColumnWidth(15)
	private String wspName;

	@ExcelProperty({"明细", "计量单位编码"})
	@NotBlank(message = "计量单位编码不能为空！")
	@ColumnWidth(15)
	private String umCode;

	@ExcelProperty({"明细", "计量单位名称"})
	@ColumnWidth(15)
	private String umName;

	@ExcelProperty({"明细", "计划数量"})
	@ColumnWidth(15)
	private BigDecimal planQty;

	@ExcelProperty({"明细", "实际数量"})
	@ColumnWidth(15)
	private BigDecimal scanQty;

	@ExcelProperty({"明细", "剩余数量"})
	@ColumnWidth(15)
	private BigDecimal surplusQty;

//	@ExcelProperty({"明细", "单价"})
//	@ColumnWidth(15)
//	private BigDecimal detailPrice;
//
//	@ExcelProperty({"明细", "总金额"})
//	@ColumnWidth(15)
//	private BigDecimal detailAmount;

	@ExcelProperty({"明细", "上位系统明细标识"})
	@ColumnWidth(15)
	private String asnBillDetailKey;

	@ExcelProperty({"明细", "上位系统明细备注"})
	@ColumnWidth(15)
	private String asnDetailRemark;


	/**
	 * 批属性1
	 */
	@ApiModelProperty("批属性1")
	@ExcelProperty({"明细","批属性1"})
	@ColumnWidth(15)
	private String skuLot1;
	/**
	 * 批属性2
	 */
	@ApiModelProperty("批属性2")
	@ExcelProperty({"明细","批属性2"})
	@ColumnWidth(15)
	private String skuLot2;
	/**
	 * 批属性3
	 */
	@ApiModelProperty("批属性3")
	@ExcelProperty({"明细","批属性3"})
	@ColumnWidth(15)
	private String skuLot3;
	/**
	 * 批属性4
	 */
	@ApiModelProperty("批属性4")
	@ExcelProperty({"明细","批属性4"})
	@ColumnWidth(15)
	private String skuLot4;
	/**
	 * 批属性5
	 */
	@ApiModelProperty("批属性5")
	@ExcelProperty({"明细","批属性5"})
	@ColumnWidth(15)
	private String skuLot5;
	/**
	 * 批属性6
	 */
	@ApiModelProperty("批属性6")
	@ExcelProperty({"明细","批属性6"})
	@ColumnWidth(15)
	private String skuLot6;
	/**
	 * 批属性7
	 */
	@ApiModelProperty("批属性7")
	@ExcelProperty({"明细","批属性7"})
	@ColumnWidth(15)
	private String skuLot7;
	/**
	 * 批属性8
	 */
	@ApiModelProperty("批属性8")
	@ExcelProperty({"明细","批属性8"})
	@ColumnWidth(15)
	private String skuLot8;
	/**
	 * 批属性9
	 */
	@ApiModelProperty("批属性9")
	@ExcelProperty({"明细","批属性9"})
	@ColumnWidth(15)
	private String skuLot9;
	/**
	 * 批属性10
	 */
	@ApiModelProperty("批属性10")
	@ExcelProperty({"明细","批属性10"})
	@ColumnWidth(15)
	private String skuLot10;
	/**
	 * 批属性11
	 */
	@ApiModelProperty("批属性11")
	@ExcelProperty({"明细","批属性11"})
	@ColumnWidth(15)
	private String skuLot11;
	/**
	 * 批属性12
	 */
	@ApiModelProperty("批属性12")
	@ExcelProperty({"明细","批属性12"})
	@ColumnWidth(15)
	private String skuLot12;
	/**
	 * 批属性13
	 */
	@ApiModelProperty("批属性13")
	@ExcelProperty({"明细","批属性13"})
	@ColumnWidth(15)
	private String skuLot13;
	/**
	 * 批属性14
	 */
	@ApiModelProperty("批属性14")
	@ExcelProperty({"明细","批属性14"})
	@ColumnWidth(15)
	private String skuLot14;
	/**
	 * 批属性15
	 */
	@ApiModelProperty("批属性15")
	@ExcelProperty({"明细","批属性15"})
	@ColumnWidth(15)
	private String skuLot15;
	/**
	 * 批属性16
	 */
	@ApiModelProperty("批属性16")
	@ExcelProperty({"明细","批属性16"})
	@ColumnWidth(15)
	private String skuLot16;
	/**
	 * 批属性17
	 */
	@ApiModelProperty("批属性17")
	@ExcelProperty({"明细","批属性17"})
	@ColumnWidth(15)
	private String skuLot17;
	/**
	 * 批属性18
	 */
	@ApiModelProperty("批属性18")
	@ExcelProperty({"明细","批属性18"})
	@ColumnWidth(15)
	private String skuLot18;
	/**
	 * 批属性19
	 */
	@ApiModelProperty("批属性19")
	@ExcelProperty({"明细","批属性19"})
	@ColumnWidth(15)
	private String skuLot19;
	/**
	 * 批属性20
	 */
	@ApiModelProperty("批属性20")
	@ExcelProperty({"明细","批属性20"})
	@ColumnWidth(15)
	private String skuLot20;
	/**
	 * 批属性21
	 */
	@ApiModelProperty("批属性21")
	@ExcelProperty({"明细","批属性21"})
	@ColumnWidth(15)
	private String skuLot21;
	/**
	 * 批属性22
	 */
	@ApiModelProperty("批属性22")
	@ExcelProperty({"明细","批属性22"})
	@ColumnWidth(15)
	private String skuLot22;
	/**
	 * 批属性23
	 */
	@ApiModelProperty("批属性23")
	@ExcelProperty({"明细","批属性23"})
	@ColumnWidth(15)
	private String skuLot23;
	/**
	 * 批属性24
	 */
	@ApiModelProperty("批属性24")
	@ExcelProperty({"明细","批属性24"})
	@ColumnWidth(15)
	private String skuLot24;
	/**
	 * 批属性25
	 */
	@ApiModelProperty("批属性25")
	@ExcelProperty({"明细","批属性25"})
	@ColumnWidth(15)
	private String skuLot25;
	/**
	 * 批属性26
	 */
	@ApiModelProperty("批属性26")
	@ExcelProperty({"明细","批属性26"})
	@ColumnWidth(15)
	private String skuLot26;
	/**
	 * 批属性27
	 */
	@ApiModelProperty("批属性27")
	@ExcelProperty({"明细","批属性27"})
	@ColumnWidth(15)
	private String skuLot27;
	/**
	 * 批属性28
	 */
	@ApiModelProperty("批属性28")
	@ExcelProperty({"明细","批属性28"})
	@ColumnWidth(15)
	private String skuLot28;
	/**
	 * 批属性29
	 */
	@ApiModelProperty("批属性29")
	@ExcelProperty({"明细","批属性29"})
	@ColumnWidth(15)
	private String skuLot29;
	/**
	 * 批属性30
	 */
	@ApiModelProperty("批属性30")
	@ExcelProperty({"明细","批属性30"})
	@ColumnWidth(15)
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
	 * @param i
	 * @param skuLotValue
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
