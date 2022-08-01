using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class WmsStockDal
    {
        public static List<WmsStock> GetByBoxCode(List<string> boxCodeList)
        {
            return Db.FreeSql.Select<WmsStock>()
                .Where(d => boxCodeList.Contains(d.BoxCode)
                && d.StockQty>d.PickQty
                && d.IsDeleted==0)
                .ToList();
        }
    }
}