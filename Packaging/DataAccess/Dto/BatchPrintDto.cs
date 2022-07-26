using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using DataAccess.Enitiies;

namespace DataAccess.Dto
{
    public class BatchPrintDto
    {
        public string SkuNames => string.Join("、", 
            SkuDetails.Select(d => d.Sku.SkuName));

        public string Model => SkuDetails.First(d=>
            !string.IsNullOrWhiteSpace(d.SkuSpec)).SkuSpec;
        public string BoxType { get; set; }
        public string PrintDate { get; set; }
        public string UserName { get; set; }
        public string BoxNumber { get; set; }
        public short Copies { get; set; }
        public string SpecialCustomer { get; set; }
        public List<SkuDetail> SkuDetails { get; set; }
        public List<ReceiveDetailLpn> ReceiveDetailLpns { get; set; }
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