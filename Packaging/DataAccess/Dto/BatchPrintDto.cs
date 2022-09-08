using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using DataAccess.Enitiies;

namespace DataAccess.Dto
{
    public class BatchPrintDto
    {
        public string SkuNames => string.Join("、", 
            SkuDetails.Select(d => d.Sku.SkuNameS).Distinct());

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
        public string ProductionPlan { get; set; }
        public string PoCode { get; set; }
        public string WoCode { get; set; }
        public string SpecialCustomer { get; set; }
        public List<SkuDetail> SkuDetails { get; set; }
        public string Qty1 { get; set; }
        public string Qty2 { get; set; }
        public string Qty3 { get; set; }
        public string Qty4 { get; set; }
        public List<ReceiveDetailLpn> ReceiveDetailLpns { get; set; }

        public void SetQty()
        {
            // 相关物品的数量进行合并后打印箱贴
            var skuDetails = SkuDetails.GroupBy(d => d.Sku)
                .Select(g => new SkuDetail()
                {
                    Sku=g.Key,
                    PlanQty = g.Sum(p=>p.PlanQty)
                }).ToList();

            var oneFlag = skuDetails.Count == 1;
            for (var i = 0; i < skuDetails.Count; i++)
            {
                var skuDetail = skuDetails[i];
                var qty = $@"{(!oneFlag ? skuDetail.Sku.SkuName + " " : "")}{skuDetail.PlanQty:#########} {skuDetail.Sku.WspName}/箱";
                switch (i)
                {
                    case 0:
                        Qty1 = qty;
                        break;
                    case 1:
                        Qty2 = qty;
                        break;
                    case 2:
                        Qty3 = qty;
                        break;
                    case 3:
                        Qty4 = qty;
                        break;
                }
            }
        }
    }

    public class SkuDetail
    {
        public string SkuName { get; set; }
        public string SkuCode { get; set; }
        public string SkuSpec { get; set; }
        public decimal PlanQty { get; set; }
        public string SkuLot1 { get; set; }
        public string TrackingNumber { get; set; }
        public Sku Sku { get; set; }
        public bool IsExcelVaildateOk { get; set; }
    }
}