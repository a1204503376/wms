using NPOI.HSSF.UserModel;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System;
using System.Linq;
using DataAccess.Enitiies;
using NPOI.SS.UserModel;
using NPOI.XSSF.UserModel;
using System.Text.RegularExpressions;
using DataAccess.Dto;

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
            List<T> enlist = new List<T>();
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
            List<T> enlist = new List<T>(); // 转换后的集合
            try
            {
                using (FileStream fs = File.OpenRead(filePath))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(fs);
                    HSSFSheet sheet = (HSSFSheet)workbook.GetSheetAt(0); // 获取此文件第一个Sheet页
                    for (int rowIndex = startIndex; rowIndex <= sheet.LastRowNum; rowIndex++)
                    {
                        // 1.判断当前行是否空行，若空行就不在进行读取下一行操作，结束Excel读取操作
                        IRow row = sheet.GetRow(rowIndex);
                        if (row == null)
                        {
                            break;
                        }

                        // 2.每一个Excel row转换为一个实体对象
                        T en = new T();
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
            List<T> enlist = new List<T>(); // 转换后的集合
            try
            {
                using (FileStream fs = File.OpenRead(filePath))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(fs);
                    XSSFSheet sheet = (XSSFSheet)workbook.GetSheetAt(0); // 获取此文件第一个Sheet页
                    for (int rowIndex = startIndex; rowIndex <= sheet.LastRowNum; rowIndex++)
                    {
                        // 1.判断当前行是否空行，若空行就不在进行读取下一行操作，结束Excel读取操作
                        IRow row = sheet.GetRow(rowIndex);
                        if (row == null)
                        {
                            break;
                        }

                        // 2.每一个Excel row转换为一个实体对象
                        T en = new T();
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
            List<string> keys = cellHeader.Keys.ToList(); // 要赋值的实体对象属性名称
            string errStr = ""; // 当前行转换时，是否有错误信息，格式为：第1行数据转换异常：XXX列；
            for (int i = 0; i < keys.Count; i++)
            {
                // 1.若属性头的名称包含'.',就表示是子类里的属性，那么就要遍历子类，eg：UserEn.TrueName
                if (keys[i].IndexOf(".", StringComparison.Ordinal) >= 0)
                {
                    // 1)解析子类属性
                    string[] properotyArray =
                        keys[i].Split(new string[] { "." }, StringSplitOptions.RemoveEmptyEntries);
                    string subClassName = properotyArray[0]; // '.'前面的为子类的名称
                    string subClassProperotyName = properotyArray[1]; // '.'后面的为子类的属性名称
                    System.Reflection.PropertyInfo subClassInfo = en.GetType().GetProperty(subClassName); // 获取子类的类型
                    if (subClassInfo == null)
                    {
                        continue;
                    }

                    // 2)获取子类的实例
                    var subClassEn = en.GetType().GetProperty(subClassName)?.GetValue(en, null);
                    // 3)根据属性名称获取子类里的属性信息
                    System.Reflection.PropertyInfo properotyInfo =
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
                    System.Reflection.PropertyInfo properotyInfo = en.GetType().GetProperty(keys[i]);
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
            object rs = distanceType.IsValueType ? Activator.CreateInstance(distanceType) : null;

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

            string valueDataType = distanceType.Name;

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


        public static List<SkuDetail> ImportBatchDetail(string filePath)
        {
            // 单元格抬头
            // key：实体对象属性名称，可通过反射获取值
            // value：属性对应的中文注解
            Dictionary<string, string> cellheader = new Dictionary<string, string>
            {
                { "SkuName", "物品名称"},
                { "SkuCode", "物品编码" },
                { "SkuSpec", "型号" },
                { "SkuLot1", "批次" },
                { "PlanQty", "数量" },
                { "TrackingNumber", "追踪号" }
            };

            var skuDetails = ExcelToEntityList<SkuDetail>(cellheader, filePath, out var sb);
            var errorMsg = sb.ToString();
            if (!string.IsNullOrWhiteSpace(errorMsg))
            {
                throw new Exception(errorMsg);
            }

            for (int i = 0; i < skuDetails.Count; i++)
            {
                var en = skuDetails[i];
                string errorMsgStr = "第" + (i + 1) + "行数据检测异常：";
                bool isHaveNoInputValue = false; // 是否含有未输入项
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
            Dictionary<string, string> cellheader = new Dictionary<string, string>
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

            for (int i = 0; i < packingSerialDetails.Count; i++)
            {
                var en = packingSerialDetails[i];
                string errorMsgStr = "第" + (i + 1) + "行数据检测异常：";
                bool isHaveNoInputValue = false; // 是否含有未输入项
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
