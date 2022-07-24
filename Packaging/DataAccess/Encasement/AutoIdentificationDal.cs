using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Encasement
{
    public class AutoIdentificationDal
    {
        public static bool SaveImportExcel(List<PackingAutoIdentification> packingAutoIdentifications)
        {
            return Db.FreeSql.Insert<PackingAutoIdentification>(packingAutoIdentifications)
                .ExecuteAffrows()>0;
        }

        public static PackingAutoIdentification GetBySerialNumberAndModel(string model,string serialNumber)
        {
            return Db.FreeSql.Select<PackingAutoIdentification>()
                .Where(d => d.Model == model && d.ProductSupportCode == serialNumber)
                .First();
        }
    }
}