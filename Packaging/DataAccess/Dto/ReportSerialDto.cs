using System;

namespace DataAccess.Dto
{
    public class ReportSerialDto
    {
        public DateTime CreateTime { get; set; }
        public string BoxNumber { get; set; }
        public string SkuNameS { get; set; }
        public string Model { get; set; }
        public string UserName { get; set; }
        public string AssemblePeople { get; set; }
        public string AssembleDate { get; set; }
        /// <summary>
        /// 产品标识代码
        /// </summary>
        public string ProductIdentificationCode { get; set; }
        /// <summary>
        /// 辅助代码(序列号)
        /// </summary>
        public int ProductSupportCode { get; set; }
        /// <summary>
        /// 摩擦块批次
        /// </summary>
        public string FrictionBlockBatch { get; set; }
        /// <summary>
        /// 追踪号
        /// </summary>
        public string TrackingNumber { get; set; }
        /// <summary>
        /// 尺寸范围
        /// </summary>
        public string SizeRange { get; set; }
        /// <summary>
        /// 钢背批次
        /// </summary>
        public string SteelBackBatch { get; set; }
        /// <summary>
        /// 卡簧批次
        /// </summary>
        public string SpringBatch { get; set; }
        /// <summary>
        /// 弹性元件批次
        /// </summary>
        public string ElasticElementBatch { get; set; }
        /// <summary>
        /// 备注
        /// </summary>
        public string Remark { get; set; }
    }
}