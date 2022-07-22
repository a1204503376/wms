using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Encasement
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