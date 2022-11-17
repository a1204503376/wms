namespace DataAccess.Enitiies
{
    /// <summary>
    /// 序列号头表信息
    /// </summary>
    public class PackingSerialHeader:BaseEntity<long>
    {
        /// <summary>
        /// 箱型：A,B,C,D
        /// </summary>
        public string BoxType { get; set; }
        /// <summary>
        /// 箱号
        /// </summary>
        public string BoxNumber { get; set; }
        public long SkuId { get; set; }
        public string SkuCode { get; set; }
        public string SkuName { get; set; }
        public string SkuNameS { get; set; }
        /// <summary>
        /// 型号
        /// </summary>
        public string Model { get; set; }
        public string ProductionPlan { get; set; }
        public string PoCode { get; set; }
        public string WoCode { get; set; }
        public string SpecialCustomer { get; set; }
        public string SpeedClass { get; set; }
        /// <summary>
        /// 用户名
        /// </summary>
        public string UserName { get; set; }

        public string AssemblePeople { get; set; }
        public string PrintDate { get; set; }
    }
}