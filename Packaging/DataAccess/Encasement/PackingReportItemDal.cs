using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Encasement
{
    public class PackingReportItemDal
    {
        public static List<PackingReportItem> GetItems()
        {
            return Db.FreeSql.Select<PackingReportItem>()
                .ToList();
        }

        public static PackingReportItem GetByName(string name)
        {
            return Db.FreeSql.Select<PackingReportItem>()
                .Where(d => d.Name == name)
                .First();
        }

        public static void Save(PackingReportItem reportData)
        {
            Db.FreeSql.InsertOrUpdate<PackingReportItem>()
                .SetSource(reportData)
                .ExecuteAffrows();
        }
    }
}