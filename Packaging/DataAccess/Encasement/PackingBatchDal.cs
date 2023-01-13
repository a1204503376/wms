using System;
using System.Collections.Generic;
using DataAccess.Dto;
using DataAccess.Enitiies;

namespace DataAccess.Encasement
{
    public class PackingBatchDal
    {
        public static void SaveBatchData(BatchPrintDto batchPrintDto)
        {
            var headerId = Db.FreeSql.Select<PackingBatchHeader>()
                .Where(d => d.BoxNumber == batchPrintDto.BoxNumber)
                .First<long>(d => d.Id);
            if (headerId > 0)
            {
                var batchHeaderId = headerId;
                Db.FreeSql.Delete<PackingBatchDetail>()
                    .Where(d => d.HeaderId == batchHeaderId)
                    .ExecuteAffrows();
                Db.FreeSql.Delete<PackingBatchHeader>()
                    .Where(d => d.Id == batchHeaderId)
                    .ExecuteAffrows();
            }

            var packingBatchHeader = new PackingBatchHeader
            {
                BoxType = batchPrintDto.BoxType,
                BoxNumber = batchPrintDto.BoxNumber,
                ProductionPlan = batchPrintDto.ProductionPlan,
                PoCode = batchPrintDto.PoCode,
                WoCode = batchPrintDto.WoCode,
                SpecialCustomer = batchPrintDto.SpecialCustomer,
                UserName = batchPrintDto.UserName,
                PrintDate = batchPrintDto.PrintDate
            };

            headerId = Db.FreeSql.Insert(packingBatchHeader)
                .ExecuteIdentity();
            if (headerId <= 0)
            {
                throw new Exception("保存批次号装箱头表信息失败");
            }

            var packingBatches = new List<PackingBatchDetail>();
            foreach (var skuDetail in batchPrintDto.SkuDetails)
            {
                var packingBatch = new PackingBatchDetail
                {
                    HeaderId = headerId,
                    SkuId = skuDetail.Sku.SkuId,
                    SkuCode = skuDetail.Sku.SkuCode,
                    SkuName = skuDetail.Sku.SkuName,
                    SkuNameS = skuDetail.Sku.SkuNameS,
                    SkuLot1 = skuDetail.SkuLot1,
                    Qty = skuDetail.PlanQty,
                    Model = batchPrintDto.Model,
                    TrackingNumber = skuDetail.TrackingNumber
                };
                packingBatches.Add(packingBatch);
            }

            var flag = Db.FreeSql.Insert(packingBatches)
                .ExecuteAffrows() > 0;
            if (!flag)
            {
                throw new Exception("保存批次号装箱明细信息失败");
            }
        }

        public static List<ReportBatchDto> GetReportBatchDtos(DateTime beginDateTime, DateTime enDateTime)
        {
            return Db.FreeSql.Select<PackingBatchHeader, PackingBatchDetail>()
                .InnerJoin(d => d.t1.Id == d.t2.HeaderId)
                .Where(d => d.t1.CreateTime >= beginDateTime && d.t1.CreateTime <= enDateTime)
                .ToList(d => new ReportBatchDto()
                {
                    CreateTime = d.t1.CreateTime.Value,
                    BoxNumber = d.t1.BoxNumber,
                    SkuSpec = d.t2.Model,
                    SkuCode = d.t2.SkuCode,
                    SkuNameS = d.t2.SkuNameS,
                    SkuLot1 = d.t2.SkuLot1,
                    PlanQty = d.t2.Qty,
                    TrackingNumber = d.t2.TrackingNumber,
                    UserName = d.t1.UserName
                });
        }
    }
}