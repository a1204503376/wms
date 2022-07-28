using System;
using System.Collections.Generic;
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
    public partial class Box : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            var boxPrintRequest = GetBoxPrintRequest();

            var wmsStockList = WmsStockDal.GetByBoxCode(boxPrintRequest.BoxCodes);
            if (wmsStockList.Count==0)
            {
                throw new Exception("箱号对应的库存为空");
            }

            var serialFlag = boxPrintRequest.BoxType.ToLower() != "batch";
            var serialNumberPrintDtos = new List<SerialNumberPrintDto>();
            var batchPrintDtos = new List<BatchPrintDto>();

            var groupBy = wmsStockList.GroupBy(d => d.BoxCode);

            var enumerable = groupBy as IGrouping<string, WmsStock>[] ?? groupBy.ToArray();
            if (serialFlag)
            {
                if (enumerable.Any(grouping => grouping.Count() > 1))
                {
                    throw new Exception("序列号打印一个箱号只允许存在一个物料");
                }
            }

            foreach (var grouping in enumerable)
            {
                if (serialFlag)
                {
                    var wmsStock = grouping.First();
                    var serialNumberPrintDto = new SerialNumberPrintDto
                    {
                        Copies = boxPrintRequest.Copies,
                        SkuName = wmsStock.SkuName,
                        Model = wmsStock.SkuLot2,
                        UserName = boxPrintRequest.UserName,
                        Qty = $"{(wmsStock.StockQty-wmsStock.PickQty):#########} {wmsStock.WsuCode}/箱",
                        BoxNumber = grouping.Key,
                        SpecialCustomer = wmsStock.SkuLot4
                    };
                    List<string> serialList = WmsSerialDal.GetByStockIdList(new List<long> { wmsStock.StockId });
                    SerialNumberPrintDto.SetSerialNumberRanges(serialNumberPrintDto, serialList);
                    serialNumberPrintDtos.Add(serialNumberPrintDto);
                }
                else
                {
                    var batchPrintDto = new BatchPrintDto
                    {
                        UserName = boxPrintRequest.UserName,
                        Copies = boxPrintRequest.Copies,
                        BoxNumber = grouping.Key,
                    };
                    // 同个箱号可能存在多条相同物品的记录，打印时合并处理
                    var skuGroup = grouping.GroupBy(d => d.SkuId);
                    var skuDetails = new List<SkuDetail>();
                    foreach (IGrouping<long, WmsStock> wmsStockGroup in skuGroup)
                    {
                        var planQty = wmsStockGroup.Sum(d => d.StockQty - d.PickQty);
                        if (planQty<=0)
                        {
                            continue;
                        }
                        WmsStock wmsStock = wmsStockGroup.First();
                        skuDetails.Add(new SkuDetail
                        {
                            Sku = new Sku
                            {
                                SkuId = wmsStockGroup.Key,
                                SkuCode = wmsStock.SkuCode,
                                SkuName = wmsStock.SkuName,
                                SkuSpec = wmsStock.SkuLot2,
                                WspName = wmsStock.WsuCode
                            },
                            SkuSpec = wmsStock.SkuLot2,
                            SkuLot1 = wmsStock.SkuLot1,
                            SkuCode = wmsStock.SkuCode,
                            PlanQty = planQty
                        });
                        batchPrintDto.SpecialCustomer = wmsStock.SkuLot4;
                    }
                    batchPrintDto.SkuDetails = skuDetails;
                    BatchPrintDto.SetQty(batchPrintDto);

                    batchPrintDtos.Add(batchPrintDto);
                }
            }

            if (serialNumberPrintDtos.Count==0 && batchPrintDtos.Count==0)
            {
                throw new Exception("不存在打印数据");
            }

            XtraReport report;
            if (serialFlag)
            {
                var numberPrintDtos = PrintHelper.AddCopiesForSerial(serialNumberPrintDtos, boxPrintRequest.Copies);

                report = new SerialNumberReport(numberPrintDtos);
            }
            else
            {
                var printDtos = PrintHelper.AddCopiesForBatch(batchPrintDtos,boxPrintRequest.Copies);

                report = new BatchPackingReport(printDtos);
            }

            using MemoryStream ms = new MemoryStream();
            report.ExportToPdf(ms, new PdfExportOptions() { ShowPrintDialogOnOpen = true });
            WriteDocumentToResponse(ms.ToArray(), "pdf", true, "Report.pdf");
        }

        private BoxPrintRequest GetBoxPrintRequest()
        {
            var boxCodes = Request.Params["BoxCodes"];
            if (string.IsNullOrWhiteSpace(boxCodes))
            {
                throw new Exception("箱号为空");
            }

            var boxType = Request.Params["BoxType"];
            if (string.IsNullOrWhiteSpace(boxType))
            {
                throw new Exception("箱号类型为空");
            }

            var userName = Request.Params["UserName"];
            if (string.IsNullOrWhiteSpace(userName))
            {
                throw new Exception("工号为空");
            }

            var copies = Request.Params["Copies"];
            if (string.IsNullOrWhiteSpace(copies)
                || !int.TryParse(copies, out var _))
            {
                copies = "2";
            }

            var boxPrintRequest = new BoxPrintRequest
            {
                BoxType = boxType,
                BoxCodes = new List<string>(boxCodes.Split(',')),
                Copies = Convert.ToInt16(copies),
                UserName = userName
            };
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

    public class BoxPrintRequest
    {
        public string BoxType { get; set; }
        public List<string> BoxCodes { get; set; }
        public string UserName { get; set; }
        public short Copies { get; set; }

    }
}