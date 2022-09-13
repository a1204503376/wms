using FreeSql.DataAnnotations;

namespace DataAccess.Enitiies
{
    [Table(Name = "wms_sku")]
    public class Sku
    {
        [Column(IsPrimary = true)]
        public long SkuId { get; set; }
        public string SkuCode { get; set; }
        public string SkuName { get; set; }
        /// <summary>
        /// 型号
        /// </summary>
        public string SkuSpec { get; set; }
        public long WspId{ get; set; }
        /// <summary>
        /// 包装名称
        /// </summary>
        public string WspName { get; set; }
        /// <summary>
        /// 产品序列号
        /// </summary>
        public string SkuBarcodeList { get; set; }
        /// <summary>
        /// 箱标名称
        /// </summary>
        public string SkuNameS { get; set; }
        /// <summary>
        /// 适用速度等级
        /// </summary>
        public string SkuRemark { get; set; }
        /// <summary>
        /// 箱型：A,B,C,D
        /// </summary>
        public string Udf1 { get; set; }
    }
}