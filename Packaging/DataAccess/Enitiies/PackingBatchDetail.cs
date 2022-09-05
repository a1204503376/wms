namespace DataAccess.Enitiies
{
    /// <summary>
    /// 批次号装箱明细信息
    /// </summary>
    public class PackingBatchDetail : BaseEntity<long>
    {
        public long HeaderId { get; set; }
        public long SkuId { get; set; }
        public string SkuCode { get; set; }
        public string SkuName { get; set; }
        public string SkuNameS { get; set; }
        public string Model { get; set; }
        public string SkuLot1 { get; set; }
        public decimal Qty { get; set; }
        public string TrackingNumber { get; set; }
    }
}