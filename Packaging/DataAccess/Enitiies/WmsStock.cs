using FreeSql.DataAnnotations;

namespace DataAccess.Enitiies
{
    public class WmsStock
    {
        [Column(IsPrimary = true)]
        public long StockId { get; set; }
        public long SkuId { get; set; }
        public string SkuCode { get; set; }
        public string SkuName { get; set; }
        public string WsuCode { get; set; }
        public decimal StockQty { get; set; }
        public decimal PickQty { get; set; }
        public string BoxCode { get; set; }
        public string SkuLot1 { get; set; }
        public string SkuLot2 { get; set; }
        public string SkuLot3 { get; set; }
        public string SkuLot4 { get; set; }
        public string SkuLot5 { get; set; }
        public string SkuLot6 { get; set; }
        public string SkuLot7 { get; set; }
        public string SkuLot8 { get; set; }
        public int IsDeleted { get; set; }
    }
}