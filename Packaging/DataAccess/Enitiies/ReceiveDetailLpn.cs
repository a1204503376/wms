using System;
using DataAccess.Enums;
using FreeSql.DataAnnotations;

namespace DataAccess.Enitiies
{
    public class ReceiveDetailLpn
    {
        [Column(IsPrimary = true)]
        public long Id { get; set; }
        /// <summary>
        /// 收货单头表id
        /// </summary>
        public long ReceiveHeaderId { get; set; }

        /// <summary>
        /// 收货单明细id
        /// </summary>
        public long ReceiveDetailId { get; set; }

        /// <summary>
        /// 接收状态
        /// </summary>
        public ReceiveDetailStatusEnum DetailStatus { get; set; }

        /// <summary>
        /// 供应商id
        /// </summary>
        public long SupplierId { get; set; }
        /// <summary>
        /// 物品id
        /// </summary>
        public long SkuId { get; set; }

        /// <summary>
        /// 物品编码
        /// </summary>
        public string SkuCode { get; set; }

        /// <summary>
        /// 物品名称
        /// </summary>
        public string SkuName { get; set; }

        /// <summary>
        /// 托盘号
        /// </summary>
        public string LpnCode { get; set; }

        /// <summary>
        /// 箱号
        /// </summary>
        public string BoxCode { get; set; }

        /// <summary>
        /// 序列号
        /// </summary>
        public string SnCode { get; set; }

        /// <summary>
        /// 计划数量
        /// </summary>
        public decimal PlanQty { get; set; }

        /// <summary>
        /// 实收数量
        /// </summary>
        public decimal ScanQty { get; set; }

        /// <summary>
        /// 包装id
        /// </summary>
        public long WspId { get; set; }

        /// <summary>
        /// 层级
        /// </summary>
        public int SkuLevel { get; set; }

        /// <summary>
        /// 规格
        /// </summary>
        public string SkuSpec { get; set; }

        /// <summary>
        /// 换算倍率
        /// </summary>
        public int ConvertQty { get; set; }

        /// <summary>
        /// 计量单位编码
        /// </summary>
        public string UmCode { get; set; }

        /// <summary>
        /// 计量单位名称
        /// </summary>
        public string UmName { get; set; }

        /// <summary>
        /// 库房id
        /// </summary>
        public long WhId { get; set; }

        /// <summary>
        /// 库房编码
        /// </summary>
        public string WhCode { get; set; }

        /// <summary>
        /// 基础计量单位编码
        /// </summary>
        public string BaseUmCode { get; set; }

        /// <summary>
        /// 基础计量单位名称
        /// </summary>
        public string BaseUmName { get; set; }

        /// <summary>
        /// 货主id
        /// </summary>
        public long WoId { get; set; }

        /// <summary>
        /// 货主编码
        /// </summary>
        public string OwnerCode { get; set; }

        /// <summary>
        /// 单价
        /// </summary>
        public decimal DetailPrice { get; set; }

        /// <summary>
        /// 明细总金额
        /// </summary>
        public decimal DetailAmount { get; set; }

        /// <summary>
        /// 批属性1
        /// </summary>
        public string SkuLot1{ get; set; }

        /// <summary>
        /// 批属性2
        /// </summary>
        public string SkuLot2{ get; set; }

        /// <summary>
        /// 批属性3
        /// </summary>
        public string SkuLot3{ get; set; }

        /**
		 * 批属性4
		 */
        public string SkuLot4{ get; set; }

        /**
		 * 批属性5
		 */
        public string SkuLot5{ get; set; }

        /**
		 * 批属性6
		 */
        public string SkuLot6{ get; set; }

        /**
		 * 批属性7
		 */
        public string SkuLot7{ get; set; }

        /**
		 * 批属性8
		 */
        public string SkuLot8{ get; set; }

        /**
		 * 批属性9
		 */
        public string SkuLot9{ get; set; }

        /**
		 * 批属性10
		 */
        public string SkuLot10{ get; set; }

        /**
		 * 批属性11
		 */
        public string SkuLot11{ get; set; }

        /**
		 * 批属性12
		 */
        public string SkuLot12{ get; set; }

        /**
		 * 批属性13
		 */
        public string SkuLot13{ get; set; }

        /**
		 * 批属性14
		 */
        public string SkuLot14{ get; set; }

        /**
		 * 批属性15
		 */
        public string SkuLot15{ get; set; }

        /**
		 * 批属性16
		 */
        public string SkuLot16{ get; set; }

        /**
		 * 批属性17
		 */
        public string SkuLot17{ get; set; }

        /**
		 * 批属性18
		 */
        public string SkuLot18{ get; set; }

        /**
		 * 批属性19
		 */
        public string SkuLot19{ get; set; }

        /**
		 * 批属性20
		 */
        public string SkuLot20{ get; set; }

        /**
		 * 批属性21
		 */
        public string SkuLot21{ get; set; }

        /**
		 * 批属性22
		 */
        public string SkuLot22{ get; set; }

        /**
		 * 批属性23
		 */
        public string SkuLot23{ get; set; }

        /**
		 * 批属性24
		 */
        public string SkuLot24{ get; set; }

        /**
		 * 批属性25
		 */
        public string SkuLot25{ get; set; }

        /**
		 * 批属性26
		 */
        public string SkuLot26{ get; set; }

        /**
		 * 批属性27
		 */
        public string SkuLot27{ get; set; }

        /**
		 * 批属性28
		 */
        public string SkuLot28{ get; set; }

        /**
		 * 批属性29
		 */
        public string SkuLot29{ get; set; }

        /**
		 * 批属性30
		 */
        public string SkuLot30{ get; set; }

        public string Udf1 { get; set; }
        public string Udf2 { get; set; }
        public string Udf3 { get; set; }

        public long CreateUser{ get; set; }
        public long CreateDept{ get; set; }

        public DateTime? CreateTime { get; set; } = DateTime.Now;
        public DateTime? UpdateTime { get; set; } = DateTime.Now;
    }
}