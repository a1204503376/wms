using System;

namespace DataAccess.Dto
{
    public class ReportBatchDto
    {
        public DateTime CreateTime { get; set; }
        public string BoxNumber { get; set; }
        public string SkuCode { get; set; }
        public string SkuNameS { get; set; }
        public string SkuSpec { get; set; }
        public string UserName { get; set; }
        public string SkuLot1 { get; set; }
        public decimal PlanQty { get; set; }
        public string TrackingNumber { get; set; }
    }
}