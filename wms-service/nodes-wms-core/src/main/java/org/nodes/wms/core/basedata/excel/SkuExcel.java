package org.nodes.wms.core.basedata.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author pengwei
 * @Date 2021/4/8 12:14
 * @Description 物品导出模板
 */
@Data
public class SkuExcel{
	/**
	 * 物品编码
	 */
	@NotBlank(message = "物品编码不能为空！")
	@ColumnWidth(15)
	@ExcelProperty("物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@NotBlank(message = "物品名称不能为空！")
	@ColumnWidth(15)
	@ExcelProperty("物品名称")
	private String skuName;
	/**
	 * 物品简称
	 */
	@ColumnWidth(15)
	@ExcelProperty("物品简称")
	private String skuNameS;
	/**
	 * 货主编码
	 */
	@NotBlank(message = "货主编码不能为空！")
	@ColumnWidth(15)
	@ExcelProperty("货主编码")
	private String ownerCode;
	/**
	 * 货主名称
	 */
	@ColumnWidth(15)
	@ExcelProperty("货主名称")
	private String ownerName;
	/**
	 * 保质期天数
	 */
	@ColumnWidth(20)
	@ExcelProperty("保质期天数")
	private String shelfLife;
	/**
	 * 保质期阈值
	 */
	@ColumnWidth(15)
	@ExcelProperty("保质期阈值")
	private String shelfLifeVal;

	/**
	 * 所属分类
	 */
	@ColumnWidth(15)
	@ExcelProperty("所属分类")
	private String skuType;
	/**
	 * 分类编码
	 */
	@ColumnWidth(15)
	@ExcelProperty("分类编码")
	private String typeCode;
	/**
	 * 包装名称
	 */
	@NotBlank(message = "包装名称不能为空！")
	@ColumnWidth(15)
	@ExcelProperty("包装名称")
	private String packageName;
	/**
	 * 批属性
	 */
	@ColumnWidth(15)
	@ExcelProperty("批属性")
	private String skuLot;
	/**
	 * 批属性编码
	 */
	@NotBlank(message = "批属性编码不能为空！")
	@ColumnWidth(15)
	@ExcelProperty("批属性编码")
	private String skuLotCode;
	/**
	 * 批属性验证
	 */
	@NotBlank(message = "批属性验证不能为空！")
	@ColumnWidth(15)
	@ExcelProperty("批属性验证")
	private String skuLotVal;
	/**
	 * ABC分类
	 */
	@ColumnWidth(15)
	@ExcelProperty("ABC分类")
	private String abc;
	/**
	 * 箱号
	 */
	@ColumnWidth(15)
	@ExcelProperty("箱号")
	private String boxNum;
	/**
	 * 体积
	 */
	@ColumnWidth(15)
	@ExcelProperty("体积")
	private String skuVolume;
	/**
	 * 毛重
	 */
	@ColumnWidth(15)
	@ExcelProperty("毛重")
	private String skuGrossWeight;
	/**
	 * 净重
	 */
	@ColumnWidth(15)
	@ExcelProperty("净重")
	private String skuNetWeight;
	/**
	 * 皮重
	 */
	@ColumnWidth(15)
	@ExcelProperty("皮重")
	private String skuTareWeight;
	/**
	 * 序列号
	 */
	@ColumnWidth(15)
	@ExcelProperty("序列号")
	private String isSn;
	/**
	 * 存货类型
	 */
	@ColumnWidth(15)
	@ExcelProperty("存货类型")
	private String storageType;
	/**
	 * 说明
	 */
	@ColumnWidth(15)
	@ExcelProperty("说明")
	private String remarks;
	/**
	 * 物品条码
	 */
	@ColumnWidth(15)
	@ExcelProperty("物品条码")
	private String skuBarcodeList;

	/**
	 * 物品包装
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "物品包装"})
	private String replacePackageName1;
	/**
	 * 计量单位
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "计量单位"})
	private String unitName;
	/**
	 * 计量单位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "计量单位编码"})
	private String unitCode;
	/**
	 * 物品数量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "物品数量"})
	private String skuCount;
	/**
	 * 替代物品
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "替代物品"})
	private String replaceSkuName;
	/**
	 * 替代物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "替代物品编码"})
	private String replaceSkuCode;
	/**
	 * 替代包装
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "替代包装"})
	private String replacePackageName;
	/**
	 * 替代包装
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "替代单位"})
	private String replaceUnitName;
	/**
	 * 替代单位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "替代单位编码"})
	private String replaceUnitCode;
	/**
	 * 替代包装
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "替代数量"})
	private String replaceSkuCount;
	/**
	 * 替代货主编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "替代货主编码"})
	private String replaceOwnerCode;
	/**
	 * 替代货主名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"替代物品", "替代货主名称"})
	private String replaceOwnerName;
	/**
	 * 企业名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "企业名称"})
	private String enterpriseName;
	/**
	 * 企业编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "企业编码"})
	private String enterpriseCode;
	/**
	 * 包装名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "企业包装名称"})
	private String enterprisePackageName;
	/**
	 * 物品名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "企业物品名称"})
	private String enterpriseSkuName;
	/**
	 * 扩展字段1
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段1"})
	private String extendField1;
	/**
	 * 扩展字段2
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段2"})
	private String extendField2;
	/**
	 * 扩展字段3
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段3"})
	private String extendField3;
	/**
	 * 扩展字段4
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段4"})
	private String extendField4;
	/**
	 * 扩展字段5
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段5"})
	private String extendField5;
	/**
	 * 扩展字段6
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段6"})
	private String extendField6;
	/**
	 * 扩展字段7
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段7"})
	private String extendField7;
	/**
	 * 扩展字段8
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段8"})
	private String extendField8;
	/**
	 * 扩展字段9
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段9"})
	private String extendField9;
	/**
	 * 扩展字段10
	 */
	@ColumnWidth(15)
	@ExcelProperty({"企业信息", "扩展字段10"})
	private String extendField10;

}
