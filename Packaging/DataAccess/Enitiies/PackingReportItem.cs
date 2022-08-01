namespace DataAccess.Enitiies
{
    public class PackingReportItem:BaseEntity<long>
    {
        public string Name { get; set; }
        public string DisplayName { get; set; }
        public byte[] LayoutData { get; set; }
    }
}