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

            var wmsStockList = WmsStockDal.GetWmsStockList(boxPrintRequest.BoxCode);
            if (wmsStockList == null || wmsStockList.Count==0)
            {
                throw new Exception("箱号对应的库存为空");
            }

            var serialFlag = boxPrintRequest.BoxType.ToLower() != "batch";
            XtraReport report;
            if (serialFlag)
            {
                var serialNumberPrintDtos = GetSerialPrintDtos(wmsStockList, boxPrintRequest);
                var numberPrintDtos = PrintHelper.AddCopiesForSerial(serialNumberPrintDtos, boxPrintRequest.Copies);

                report = new SerialNumberReport(numberPrintDtos);
            }
            else
            {
                var batchPrintDtos = GetBatchPrintDtos(wmsStockList, boxPrintRequest);

                var printDtos = PrintHelper.AddCopiesForBatch(batchPrintDtos, boxPrintRequest.Copies);

                report = new BatchPackingReport(printDtos);
            }

            using MemoryStream ms = new MemoryStream();
            report.ExportToPdf(ms, new PdfExportOptions() { ShowPrintDialogOnOpen = true });
            WriteDocumentToResponse(ms.ToArray(), "pdf", true, "Report.pdf");
        }

        private static List<BatchPrintDto> GetBatchPrintDtos(IEnumerable<WmsStock> wmsStockList, BoxPrintRequest boxPrintRequest)
        {
            var batchPrintDtos = new List<BatchPrintDto>();

            var batchPrintDto = new BatchPrintDto
            {
                UserName = boxPrintRequest.UserName,
                Copies = boxPrintRequest.Copies,
                BoxNumber = boxPrintRequest.BoxCode,
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
                        WspName = wmsStock.WsuCode
                    },
                    SkuSpec = wmsStock.SkuLot2,
                    SkuLot1 = wmsStock.SkuLot1,
                    SkuCode = sku.SkuCode,
                    PlanQty = planQty
                });
                batchPrintDto.SpecialCustomer = wmsStock.SkuLot4;
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

        private static List<SerialNumberPrintDto> GetSerialPrintDtos(IReadOnlyCollection<WmsStock> wmsStockList, BoxPrintRequest boxPrintRequest)
        {
            if (wmsStockList.Select(d => d.SkuCode).Distinct().Count() > 1)
            {
                throw new Exception("序列号打印一个箱号只允许存在一个物料");
            }

            var wmsStock = wmsStockList.First();
            var sku = WmsSkuDal.GetById(wmsStock.SkuId);
            if (sku==null)
            {
                throw new Exception($"根据skuId[{wmsStock.SkuId}]获取sku对象为空!");
            }

            List<SerialNumberRange> serialNumberRanges;
            var serialNumberList = WmsSerialDal.GetSerialNumberList(wmsStockList.Select(d => d.StockId).ToList());
            // 一个物品对应2个序列号，WMS保存奇数的的序列号
            var packingSequenctNumberPairs = BladeDictDal.GetPackingSequenctNumberPairs();
            var isDobuleSerialNumber =
                SerialNumberPrintDto.IsDobuleSerialNumber(packingSequenctNumberPairs, wmsStock.SkuLot2, sku.SkuName);
            if (isDobuleSerialNumber)
            {
                SetDoubleSerialNumberList(serialNumberList);
                serialNumberRanges = SerialNumberPrintDto.GetDoubleSerialNumberRanges(serialNumberList);
            }
            else
            {
                serialNumberRanges = SerialNumberPrintDto.GetSerialNumberRanges(serialNumberList);
            }

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
                    UserName = boxPrintRequest.UserName,
                    Qty = qty,
                    WspName = wmsStock.WsuCode,
                    BoxNumber = wmsStock.BoxCode,
                    SpecialCustomer = wmsStock.SkuLot4
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

        private static void SetDoubleSerialNumberList(ICollection<int> serialNumberList)
        {
            HashSet<int> serialHashSet = new HashSet<int>();
            foreach (var serialNumber in serialNumberList.Where(serialNumber => serialNumber % 2 != 0))
            {
                serialHashSet.Add(serialNumber);
            }

            serialNumberList.Clear();
            // 每个奇数的序列号补充偶数
            foreach (var serialNumber in serialHashSet)
            {
                serialNumberList.Add(serialNumber);
                serialNumberList.Add(serialNumber + 1);
            }
        }

        private BoxPrintRequest GetBoxPrintRequest()
        {
            var boxCode = Request.Params["BoxCode"];
            if (string.IsNullOrWhiteSpace(boxCode))
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
                BoxCode = boxCode,
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
        public string BoxCode { get; set; }
        public string UserName { get; set; }
        public short Copies { get; set; }

    }
}