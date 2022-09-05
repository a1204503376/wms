using System;
using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class ReceiveDetailLpnDal
    {
        public static void Save(string boxNumber, List<ReceiveDetailLpn> receiveDetailLpns)
        {
            Db.FreeSql.Transaction(() =>
            {
                Db.FreeSql.Delete<ReceiveDetailLpn>()
                    .Where(d => d.BoxCode == boxNumber)
                    .ExecuteAffrows();

                var flag = Db.FreeSql.Insert<ReceiveDetailLpn>(receiveDetailLpns)
                    .ExecuteAffrows() > 0;
                if (!flag)
                {
                    throw new Exception("入库[receive_detail_lpn]保存失败");
                }
            });
        }

        public static void Save(string boxNumber, ReceiveDetailLpn receiveDetailLpn)
        {
            Save(boxNumber, new List<ReceiveDetailLpn>
            {
                receiveDetailLpn
            });
        }
    }
}