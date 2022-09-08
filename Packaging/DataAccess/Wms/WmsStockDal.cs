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
    }
}