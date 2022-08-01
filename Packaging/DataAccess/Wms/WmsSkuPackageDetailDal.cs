using System;
using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class WmsSkuPackageDetailDal
    {
        public static WmsSkuPackageDetail GetFirst(long wspId)
        {
            var wmsSkuPackageDetail = Db.FreeSql.Select<WmsSkuPackageDetail>()
                .Where(d => d.WspId == wspId && d.IsDeleted==0)
                .First();
            if (wmsSkuPackageDetail==null)
            {
                throw new Exception($"包装为空，ID：{wspId}");
            }

            return wmsSkuPackageDetail;
        }
    }
}