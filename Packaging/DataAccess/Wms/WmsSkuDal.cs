using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class WmsSkuDal
    {
        public static List<Sku> GetAll()
        {
            return Db.FreeSql.Select<Sku>()
                .ToList();
        }

        public static Sku GetById(long skuId)
        {
            return Db.FreeSql.Select<Sku>(skuId)
                .First();
        }
    }
}