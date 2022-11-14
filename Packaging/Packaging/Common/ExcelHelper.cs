using NPOI.HSSF.UserModel;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System;
using System.ComponentModel;
using System.Linq;
using DataAccess.Enitiies;
using NPOI.SS.UserModel;
using NPOI.XSSF.UserModel;
using System.Text.RegularExpressions;
using DataAccess.Dto;
using System.Reflection;
using System.Data;

namespace Packaging.Common
{
    public class ExcelHelper
    {
        #region Excel导入

        /// <summary>
        /// 从Excel取数据并记录到List集合里
        /// </summary>
        /// <param name="cellHeader">单元头的值和名称：{ { "UserName", "姓名" }, { "Age", "年龄" } };</param>
        /// <param name="filePath">保存文件绝对路径</param>
        /// <param name="errorMsg">错误信息</param>
        /// <param name="startIndex">数据行开始序列，默认为1（即第二列，从0开始）</param>
        /// <returns>转换后的List对象集合</returns>
        private static List<T> ExcelToEntityList<T>(Dictionary<string, string> cellHeader, string filePath,
            out StringBuilder errorMsg, int startIndex = 1) where T : new()
        {
            var enlist = new List<T>();
            errorMsg = new StringBuilder();
            try
            {
                if (Regex.IsMatch(filePath, ".xls$")) // 2003
                {
                    enlist = Excel2003ToEntityList<T>(cellHeader, filePath, out errorMsg, startIndex);
                }
                else if (Regex.IsMatch(filePath, ".xlsx$")) // 2007
                {
                    enlist = Excel2007ToEntityList<T>(cellHeader, filePath, out errorMsg, startIndex);
                }

                return enlist;
            }
            catch (Exception)
            {
                throw;
            }
        }

        /// <summary>
        /// 从Excel2003取数据并记录到List集合里
        /// </summary>
        /// <param name="cellHeader">单元头的Key和Value：{ { "UserName", "姓名" }, { "Age", "年龄" } };</param>
        /// <param name="filePath">保存文件绝对路径</param>
        /// <param name="errorMsg">错误信息</param>
        /// <param name="startIndex"></param>
        /// <returns>转换好的List对象集合</returns>
        private static List<T> Excel2003ToEntityList<T>(Dictionary<string, string> cellHeader, string filePath,
            out StringBuilder errorMsg, int startIndex = 1) where T : new()
        {
            errorMsg = new StringBuilder(); // 错误信息,Excel转换到实体对象时，会有格式的错误信息
            var enlist = new List<T>(); // 转换后的集合
            try
            {
                using (var fs = File.OpenRead(filePath))
                {
                    var workbook = new HSSFWorkbook(fs);
                    var sheet = (HSSFSheet)workbook.GetSheetAt(0); // 获取此文件第一个Sheet页
                    for (var rowIndex = startIndex; rowIndex <= sheet.LastRowNum; rowIndex++)
                    {
                        // 1.判断当前行是否空行，若空行就不在进行读取下一行操作，结束Excel读取操作
                        var row = sheet.GetRow(rowIndex);
                        if (row == null)
                        {
                            break;
                        }

                        // 2.每一个Excel row转换为一个实体对象
                        var en = new T();
                        ExcelRowToEntity<T>(cellHeader, row, rowIndex, en, ref errorMsg);

                        enlist.Add(en);
                    }
                }

                return enlist;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        /// <summary>
        /// 从Excel2007取数据并记录到List集合里
        /// </summary>
        /// <param name="cellHeader">单元头的Key和Value：{ { "UserName", "姓名" }, { "Age", "年龄" } };</param>
        /// <param name="filePath">保存文件绝对路径</param>
        /// <param name="errorMsg">错误信息</param>
        /// <param name="startIndex">数据行开始序列，默认为1（即第二列，从0开始）</param>
        /// <returns>转换好的List对象集合</returns>
        private static List<T> Excel2007ToEntityList<T>(Dictionary<string, string> cellHeader, string filePath,
            out StringBuilder errorMsg, int startIndex = 1) where T : new()
        {
            errorMsg = new StringBuilder(); // 错误信息,Excel转换到实体对象时，会有格式的错误信息
            var enlist = new List<T>(); // 转换后的集合
            try
            {
                using (var fs = File.OpenRead(filePath))
                {
                    var workbook = new XSSFWorkbook(fs);
                    var sheet = (XSSFSheet)workbook.GetSheetAt(0); // 获取此文件第一个Sheet页
                    for (var rowIndex = startIndex; rowIndex <= sheet.LastRowNum; rowIndex++)
                    {
                        // 1.判断当前行是否空行，若空行就不在进行读取下一行操作，结束Excel读取操作
                        var row = sheet.GetRow(rowIndex);
                        if (row == null)
                        {
                            break;
                        }

                        // 2.每一个Excel row转换为一个实体对象
                        var en = new T();
                        ExcelRowToEntity<T>(cellHeader, row, rowIndex, en, ref errorMsg);
                        enlist.Add(en);
                    }
                }

                return enlist;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        #endregion Excel导入

        #region Common

        /// <summary>
        /// Excel row转换为实体对象
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="cellHeader">单元头的Key和Value：{ { "UserName", "姓名" }, { "Age", "年龄" } };</param>
        /// <param name="row">Excel row</param>
        /// <param name="rowIndex">row index</param>
        /// <param name="en">实体</param>
        /// <param name="errorMsg">错误信息</param>
        private static void ExcelRowToEntity<T>(Dictionary<string, string> cellHeader, IRow row, int rowIndex, T en,
            ref StringBuilder errorMsg)
        {
            var keys = cellHeader.Keys.ToList(); // 要赋值的实体对象属性名称
            var errStr = ""; // 当前行转换时，是否有错误信息，格式为：第1行数据转换异常：XXX列；
            for (var i = 0; i < keys.Count; i++)
            {
                // 1.若属性头的名称包含'.',就表示是子类里的属性，那么就要遍历子类，eg：UserEn.TrueName
                if (keys[i].IndexOf(".", StringComparison.Ordinal) >= 0)
                {
                    // 1)解析子类属性
                    var properotyArray =
                        keys[i].Split(new string[] { "." }, StringSplitOptions.RemoveEmptyEntries);
                    var subClassName = properotyArray[0]; // '.'前面的为子类的名称
                    var subClassProperotyName = properotyArray[1]; // '.'后面的为子类的属性名称
                    var subClassInfo = en.GetType().GetProperty(subClassName); // 获取子类的类型
                    if (subClassInfo == null)
                    {
                        continue;
                    }

                    // 2)获取子类的实例
                    var subClassEn = en.GetType().GetProperty(subClassName)?.GetValue(en, null);
                    // 3)根据属性名称获取子类里的属性信息
                    var properotyInfo =
                        subClassInfo.PropertyType.GetProperty(subClassProperotyName);
                    if (properotyInfo != null)
                    {
                        try
                        {
                            // Excel单元格的值转换为对象属性的值，若类型不对，记录出错信息
                            properotyInfo.SetValue(subClassEn,
                                GetExcelCellToProperty(properotyInfo.PropertyType, row.GetCell(i)), null);
                        }
                        catch (Exception)
                        {
                            if (errStr.Length == 0)
                            {
                                errStr = "第" + rowIndex + "行数据转换异常：";
                            }

                            errStr += cellHeader[keys[i]] + "列；";
                        }

                    }
                }
                else
                {
                    // 2.给指定的属性赋值
                    var properotyInfo = en.GetType().GetProperty(keys[i]);
                    if (properotyInfo == null)
                    {
                        continue;
                    }

                    try
                    {
                        // Excel单元格的值转换为对象属性的值，若类型不对，记录出错信息
                        properotyInfo.SetValue(en,
                            GetExcelCellToProperty(properotyInfo.PropertyType, row.GetCell(i)), null);
                    }
                    catch (Exception)
                    {
                        if (errStr.Length == 0)
                        {
                            errStr = "第" + rowIndex + "行数据转换异常：";
                        }

                        errStr += cellHeader[keys[i]] + "列；";
                    }
                }
            }

            // 若有错误信息，就添加到错误信息里
            if (errStr.Length > 0)
            {
                errorMsg.AppendLine(errStr);
            }
        }

        /// <summary>
        /// Excel Cell转换为实体的属性值
        /// </summary>
        /// <param name="distanceType">目标对象类型</param>
        /// <param name="sourceCell">对象属性的值</param>
        private static Object GetExcelCellToProperty(Type distanceType, ICell sourceCell)
        {
            var rs = distanceType.IsValueType ? Activator.CreateInstance(distanceType) : null;

            // 1.判断传递的单元格是否为空
            if (sourceCell == null || string.IsNullOrEmpty(sourceCell.ToString()))
            {
                return rs;
            }

            // 2.Excel文本和数字单元格转换，在Excel里文本和数字是不能进行转换，所以这里预先存值
            object sourceValue = null;
            switch (sourceCell.CellType)
            {
                case CellType.Blank:
                    break;

                case CellType.Boolean:
                    break;

                case CellType.Error:
                    break;

                case CellType.Formula:
                    break;

                case CellType.Numeric:
                    sourceValue = sourceCell.NumericCellValue;
                    break;

                case CellType.String:
                    sourceValue = sourceCell.StringCellValue;
                    break;

                case CellType.Unknown:
                    break;

                default:
                    break;
            }

            var valueDataType = distanceType.Name;

            // 在这里进行特定类型的处理
            switch (valueDataType.ToLower()) // 以防出错，全部小写
            {
                case "string":
                    rs = sourceValue.ToString();
                    break;
                case "int":
                case "int16":
                case "int32":
                    rs = (int)Convert.ChangeType(sourceCell.NumericCellValue.ToString(), distanceType);
                    break;
                case "float":
                case "single":
                    rs = (float)Convert.ChangeType(sourceCell.NumericCellValue.ToString(), distanceType);
                    break;
                case "decimal":
                    rs = Convert.ChangeType(sourceCell.NumericCellValue, distanceType);
                    break;
                case "datetime":
                    rs = sourceCell.DateCellValue;
                    break;
                case "guid":
                    rs = (Guid)Convert.ChangeType(sourceCell.NumericCellValue.ToString(), distanceType);
                    return rs;
            }

            return rs;
        }

        #endregion

        #region Excel导出

        /// <summary>
        /// 泛型列表List导出到Excel文件
        /// </summary>
        /// <param name="list">源List表</param>
        /// <param name="strHeaderText">标题信息</param>
        /// <param name="strFileName">保存路径</param>
        /// <param name="titles">列名</param>
        private static void ExportToFile<T>(List<T> list, string strHeaderText, string strFileName,
            string[] titles = null)
        {
            //转换数据源
            DataTable dtSource = ListToDataTable(list, titles);
            //开始导出
            Export(dtSource, strHeaderText, strFileName);
        }

        /// <summary>
        /// DataTable导出到Excel文件
        /// </summary>
        /// <param name="dtSource">源DataTable</param>
        /// <param name="strHeaderText">标题信息</param>
        /// <param name="strFileName">保存路径</param>
        private static void Export(DataTable dtSource, string strHeaderText, string strFileName)
        {
            using MemoryStream ms = Export(dtSource, strHeaderText);
            using FileStream fs = new FileStream(strFileName, FileMode.Create, FileAccess.Write);
            byte[] data = ms.ToArray();
            fs.Write(data, 0, data.Length);
            fs.Flush();
        }

        /// <summary>
        /// DataTable导出到Excel的MemoryStream
        /// </summary>
        /// <param name="dtSource">源DataTable</param>
        /// <param name="strHeaderText">标题信息</param>
        private static MemoryStream Export(DataTable dtSource, string strHeaderText)
        {
            var workbook = new XSSFWorkbook();
            ISheet sheet = workbook.CreateSheet(strHeaderText);
            ICellStyle dateStyle = workbook.CreateCellStyle();
            IDataFormat format = workbook.CreateDataFormat();
            dateStyle.DataFormat = format.GetFormat("yyyy-mm-dd");

            //取得列宽
            int[] arrColWidth = new int[dtSource.Columns.Count];
            foreach (DataColumn item in dtSource.Columns)
            {
                arrColWidth[item.Ordinal] = Encoding.GetEncoding(936).GetBytes(item.ColumnName.ToString()).Length;
            }
            for (int i = 0; i < dtSource.Rows.Count; i++)
            {
                for (int j = 0; j < dtSource.Columns.Count; j++)
                {
                    int intTemp = Encoding.GetEncoding(936).GetBytes(dtSource.Rows[i][j].ToString()).Length;
                    if (intTemp > arrColWidth[j])
                    {
                        arrColWidth[j] = intTemp;
                    }
                }
            }
            int rowIndex = 0;
            foreach (DataRow row in dtSource.Rows)
            {
                #region 新建表，填充表头，填充列头，样式
                if (rowIndex == 65535 || rowIndex == 0)
                {
                    if (rowIndex != 0)
                    {
                        sheet = workbook.CreateSheet();
                    }

                    #region 表头及样式
                    // {
                    //     IRow headerRow = sheet.CreateRow(0);
                    //     headerRow.HeightInPoints = 25;
                    //     headerRow.CreateCell(0).SetCellValue(strHeaderText);
                    //
                    //     ICellStyle headStyle = workbook.CreateCellStyle();
                    //     headStyle.Alignment = HorizontalAlignment.Center;
                    //     IFont font = workbook.CreateFont();
                    //     font.FontHeightInPoints = 20;
                    //     font.IsBold = true;
                    //     headStyle.SetFont(font);
                    //     headerRow.GetCell(0).CellStyle = headStyle;
                    //     //CellRangeAddress四个参数为：起始行，结束行，起始列，结束列
                    //     sheet.AddMergedRegion(new NPOI.SS.Util.CellRangeAddress(0, 0, 0, dtSource.Columns.Count - 1));
                    // }
                    #endregion

                    #region 列头及样式
                    {
                        IRow headerRow = sheet.CreateRow(0);
                        // ICellStyle headStyle = workbook.CreateCellStyle();
                        // headStyle.Alignment = HorizontalAlignment.Center;
                        // IFont font = workbook.CreateFont();
                        // font.FontHeightInPoints = 10;
                        // font.IsBold = true;
                        // headStyle.SetFont(font);
                        foreach (DataColumn column in dtSource.Columns)
                        {
                            headerRow.CreateCell(column.Ordinal).SetCellValue(column.ColumnName);
                            // headerRow.GetCell(column.Ordinal).CellStyle = headStyle;
                            //设置列宽
                            //sheet.SetColumnWidth(column.Ordinal, (arrColWidth[column.Ordinal] + 1) * 256);
                            sheet.SetColumnWidth(column.Ordinal, 15 * 256);
                        }
                    }
                    #endregion

                    rowIndex = 1;
                }
                #endregion

                #region 填充内容
                IRow dataRow = sheet.CreateRow(rowIndex);
                foreach (DataColumn column in dtSource.Columns)
                {
                    ICell newCell = dataRow.CreateCell(column.Ordinal);
                    string drValue = row[column].ToString();
                    switch (column.DataType.ToString())
                    {
                        case "System.String"://字符串类型
                            newCell.SetCellValue(drValue);
                            break;
                        case "System.DateTime"://日期类型
                            DateTime.TryParse(drValue, out DateTime dateV);
                            newCell.SetCellValue(dateV);
                            newCell.CellStyle = dateStyle;//格式化显示
                            break;
                        case "System.Boolean"://布尔型
                            bool.TryParse(drValue, out bool boolV);
                            newCell.SetCellValue(boolV);
                            break;
                        case "System.Int16"://整型
                        case "System.Int32":
                        case "System.Int64":
                        case "System.Byte":
                            int.TryParse(drValue, out int intV);
                            newCell.SetCellValue(intV);
                            break;
                        case "System.Decimal"://浮点型
                        case "System.Double":
                            double.TryParse(drValue, out double doubV);
                            newCell.SetCellValue(doubV);
                            break;
                        case "System.DBNull"://空值处理
                            newCell.SetCellValue("");
                            break;
                        default:
                            newCell.SetCellValue("");
                            break;
                    }
                }
                #endregion

                rowIndex++;
            }

            using MemoryStream ms = new MemoryStream();
            workbook.Write(ms);
            return ms;
        }

        /// <summary>
        /// 泛型列表List转换为DataTable
        /// </summary>
        /// <typeparam name="T">泛型实体</typeparam>
        /// <param name="list">要转换的列表</param>
        /// <param name="titles">标题</param>
        /// <returns></returns>
        private static DataTable ListToDataTable<T>(List<T> list, string[] titles)
        {
            DataTable dt = new DataTable();
            Type listType = typeof(T);
            PropertyInfo[] properties = listType.GetProperties();
            var piList = new List<PropertyInfo>();
            foreach (var property in properties)
            {
                var displayName = property.GetCustomAttribute<DisplayNameAttribute>()?.DisplayName;
                if (string.IsNullOrWhiteSpace(displayName))
                {
                    continue;
                }

                dt.Columns.Add(new DataColumn(displayName, property.PropertyType));
                piList.Add(property);
            }
            
            //内容行
            foreach (T item in list)
            {
                DataRow dr = dt.NewRow();
                for (int i = 0; i < dt.Columns.Count; i++)
                {
                    dr[i] = piList[i].GetValue(item, null);
                }
                dt.Rows.Add(dr);
            }
            return dt;
        }

        #endregion

        public static void ExportBatchDetail(IEnumerable<SkuDetailDto> skuDetailDtos, string url)
        {
            var skuDetailExportDtos = skuDetailDtos.Select(skuDetailDto => new SkuDetailExportDto
                {
                    SkuName = skuDetailDto.Sku.SkuName,
                    SkuCode = skuDetailDto.Sku.SkuCode,
                    SkuSpec = skuDetailDto.SkuSpec,
                    SkuLot1 = skuDetailDto.SkuLot1,
                    PlanQty = skuDetailDto.PlanQty,
                    TrackingNumber = skuDetailDto.TrackingNumber
                }).ToList();

            ExportToFile(skuDetailExportDtos, "sheet1", url);
        }

        public static void ExportSerialNumber(IEnumerable<PackingSerialDetail> skuDetailDtos, string url)
        {
            var serialNumberExportDtos = skuDetailDtos.Select(d => new SerialNumberExportDto
            {
                ProductIdentificationCode = d.ProductIdentificationCode,
                ProductSupportCode = d.ProductSupportCode,
                FrictionBlockBatch=d.FrictionBlockBatch,
                SteelBackBatch = d.SteelBackBatch,
                AssembleDate = d.AssembleDate,
                TrackingNumber = d.TrackingNumber,
                SpringBatch = d.SpringBatch,
                ElasticElementBatch = d.ElasticElementBatch,
                Remark = d.Remark
            }).ToList();
            ExportToFile(serialNumberExportDtos, "sheet1", url);
        }

        public static List<SkuDetailDto> ImportBatchDetail(string filePath)
        {
            // 单元格抬头
            // key：实体对象属性名称，可通过反射获取值
            // value：属性对应的中文注解
            var cellheader = new Dictionary<string, string>
            {
                { "SkuName", "物品名称"},
                { "SkuCode", "物品编码" },
                { "SkuSpec", "型号" },
                { "SkuLot1", "批次" },
                { "PlanQty", "数量" },
                { "TrackingNumber", "追踪号" }
            };

            var skuDetails = ExcelToEntityList<SkuDetailDto>(cellheader, filePath, out var sb);
            var errorMsg = sb.ToString();
            if (!string.IsNullOrWhiteSpace(errorMsg))
            {
                throw new Exception(errorMsg);
            }

            for (var i = 0; i < skuDetails.Count; i++)
            {
                var en = skuDetails[i];
                var errorMsgStr = "第" + (i + 1) + "行数据检测异常：";
                var isHaveNoInputValue = false; // 是否含有未输入项
                if (string.IsNullOrEmpty(en.SkuCode))
                {
                    errorMsgStr += "【物品编码】列不能为空！";
                    isHaveNoInputValue = true;
                }

                if (isHaveNoInputValue) // 若必填项有值未填
                {
                    en.IsExcelVaildateOk = false;
                    sb.AppendLine(errorMsgStr);
                }
            }

            errorMsg = sb.ToString();
            if (!string.IsNullOrWhiteSpace(errorMsg))
            {
                throw new Exception(errorMsg);
            }

            return skuDetails;
        }

        public static List<PackingSerialDetail> ImportSerialDetail(string filePath)
        {
            // 单元格抬头
            // key：实体对象属性名称，可通过反射获取值
            // value：属性对应的中文注解
            var cellheader = new Dictionary<string, string>
            {
                { "ProductIdentificationCode", "产品标识代码" },
                { "ProductSupportCode", "产品辅助代码" },
                { "FrictionBlockBatch", "摩擦块批次" },
                { "SteelBackBatch", "钢背批次" },
                { "AssembleDate", "组装日期" },
                { "TrackingNumber", "追踪号" },
                { "SpringBatch", "卡簧批次" },
                { "ElasticElementBatch", "弹性元件批次" },
                { "Remark", "备注" },
            };

            var packingSerialDetails = ExcelToEntityList<PackingSerialDetail>(cellheader, filePath, out var sb);
            var errorMsg = sb.ToString();
            if (!string.IsNullOrWhiteSpace(errorMsg))
            {
                throw new Exception(errorMsg);
            }

            for (var i = 0; i < packingSerialDetails.Count; i++)
            {
                var en = packingSerialDetails[i];
                var errorMsgStr = "第" + (i + 1) + "行数据检测异常：";
                var isHaveNoInputValue = false; // 是否含有未输入项
                if (string.IsNullOrEmpty(en.ProductIdentificationCode))
                {
                    errorMsgStr += "【产品标识代码】列不能为空！";
                    isHaveNoInputValue = true;
                }

                if (en.ProductSupportCode <= 0 || string.IsNullOrEmpty(en.ProductSupportCode.ToString()))
                {
                    errorMsgStr += "【辅助代码】列不能为空！";
                    isHaveNoInputValue = true;
                }

                if (isHaveNoInputValue) // 若必填项有值未填
                {
                    en.IsExcelVaildateOk = false;
                    sb.AppendLine(errorMsgStr);
                }
            }

            errorMsg = sb.ToString();
            if (!string.IsNullOrWhiteSpace(errorMsg))
            {
                throw new Exception(errorMsg);
            }

            return packingSerialDetails;
        }
    }
}
