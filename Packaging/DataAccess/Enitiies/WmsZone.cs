using FreeSql.DataAnnotations;

namespace DataAccess.Enitiies
{
    public class WmsZone
    {
        [Column(IsPrimary = true)]
        public long ZoneId { get; set; }
        public string ZoneCode { get; set; }
    }
}