using System.ComponentModel;

namespace DataAccess.Dto
{
    public class SerialNumberExportDto
    {
        /// <summary>
        /// 产品标识代码
        /// </summary>
        [DisplayName("产品标识代码")]
        public string ProductIdentificationCode { get; set; }
        /// <summary>
        /// 辅助代码(序列号)
        /// </summary>
        [DisplayName("辅助代码")]
        public int ProductSupportCode { get; set; }
        /// <summary>
        /// 摩擦块批次
        /// </summary>
        [DisplayName("摩擦块批次")]
        public string FrictionBlockBatch { get; set; }
        /// <summary>
        /// 钢背批次
        /// </summary>
        [DisplayName("钢背批次")]
        public string SteelBackBatch { get; set; }
        /// <summary>
        /// 组装日期
        /// </summary>
        [DisplayName("组装日期")]
        public string AssembleDate { get; set; }
        /// <summary>
        /// 追踪号
        /// </summary>
        [DisplayName("追踪号")]
        public string TrackingNumber { get; set; }
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