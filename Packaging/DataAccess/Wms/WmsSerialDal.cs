using System;
using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class WmsSerialDal
    {
        public static List<int> GetSerialNumberList(List<long> stockiDList)
        {
            return Db.FreeSql.Select<WmsSerial>()
                .Where(d => stockiDList.Contains(d.StockId)
                            && d.SerialState == 0
                            && d.IsDeleted == 0)
                .ToList(d=>Convert.ToInt32(d.SerialNumber));
        }
    }
}