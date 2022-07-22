using FreeSql.DataAnnotations;

namespace DataAccess.Enitiies
{
    [Table(Name = "wms_sku")]
    public class Sku
    {
        public long SkuId { get; set; }
        public string SkuCode { get; set; }
        public string SkuName { get; set; }
        /// <summary>
        /// 型号
        /// </summary>
        public string SkuSpec { get; set; }
    }
}