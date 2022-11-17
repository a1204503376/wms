using System;
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
        public string SkuLot4 { get; set; }
        public string SkuLot5 { get; set; }
        public string SkuLot6 { get; set; }
        public string SkuLot7 { get; set; }
        public string SkuLot8 { get; set; }
        /// <summary>
        /// 打印日期
        /// </summary>
        public string SkuLot9 { get; set; }
        /// <summary>
        /// 旧箱号
        /// 显示：删除前6位，保留剩余的
        /// </summary>
        public string Udf2 { get; set; }
        /// <summary>
        /// 工号
        /// </summary>
        public string Udf4 { get; set; }
        public int IsDeleted { get; set; }
        public long ZoneId { get; set; }

        public bool HasSerial { get; set; }
        public DateTime CreateTime { get; set; }
    }
}