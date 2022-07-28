using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using DataAccess.Dto;
using DataAccess.Encasement;
using DataAccess.Wms;
using Packaging.Common;
using Packaging.Utility;

namespace Packaging.Encasement
{
    public partial class BatchPackingReport : DevExpress.XtraReports.UI.XtraReport
    {
        private readonly IReadOnlyCollection<BatchPrintDto> _batchPrintDtoList;

        private BatchPackingReport()
        {
            InitializeComponent();
        }

        public BatchPackingReport(BatchPrintDto batchPrintDto) 
            : this(new List<BatchPrintDto>{batchPrintDto})
        {

        }

        private BatchPackingReport(IReadOnlyCollection<BatchPrintDto> batchPrintDtoList) : this()
        {
            var packingReportItem = PackingReportItemDal.GetByName("BatchPackingReport");
            if (packingReportItem!=null)
            {
                using MemoryStream ms = new MemoryStream(packingReportItem.LayoutData);
                this.LoadLayout(ms);
            }

            _batchPrintDtoList = batchPrintDtoList;
            this.objectDataSource1.DataSource = batchPrintDtoList;

            PrintingSystem.StartPrint += PrintingSystem_StartPrint;
        }

        private void PrintingSystem_StartPrint(object sender, DevExpress.XtraPrinting.PrintDocumentEventArgs e)
        {
            e.PrintDocument.PrinterSettings.Collate = true;
            e.PrintDocument.PrinterSettings.Copies = _batchPrintDtoList.First().Copies;
            e.PrintDocument.BeginPrint += PrintDocument_BeginPrint;
        }

        private void PrintDocument_BeginPrint(object sender, System.Drawing.Printing.PrintEventArgs e)
        {
            try
            {
                ReceiveDetailLpnDal.Save(_batchPrintDtoList.First().ReceiveDetailLpns);
            }
            catch (Exception ex)
            {
                e.Cancel = true;
                Serilog.Log.Fatal("序列号打印前入库保存异常：{}",ex);
                CustomMessageBox.Exception($"序列号打印前入库保存异常:{ex.GetOriginalException().Message}");
            }
        }

        private void SerialNumberReport_BeforePrint(object sender, System.Drawing.Printing.PrintEventArgs e)
        {
            var printDto = _batchPrintDtoList.First();
            if (printDto.BoxNumber != Constants.DefaulutBoxNumber)
            {
                return;
            }

            var boxNumber = WmsApiHelper.GetBoxNumber(printDto.BoxType);

            foreach (var item in _batchPrintDtoList)
            {
                if (item.BoxNumber != Constants.DefaulutBoxNumber)
                {
                    continue;
                }

                item.BoxNumber = boxNumber;
            }

            printDto.ReceiveDetailLpns.ForEach(d => { d.BoxCode = boxNumber; });
        }
    }
}
