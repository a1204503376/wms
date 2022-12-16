using System;
using System.Collections.Generic;
using System.Linq;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class WmsSerialDal
    {
        public static List<int> GetSerialNumberList(List<long> stockiDList)
        {
            // 序列号存储格式：255987000057180102065
            var serialNumberList = Db.FreeSql.Select<WmsSerial>()
                .Where(d => stockiDList.Contains(d.StockId)
                            && d.SerialState == 0
                            && d.IsDeleted == 0)
                .OrderBy(d=>d.SerialNumber)
                .ToList(d=>d.SerialNumber);

            return (from serialNumber in serialNumberList 
                where !string.IsNullOrEmpty(serialNumber) 
                select Convert.ToInt32(serialNumber.Substring(serialNumber.Length - 9, 9))
                ).ToList();
        }
    }
}