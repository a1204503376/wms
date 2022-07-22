using DataAccess.Enitiies;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using FreeSql;

namespace DataAccess.Encasement
{
    public class SpeedClassDal
    {
        public static List<PackingSpeedClass> GetAll()
        {
            return Db.FreeSql.Select<PackingSpeedClass>()
                .OrderByDescending(d=>d.Order)
                .ToList();
        }

        public static void Save(PackingSpeedClass packingSpeedClass)
        {
            Db.FreeSql.InsertOrUpdate<PackingSpeedClass>()
                .SetSource(packingSpeedClass)
                .ExecuteAffrows();
        }

        public static void Remove(long id)
        {
            Db.FreeSql.Delete<PackingSpeedClass>(id);
        }

        public static PackingSpeedClass GetSpeedClass(long id)
        {
            return Db.FreeSql.Select<PackingSpeedClass>(id)
                .First();
        }
    }
}
