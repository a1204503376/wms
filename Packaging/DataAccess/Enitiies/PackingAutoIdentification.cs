using System.ComponentModel;

namespace DataAccess.Enitiies
{
    /// <summary>
    /// 自动标识系统信息
    /// </summary>
    public class PackingAutoIdentification:BaseEntity<long>
    {
        /// <summary>
        /// 型号
        /// </summary>
        [DisplayName("型号")]
        public string Model { get; set; }
        /// <summary>
        /// 组装日期
        /// </summary>
        [DisplayName("组装日期")]
        public string AssembleDate { get; set; }
        /// <summary>
        /// 产品标识代码
        /// </summary>
        [DisplayName("产品标识代码")]
        public string ProductIdentificationCode { get; set; }
        /// <summary>
        /// 产品辅助代码(序列号)
        /// </summary>
        [DisplayName("产品辅助代码")]
        public string ProductSupportCode { get; set; }
        /// <summary>
        /// 摩擦块批次
        /// </summary>
        [DisplayName("摩擦块批次")]
        public string FrictionBlockBatch { get; set; }
        /// <summary>
        /// 追踪号
        /// </summary>
        [DisplayName("追踪号")]
        public string TrackingNumber { get; set; }
        /// <summary>
        /// 尺寸范围
        /// </summary>
        [DisplayName("尺寸范围")]
        public string SizeRange { get; set; }
        /// <summary>
        /// 钢背批次
        /// </summary>
        [DisplayName("钢背批次")]
        public string SteelBackBatch { get; set; }
        /// <summary>
        /// 卡簧批次
        /// </summary>
        [DisplayName("卡簧批次")]
        public string SpringBatch { get; set; }
        /// <summary>
        /// 弹性元件批次
        /// </summary>
        [DisplayName("弹性元件批次")]
        public string ElasticElementBatch { get; set; }
        /// <summary>
        /// 备注
        /// </summary>
        [DisplayName("备注")]
        public string Remark { get; set; }
    }
}