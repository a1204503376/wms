using System;
using DevExpress.XtraReports.UI;
using System.Collections.Generic;
using DataAccess.Dto;
using DataAccess.Wms;
using Packaging.Common;
using Packaging.Utility;

namespace Packaging.Encasement
{
    public partial class SerialNumberReport : DevExpress.XtraReports.UI.XtraReport
    {
        private readonly SerialNumberPrintDto _serialNumberPrintDto;

        private SerialNumberReport()
        {
            InitializeComponent();
        }

        public SerialNumberReport(SerialNumberPrintDto serialNumberPrintDto) : this()
        {
            _serialNumberPrintDto = serialNumberPrintDto;
            this.bindingSource1.DataSource = _serialNumberPrintDto;
            PrintingSystem.StartPrint += PrintingSystem_StartPrint;
        }

        private void PrintingSystem_StartPrint(object sender, DevExpress.XtraPrinting.PrintDocumentEventArgs e)
        {
            e.PrintDocument.PrinterSettings.Collate = true;
            e.PrintDocument.PrinterSettings.Copies = _serialNumberPrintDto.Copies;
            e.PrintDocument.BeginPrint += PrintDocument_BeginPrint;
        }

        private void PrintDocument_BeginPrint(object sender, System.Drawing.Printing.PrintEventArgs e)
        {
            try
            {
                ReceiveDetailLpnDal.Save(_serialNumberPrintDto.ReceiveDetailLpns);
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
            if (_serialNumberPrintDto == null)
            {
                return;
            }

            if (_serialNumberPrintDto.BoxNumber == Constants.DefaulutBoxNumber)
            {
                _serialNumberPrintDto.BoxNumber = WmsApiHelper.GetBoxNumber(_serialNumberPrintDto.BoxType);
                _serialNumberPrintDto.ReceiveDetailLpns.ForEach(d =>
                {
                    d.BoxCode = _serialNumberPrintDto.BoxNumber;
                });
            }

            var serialNumberRanges = _serialNumberPrintDto.SerialNumberRanges.ToArray();
            SetSerialNumberRange(serialNumberRanges, lbRangeKey1, lbRangeBegin1, lbRangeEnd1, 1);
            SetSerialNumberRange(serialNumberRanges, lbRangeKey2, lbRangeBegin2, lbRangeEnd2, 2);
            SetSerialNumberRange(serialNumberRanges, lbRangeKey3, lbRangeBegin3, lbRangeEnd3, 3);
            SetSerialNumberRange(serialNumberRanges, lbRangeKey4, lbRangeBegin4, lbRangeEnd4, 4);
            SetSerialNumberRange(serialNumberRanges, lbRangeKey5, lbRangeBegin5, lbRangeEnd5, 5);
            SetSerialNumberRange(serialNumberRanges, lbRangeKey6, lbRangeBegin6, lbRangeEnd6, 6);
        }

        private static void SetSerialNumberRange(IReadOnlyList<SerialNumberRange> serialNumberRanges,
            XRControl lbKey,
            XRControl lbBegin,
            XRControl lbEnd,
            int index)
        {
            if (serialNumberRanges.Count < index)
            {
                return;
            }
        
            var serialNumberRange = serialNumberRanges[index-1];
            lbKey.Text = serialNumberRange.Key;
            lbBegin.Text = serialNumberRange.Begin;
            lbEnd.Text = serialNumberRange.End;
        }
    }
}
