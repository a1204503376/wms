using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using DataAccess.Common;
using DataAccess.Dto;
using DataAccess.Enitiies;
using DataAccess.Wms;
using DevExpress.XtraPrinting;
using DevExpress.XtraReports.UI;
using PackagingWeb.PredefinedReports;

namespace PackagingWeb
{
    public partial class BatchExport : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            var boxPrintRequest = GetBoxPrintRequest();

            var wmsStockList =
                WmsStockDal.GetStagingAreaList(boxPrintRequest.StartDate, boxPrintRequest.EndDate, false);

            if (wmsStockList.Count == 0)
            {
                throw new Exception("没有找到库存数据");
            }

            // 是否打印UDF2（旧箱号）
            if (!boxPrintRequest.PrintUdf2Flag)
            {
                wmsStockList.ForEach(d => d.Udf2 = string.Empty);
            }

            var printDtos = new List<BatchPrintDto>();
            wmsStockList = wmsStockList
                .OrderBy(d => d.SkuLot2)
                .ThenBy(d => d.BoxCode)
                .ToList();

            var groupBy = wmsStockList.GroupBy(d => d.BoxCode).ToList();

            foreach (var wmsStock in groupBy)
            {
                List<WmsStock> wmsStocks = wmsStock.ToList();
                var batchPrintDtos = GetBatchPrintDtos(wmsStocks, boxPrintRequest);

                printDtos.AddRange(PrintHelper.AddCopiesForBatch(batchPrintDtos, boxPrintRequest.Copies));
            }

            var report = new BatchPackingReport(printDtos);


            using MemoryStream ms = new MemoryStream();
            report.ExportToPdf(ms, new PdfExportOptions() { ShowPrintDialogOnOpen = true });
            WriteDocumentToResponse(ms.ToArray(), "pdf", true, "Report.pdf");
        }

        private static List<BatchPrintDto> GetBatchPrintDtos(IReadOnlyCollection<WmsStock> wmsStockList, BatchPrintRequest boxPrintRequest)
        {
            var batchPrintDtos = new List<BatchPrintDto>();
            var batchPrintDto = new BatchPrintDto
            {
                Copies = boxPrintRequest.Copies,
            };

            // 同个箱号可能存在多条相同物品的记录，打印时合并处理
            var skuGroup = wmsStockList.GroupBy(d => d.SkuId);
            var skuDetails = new List<SkuDetailDto>();
            foreach (var wmsStockGroup in skuGroup)
            {
                var planQty = wmsStockGroup.Sum(d => d.StockQty - d.PickQty);
                if (planQty <= 0)
                {
                    continue;
                }

                WmsStock wmsStock = wmsStockGroup.First();
                var sku = WmsSkuDal.GetById(wmsStock.SkuId);
                skuDetails.Add(new SkuDetailDto
                {
                    Sku = new Sku
                    {
                        SkuId = sku.SkuId,
                        SkuCode = sku.SkuCode,
                        SkuName = sku.SkuName,
                        SkuNameS = sku.SkuNameS,
                        SkuSpec = sku.SkuSpec,
                        WspName = sku.WspName
                    },
                    SkuSpec = wmsStock.SkuLot2,
                    SkuLot1 = wmsStock.SkuLot1,
                    SkuCode = sku.SkuCode,
                    PlanQty = planQty
                });
                batchPrintDto.SpecialCustomer = wmsStock.SkuLot4;
                batchPrintDto.Udf2 = wmsStock.Udf2;
                batchPrintDto.UserName = wmsStock.Udf4;
                batchPrintDto.BoxNumber = wmsStock.BoxCode;
                batchPrintDto.PrintDate = wmsStock.SkuLot9;
            }

            batchPrintDto.SkuDetails = skuDetails;
            batchPrintDto.SetQty();

            batchPrintDtos.Add(batchPrintDto);


            if (batchPrintDtos.Count == 0)
            {
                throw new Exception("不存在打印数据");
            }

            return batchPrintDtos;
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
    }

    public class BatchPrintRequest
    {
        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }
        public short Copies { get; set; }
        public bool PrintUdf2Flag { get; set; }
    }
}