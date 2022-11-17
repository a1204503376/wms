using System;
using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class WmsStockDal
    {
        public static List<WmsStock> GetWmsStockList(string boxCode)
        {
            return Db.FreeSql.Select<WmsStock>()
                .Where(d => d.BoxCode == boxCode
                            && d.StockQty > d.PickQty
                            && d.IsDeleted == 0)
                .ToList();
        }

        public static List<WmsStock> GetStagingAreaList(DateTime? startDate,DateTime? endDate,bool hasSerial)
        {
            // 入库暂存区
            var zoneId = Db.FreeSql.Select<WmsZone>()
                .Where(d => d.ZoneCode == "WH1-STAGE")
                .ToOne(d => d.ZoneId);

            return Db.FreeSql.Select<WmsStock>()
                .Where(d=>d.ZoneId ==zoneId && !string.IsNullOrWhiteSpace(d.Udf2) && d.HasSerial == hasSerial)
                .WhereIf(startDate.HasValue, d => d.CreateTime >= startDate.Value)
                .WhereIf(endDate.HasValue, d => d.CreateTime <= endDate.Value)
                .ToList();
        }
    }
}