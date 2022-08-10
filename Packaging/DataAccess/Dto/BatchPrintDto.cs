using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Linq;
using System.Runtime.CompilerServices;
using DataAccess.Enitiies;
using DevExpress.DataAccess.ObjectBinding;

namespace DataAccess.Dto
{
    public class BatchPrintDto
    {
        public string SkuNames => string.Join("、", 
            SkuDetails.Select(d => d.Sku.SkuName));

        public string Model => SkuDetails.First(d=>
            !string.IsNullOrWhiteSpace(d.SkuSpec)).SkuSpec;
        public string BoxType { get; set; }
        public string PrintDate { get; set; } = DateTime.Now.ToString("yyMMdd");
        public string UserName { get; set; }
        public string BoxNumber { get; set; }

        public string BoxNumberLabel
        {
            get
            {
                return $"{ConfigurationManager.AppSettings["BoxCodePrefix"]}{BoxNumber}";
            }
        }

        public string BoxNumberSuffix
        {
            get => BoxNumber.Substring(BoxNumber.Length - 4, 4);
        }
        public short Copies { get; set; }
        public string SpecialCustomer { get; set; }
        public List<SkuDetail> SkuDetails { get; set; }
        public string Qty1 { get; set; }
        public string Qty2 { get; set; }
        public string Qty3 { get; set; }
        public string Qty4 { get; set; }
        public List<ReceiveDetailLpn> ReceiveDetailLpns { get; set; }

        public static void SetQty(BatchPrintDto batchPrintDto)
        {
            var oneFlag = batchPrintDto.SkuDetails.Count == 1;
            for (var i = 0; i < batchPrintDto.SkuDetails.Count; i++)
            {
                var skuDetail = batchPrintDto.SkuDetails[i];
                var qty = $@"{(!oneFlag ? skuDetail.Sku.SkuName + " " : "")}{skuDetail.PlanQty:#########} {skuDetail.Sku.WspName}/箱";
                switch (i)
                {
                    case 0:
                        batchPrintDto.Qty1 = qty;
                        break;
                    case 1:
                        batchPrintDto.Qty2 = qty;
                        break;
                    case 2:
                        batchPrintDto.Qty3 = qty;
                        break;
                    case 3:
                        batchPrintDto.Qty4 = qty;
                        break;
                }
            }
        }
    }

    public class SkuDetail
    {
        public Sku Sku { get; set; }
        public string SkuCode { get; set; }
        public string SkuSpec { get; set; }
        public decimal PlanQty { get; set; }
        public string SkuLot1 { get; set; }
    }
}