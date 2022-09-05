namespace DataAccess.Enitiies
{
    public class PackingSpeedClass:BaseEntity<long>
    {
        public string SpeedClass { get; set; }
        public int Order { get; set; }
    }
}
