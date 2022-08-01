using FreeSql.DataAnnotations;

namespace DataAccess.Enitiies
{
    public class WmsSerial
    {
        [Column(IsPrimary = true)]
        public long SerialId { get; set; }
        public string SerialNumber { get; set; }
        public long StockId { get; set; }
        public int SerialState { get; set; }
        public long IsDeleted { get; set; }
    }
}