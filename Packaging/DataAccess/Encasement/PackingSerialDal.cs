using System;
using System.Collections.Generic;
using System.Linq;
using DataAccess.Dto;
using DataAccess.Enitiies;

namespace DataAccess.Encasement
{
    public class PackingSerialDal
    {
        public static bool SaveImportExcel(List<PackingSerialDetail> packingAutoIdentifications)
        {
            return Db.FreeSql.Insert<PackingSerialDetail>(packingAutoIdentifications)
                .ExecuteAffrows()>0;
        }

        public static void SaveSerialData(SerialNumberPrintDto serialNumberPrintDto)
        {
            Db.FreeSql.Transaction(() =>
            {
                var headerId = Db.FreeSql.Select<PackingSerialHeader>()
                    .Where(d => d.BoxNumber == serialNumberPrintDto.BoxNumber)
                    .First<long>(d => d.Id);
                if (headerId > 0)
                {
                    var serialHeaderId = headerId;
                    Db.FreeSql.Delete<PackingSerialDetail>()
                        .Where(d => d.HeaderId == serialHeaderId)
                        .ExecuteAffrows();
                    Db.FreeSql.Delete<PackingSerialHeader>()
                        .Where(d => d.Id == serialHeaderId)
                        .ExecuteAffrows();
                }

                var packingSerialHeader = new PackingSerialHeader
                {
                    BoxType = serialNumberPrintDto.BoxType,
                    SkuId = serialNumberPrintDto.SkuId,
                    SkuCode = serialNumberPrintDto.SkuCode,
                    SkuName = serialNumberPrintDto.SkuName,
                    SkuNameS = serialNumberPrintDto.SkuNameS,
                    Model = serialNumberPrintDto.Model,
                    ProductionPlan = serialNumberPrintDto.ProductionPlan,
                    PoCode = serialNumberPrintDto.PoCode,
                    WoCode = serialNumberPrintDto.WoCode,
                    SpecialCustomer = serialNumberPrintDto.SpecialCustomer,
                    SpeedClass = serialNumberPrintDto.SpeedClass,
                    AssemblePeople = serialNumberPrintDto.AssemblePeople,
                    UserName = serialNumberPrintDto.UserName,
                    BoxNumber = serialNumberPrintDto.BoxNumber
                };
                headerId = Db.FreeSql.Insert<PackingSerialHeader>(packingSerialHeader)
                    .ExecuteIdentity();
                if (headerId<=0)
                {
                    throw new Exception("保存序列号头表信息失败");
                }

                foreach (var detail in serialNumberPrintDto.SerialDetails)
                {
                    detail.HeaderId = headerId;
                }

                var packingSerialDetails = serialNumberPrintDto.SerialDetails.OrderBy(d => d.ProductSupportCode).ToList();
                var flag = Db.FreeSql.Insert<PackingSerialDetail>(packingSerialDetails)
                    .ExecuteAffrows()>0;
               if (!flag)
               {
                   throw new Exception("保存序列号明细信息失败");
               }
            });
        }

        public static bool ExistSerialBoxNumber(string boxNumber)
        {
           return Db.FreeSql.Select<PackingSerialHeader>()
                .Any(d => d.BoxNumber == boxNumber);
        }

        public static bool ExistBatchBoxNumber(string boxNumber)
        {
            return Db.FreeSql.Select<PackingBatchHeader>()
                .Any(d => d.BoxNumber == boxNumber);
        }

        public static PackingSerialHeader GetPackingSerialHeader(string boxNumber)
        {
            return Db.FreeSql.Select<PackingSerialHeader>()
                .Where(d => d.BoxNumber == boxNumber)
                .First();
        }

        public static PackingBatchHeader GetPackingBatchHeader(string boxNumber)
        {
            return Db.FreeSql.Select<PackingBatchHeader>()
                .Where(d => d.BoxNumber == boxNumber)
                .First();
        }

        public static List<PackingSerialDetail> GetPackingSerialDetails(long headerId)
        {
            return Db.FreeSql.Select<PackingSerialDetail>()
                .Where(d => d.HeaderId == headerId)
                .ToList();
        }

        public static List<PackingBatchDetail> GetPackingBatchDetails(long headerId)
        {
            return Db.FreeSql.Select<PackingBatchDetail>()
                .Where(d => d.HeaderId == headerId)
                .ToList();
        }

        public static List<ReportSerialDto> GetReportSerials(DateTime beginDateTime, DateTime endDateTime)
        {
            return Db.FreeSql.Select<PackingSerialHeader, PackingSerialDetail>()
                .InnerJoin(d => d.t1.Id == d.t2.HeaderId)
                .Where(d => d.t1.CreateTime >= beginDateTime && d.t1.CreateTime <= endDateTime)
                .ToList(d => new ReportSerialDto()
                {
                    CreateTime = d.t1.CreateTime.Value,
                    BoxNumber = d.t1.BoxNumber,
                    SkuNameS = d.t1.SkuNameS,
                    Model = d.t1.Model,
                    UserName = d.t1.UserName,
                    AssemblePeople = d.t1.AssemblePeople,
                    AssembleDate = d.t2.AssembleDate,
                    ProductIdentificationCode = d.t2.ProductIdentificationCode,
                    ProductSupportCode = d.t2.ProductSupportCode,
                    FrictionBlockBatch = d.t2.FrictionBlockBatch,
                    SteelBackBatch = d.t2.SteelBackBatch,
                    SpringBatch = d.t2.SpringBatch,
                    ElasticElementBatch = d.t2.ElasticElementBatch,
                    TrackingNumber = d.t2.TrackingNumber,
                    SizeRange = d.t2.SizeRange,
                    Remark = d.t2.Remark
                });
        }
    }
}