using FreeSql.DataAnnotations;

namespace DataAccess.Enitiies
{
    public class WmsSkuPackageDetail
    {
        [Column(IsPrimary = true)]
        public long WspdId { get; set; }
        public long WspId { get; set; }
        public string WsuCode { get; set; }
        public string WsuName { get; set; }
        public int IsDeleted { get; set; }
    }
}