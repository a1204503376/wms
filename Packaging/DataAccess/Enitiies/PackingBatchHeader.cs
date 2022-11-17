namespace DataAccess.Enitiies
{
    public class PackingBatchHeader:BaseEntity<long>
    {
        /// <summary>
        /// 箱型：A,B,C,D
        /// </summary>
        public string BoxType { get; set; }
        /// <summary>
        /// 箱号
        /// </summary>
        public string BoxNumber { get; set; }
        public string ProductionPlan { get; set; }
        public string PoCode { get; set; }
        public string WoCode { get; set; }
        public string SpecialCustomer { get; set; }
        /// <summary>
        /// 用户名
        /// </summary>
        public string UserName { get; set; }

        public string PrintDate { get; set; }
    }
}