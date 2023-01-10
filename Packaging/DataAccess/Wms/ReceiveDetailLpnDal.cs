using System;
using System.Collections.Generic;
using DataAccess.Dto;
using DataAccess.Encasement;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class ReceiveDetailLpnDal
    {
        public static void SaveSerial(SerialNumberPrintDto serialNumberPrintDto)
        {
            Db.FreeSql.Transaction(() =>
            {
                Save(serialNumberPrintDto.BoxNumber,serialNumberPrintDto.ReceiveDetailLpns);
                PackingSerialDal.SaveSerialData(serialNumberPrintDto);
            });
        }

        public static void SaveBatch(BatchPrintDto batchPrintDto)
        {
            Db.FreeSql.Transaction(() =>
            {
                Save(batchPrintDto.BoxNumber, batchPrintDto.ReceiveDetailLpns);
                PackingBatchDal.SaveBatchData(batchPrintDto);
            });
        }

        private static void Save(string boxNumber, List<ReceiveDetailLpn> receiveDetailLpns)
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
        }

        public static bool ValidScanQtyGeZero(string boxNumber)
        {
            return Db.FreeSql.Select<ReceiveDetailLpn>()
                .Where(d => d.BoxCode == boxNumber
                            && d.ScanQty > 0)
                .Any();
        }
    }
}