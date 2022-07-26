using System;
using DevExpress.XtraReports.UI;
using System.Collections.Generic;
using DataAccess.Dto;
using DataAccess.Wms;
using Packaging.Common;
using Packaging.Utility;

namespace Packaging.Encasement
{
    public partial class BatchPackingReport : DevExpress.XtraReports.UI.XtraReport
    {
        private readonly BatchPrintDto _batchPrintDto;

        private BatchPackingReport()
        {
            InitializeComponent();
        }

        public BatchPackingReport(BatchPrintDto serialNumberPrintDto) : this()
        {
            _batchPrintDto = serialNumberPrintDto;
            this.bindingSource1.DataSource = _batchPrintDto;
            PrintingSystem.StartPrint += PrintingSystem_StartPrint;
        }

        private void PrintingSystem_StartPrint(object sender, DevExpress.XtraPrinting.PrintDocumentEventArgs e)
        {
            e.PrintDocument.PrinterSettings.Collate = true;
            e.PrintDocument.PrinterSettings.Copies = _batchPrintDto.Copies;
            e.PrintDocument.BeginPrint += PrintDocument_BeginPrint;
        }

        private void PrintDocument_BeginPrint(object sender, System.Drawing.Printing.PrintEventArgs e)
        {
            try
            {
                ReceiveDetailLpnDal.Save(_batchPrintDto.ReceiveDetailLpns);
            }
            catch (Exception ex)
            {
                e.Cancel = true;
                Serilog.Log.Fatal("序列号打印前入库保存异常",ex);
                CustomMessageBox.Exception($"序列号打印前入库保存异常:{ex.GetOriginalException().Message}");
            }
        }

        private void SerialNumberReport_BeforePrint(object sender, System.Drawing.Printing.PrintEventArgs e)
        {
            if (_batchPrintDto == null)
            {
                return;
            }

            if (_batchPrintDto.BoxNumber == Constants.DefaulutBoxNumber)
            {
                _batchPrintDto.BoxNumber = WmsApiHelper.GetBoxNumber(_batchPrintDto.BoxType);
                _batchPrintDto.ReceiveDetailLpns.ForEach(d =>
                {
                    d.BoxCode = _batchPrintDto.BoxNumber;
                });
            }

            var skuDetails = _batchPrintDto.SkuDetails.ToArray();
            SetQty(skuDetails, lbQty1,  1);
            SetQty(skuDetails, lbQty2,  2);
            SetQty(skuDetails, lbQty3,  3);
            SetQty(skuDetails, lbQty4,  4);
        }

        private static void SetQty(IReadOnlyList<SkuDetail> skuDetails,
            XRControl lbQty,
            int index)
        {
            if (skuDetails.Count < index)
            {
                return;
            }

            var flag = skuDetails.Count > 1;
            var skuDetail = skuDetails[index-1];
            lbQty.Text = $@"{(flag?skuDetail.Sku.SkuName+" ":"")}{skuDetail.PlanQty} {skuDetail.Sku.WspName}/箱";
        }
    }
}
