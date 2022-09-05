using FreeSql.DataAnnotations;

namespace DataAccess.Enitiies
{
    /// <summary>
    /// 序列号明细信息
    /// </summary>
    public class PackingSerialDetail:BaseEntity<long>
    {
        /// <summary>
        /// 头表主键ID
        /// </summary>
        public long HeaderId { get; set; }
        /// <summary>
        /// 组装日期
        /// </summary>
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
        /// <summary>
        /// 非表字段
        /// 用于多选标识
        /// true：选中，false：非选中
        /// </summary>
        [Column(IsIgnore = true)]
        public bool Checked { get; set; }
        /// <summary>
        /// 导入的EXCEL是否通过校验
        /// </summary>
        [Column(IsIgnore = true)]
        public bool IsExcelVaildateOk { get; set; }
    }
}