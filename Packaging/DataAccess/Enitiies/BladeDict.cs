namespace DataAccess.Enitiies
{
    public class BladeDict:BaseEntity<long>
    {
        public string Code { get; set; }
        public string DictValue { get; set; }
        public int IsSealed { get; set; }
        public int DictKey { get; set; }
    }
}