using System;
using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class ReceiveDetailLpnDal
    {
        public static void Save(List<ReceiveDetailLpn> receiveDetailLpns)
        {
            var flag = Db.FreeSql.Insert<ReceiveDetailLpn>(receiveDetailLpns)
                .ExecuteAffrows()>0;
            if (!flag)
            {
                throw new Exception("入库[receive_detail_lpn]保存失败");
            }
        }
    }
}