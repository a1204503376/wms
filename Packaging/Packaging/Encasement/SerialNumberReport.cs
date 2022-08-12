using System;
using System.Collections.Generic;
using System.Configuration;
using System.Drawing.Printing;
using System.IO;
using System.Linq;
using DataAccess.Dto;
using DataAccess.Encasement;
using DataAccess.Enitiies;
using DataAccess.Wms;
using Newtonsoft.Json;
using Packaging.Common;
using Packaging.Utility;

namespace Packaging.Encasement
{
    public partial class SerialNumberReport : DevExpress.XtraReports.UI.XtraReport
    {
        private readonly List<SerialNumberPrintDto> _serialNumberPrintDtoList;
        private bool _isSave = false;

        private SerialNumberReport()
        {
            InitializeComponent();
        }

        public SerialNumberReport(SerialNumberPrintDto serialNumberPrintDto) 
            : this(new List<SerialNumberPrintDto>{serialNumberPrintDto})
        {

        }

        private SerialNumberReport(List<SerialNumberPrintDto> serialNumberPrintDtoList) : this()
        {
            var packingReportItem = PackingReportItemDal.GetByName("SerialNumberReport");
            if (packingReportItem != null)
            {
                using MemoryStream ms = new MemoryStream(packingReportItem.LayoutData);
                this.LoadLayout(ms);
            }

            _serialNumberPrintDtoList = serialNumberPrintDtoList;
            this.objectDataSource1.DataSource = _serialNumberPrintDtoList;
            PrintingSystem.StartPrint += PrintingSystem_StartPrint;
        }

        private void PrintingSystem_StartPrint(object sender, DevExpress.XtraPrinting.PrintDocumentEventArgs e)
        {
            e.PrintDocument.PrinterSettings.Collate = true;
            e.PrintDocument.PrinterSettings.Copies = _serialNumberPrintDtoList.First().Copies;
            e.PrintDocument.BeginPrint += PrintDocument_BeginPrint;
        }

        private void PrintDocument_BeginPrint(object sender, System.Drawing.Printing.PrintEventArgs e)
        {
            try
            {
                SaveData();
            }
            catch (Exception ex)
            {
                e.Cancel = true;
                Serilog.Log.Fatal("序列号打印前入库保存异常:{}", ex);
                CustomMessageBox.Exception($"序列号打印前入库保存异常:{ex.GetOriginalException().Message}");
            }
        }

        private void SaveData()
        {
            if (_isSave)
            {
                return;
            }
            // 多个序列号合并成一条记录
            var receiveDetailLpns = _serialNumberPrintDtoList.First().ReceiveDetailLpns;
            var receiveDetailLpn = JsonConvert.DeserializeObject<ReceiveDetailLpn>(
                JsonConvert.SerializeObject(receiveDetailLpns.First()));
            receiveDetailLpn.SnCode = string.Join(",", receiveDetailLpns.Select(d => d.SnCode).ToList());
            receiveDetailLpn.PlanQty = receiveDetailLpns.Count;

            ReceiveDetailLpnDal.Save(receiveDetailLpn);
        }

        private void SerialNumberReport_BeforePrint(object sender, System.Drawing.Printing.PrintEventArgs e)
        {
            var serialNumberDto = _serialNumberPrintDtoList.First();
            if (serialNumberDto.BoxNumber != Constants.DefaulutBoxNumber)
            {
                return;
            }

            var boxNumber = WmsApiHelper.GetBoxNumber(serialNumberDto.BoxType);

            foreach (var item in _serialNumberPrintDtoList)
            {
                if (item.BoxNumber != Constants.DefaulutBoxNumber)
                {
                    continue;
                }

                item.BoxNumber = boxNumber;
            }

            serialNumberDto.ReceiveDetailLpns.ForEach(d => { d.BoxCode = boxNumber; });

            SavePrintPreView();
        }

        private void SavePrintPreView()
        {
            bool savePrintPreviewFlag = Convert.ToBoolean(ConfigurationManager.AppSettings["SavePrintPreviewFlag"]);
            if (!savePrintPreviewFlag)
            {
                return;
            }

            try
            {
                SaveData();
                _isSave = true;
            }
            catch (Exception ex)
            {
                Serilog.Log.Fatal("序列号打印前入库保存异常:{}", ex);
                CustomMessageBox.Exception($"序列号打印前入库保存异常:{ex.GetOriginalException().Message}");
            }
        }
    }
}
