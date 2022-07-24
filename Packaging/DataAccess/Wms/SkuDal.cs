using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class SkuDal
    {
        public static List<Sku> GetAll()
        {
            return Db.FreeSql.Select<Sku>()
                .ToList();
        }
    }
}