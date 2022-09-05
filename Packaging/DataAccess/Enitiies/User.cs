namespace DataAccess.Enitiies
{
    public class User : BaseEntity<long>
    {
        public string Account { get; set; }
        public string Password { get; set; }
        public string Name { get; set; }
        public string RealName { get; set; }
        public int Status { get; set; }
    }
}
