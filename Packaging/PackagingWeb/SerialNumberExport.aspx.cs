using DataAccess.Common;
using DataAccess.Dto;
using DataAccess.Enitiies;
using DataAccess.Wms;
using DevExpress.XtraPrinting;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using PackagingWeb.PredefinedReports;

namespace PackagingWeb
{
    public partial class SerialNumberExport : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            var boxPrintRequest = GetBoxPrintRequest();

            var wmsStockList = WmsStockDal.GetStagingAreaList(boxPrintRequest.StartDate, boxPrintRequest.EndDate, true);

            if (wmsStockList.Count == 0)
            {
                throw new Exception("没有找到库存数据");
            }

            // 是否打印UDF2（旧箱号）
            if (!boxPrintRequest.PrintUdf2Flag)
            {
                wmsStockList.ForEach(d => d.Udf2 = string.Empty);
            }

            var numberPrintDtos = new List<SerialNumberPrintDto>();
            wmsStockList = wmsStockList
                .OrderBy(d => d.SkuLot2)
                .ThenBy(d => d.BoxCode)
                .ToList();

            var groupBy = wmsStockList.GroupBy(d => d.BoxCode).ToList();

            foreach (var wmsStock in groupBy)
            {
                var serialNumberPrintDtos = GetSerialPrintDtos(wmsStock.ToList(), boxPrintRequest);
                numberPrintDtos.AddRange(PrintHelper.AddCopiesForSerial(serialNumberPrintDtos, boxPrintRequest.Copies));
            }

            var report = new SerialNumberReport(numberPrintDtos);

            using MemoryStream ms = new MemoryStream();
            report.ExportToPdf(ms, new PdfExportOptions() { ShowPrintDialogOnOpen = true });
            WriteDocumentToResponse(ms.ToArray(), "pdf", true, "Report.pdf");
        }

        private static List<SerialNumberPrintDto> GetSerialPrintDtos(IReadOnlyCollection<WmsStock> wmsStockList, BatchPrintRequest boxPrintRequest)
        {
            if (wmsStockList.Select(d => d.SkuCode).Distinct().Count() > 1)
            {
                throw new Exception("序列号打印一个箱号只允许存在一个物料");
            }

            var wmsStock = wmsStockList.First();
            var sku = WmsSkuDal.GetById(wmsStock.SkuId);
            if (sku == null)
            {
                throw new Exception($"根据skuId[{wmsStock.SkuId}]获取sku对象为空!");
            }

            var serialNumberList = WmsSerialDal.GetSerialNumberList(wmsStockList.Select(d => d.StockId).ToList());

            List<SerialNumberRange> serialNumberRanges = SerialNumberPrintDto.GetContinuousRanges(serialNumberList);


            var groupSerialNumber =
                (int)Math.Ceiling((double)serialNumberRanges.Count / SerialNumberPrintDto.SerialGroupNumber);


            // 数量计算，同一个物品不同批属性合并序列号数量
            var qty = wmsStockList.Select(d => d.StockQty - d.PickQty).Sum();

            var serialNumberPrintDtos = new List<SerialNumberPrintDto>();
            for (int i = 0; i < groupSerialNumber; i++)
            {
                var serialNumberPrintDto = new SerialNumberPrintDto
                {
                    Copies = boxPrintRequest.Copies,
                    SkuName = sku.SkuName,
                    SkuNameS = sku.SkuNameS,
                    Model = wmsStock.SkuLot2,
                    UserName = wmsStock.Udf4,
                    Qty = qty,
                    WspName = wmsStock.WsuCode,
                    BoxNumber = wmsStock.BoxCode,
                    SpecialCustomer = wmsStock.SkuLot4,
                    Udf2 = wmsStock.Udf2,
                    PrintDate = wmsStock.SkuLot9,
                    SpeedClass = sku.SkuRemark,
                    ProductIdentificationCode = sku.SkuBarcodeList,
                };

                int index = (i * SerialNumberPrintDto.SerialGroupNumber);
                int count;
                if (i == 0 && serialNumberRanges.Count < SerialNumberPrintDto.SerialGroupNumber)
                {
                    count = serialNumberRanges.Count;
                }
                else if ((i + 1) == groupSerialNumber &&
                         (serialNumberRanges.Count - index) < SerialNumberPrintDto.SerialGroupNumber)
                {
                    count = serialNumberRanges.Count - index;
                }
                else
                {
                    count = SerialNumberPrintDto.SerialGroupNumber;
                }

                var numberRanges = serialNumberRanges.GetRange(index, count);
                serialNumberPrintDto.SetSerial(numberRanges);
                serialNumberPrintDtos.Add(serialNumberPrintDto);
            }

            if (serialNumberPrintDtos.Count == 0)
            {
                throw new Exception("不存在打印数据");
            }

            return serialNumberPrintDtos;
        }

        void WriteDocumentToResponse(byte[] documentData, string format, bool isInline, string fileName)
        {
            string contentType;
            string disposition = (isInline) ? "inline" : "attachment";

            switch (format.ToLower())
            {
                case "xls":
                    contentType = "application/vnd.ms-excel";
                    break;
                case "xlsx":
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    break;
                case "mht":
                    contentType = "message/rfc822";
                    break;
                case "html":
                    contentType = "text/html";
                    break;
                case "txt":
                case "csv":
                    contentType = "text/plain";
                    break;
                case "png":
                    contentType = "image/png";
                    break;
                default:
                    contentType = $"application/{format}";
                    break;
            }

            Response.Clear();
            Response.ContentType = contentType;
            Response.AddHeader("Content-Disposition", $"{disposition}; filename={fileName}");
            Response.BinaryWrite(documentData);
            Response.End();
        }

        private BatchPrintRequest GetBoxPrintRequest()
        {
            var copies = Request.QueryString["Copies"];
            if (string.IsNullOrWhiteSpace(copies)
                || !int.TryParse(copies, out var _))
            {
                copies = ConfigurationManager.AppSettings["Copies"];
            }

            var printUdf2Flag = Request.QueryString["PrintUdf2Flag"];
            if (string.IsNullOrWhiteSpace(printUdf2Flag)
                || !bool.TryParse(printUdf2Flag, out var _))
            {
                printUdf2Flag = ConfigurationManager.AppSettings["PrintUdf2Flag"];
            }

            var boxPrintRequest = new BatchPrintRequest
            {
                Copies = Convert.ToInt16(copies),
                PrintUdf2Flag = Convert.ToBoolean(printUdf2Flag)
            };

            var startDate = Request.QueryString["StartDate"];
            if (!string.IsNullOrWhiteSpace(startDate))
            {
                boxPrintRequest.StartDate = DateTime.Parse(startDate);
            }
            var endDate = Request.QueryString["EndDate"];
            if (!string.IsNullOrWhiteSpace(endDate))
            {
                boxPrintRequest.EndDate = DateTime.Parse(endDate);
            }

            return boxPrintRequest;
        }
    }
}