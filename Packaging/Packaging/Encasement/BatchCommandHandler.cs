using System.Linq;
using DataAccess.Wms;
using DevExpress.XtraPrinting;
using DevExpress.XtraReports.UI;
using Packaging.Common;
using Packaging.Utility;
using System.Configuration;
using System;
using System.Collections.Generic;
using System.Windows.Forms;
using DataAccess.Dto;
using Packaging.Encasement.Reports;

namespace Packaging.Encasement
{
    public class BatchCommandHandler : ICommandHandler
    {
        private readonly XtraReport _xtraReport;
        private readonly List<BatchPrintDto> _batchPrintDtos;
        private readonly ReportPrintTool _reportPrintTool;
        private PrintDialog _printDialog;
        private readonly short _copies;
        private DateTime? _printDateTime;

        public BatchCommandHandler(XtraReport xtraReport, ReportPrintTool reportPrintTool, DateTime? printDateTime,
            bool paperFlag = false)
        {
            if (paperFlag)
            {
                if (xtraReport is BatchPaperReport batchPaperReport)
                {
                    _batchPrintDtos = batchPaperReport.BatchPrintDtoList;
                }
            }
            else
            {
                if (xtraReport is BatchPackingReport batchPackingReport)
                {
                    _batchPrintDtos = batchPackingReport.BatchPrintDtoList;
                }
            }

            _printDateTime = printDateTime;
            _xtraReport = xtraReport;
            _reportPrintTool = reportPrintTool;
            if (_batchPrintDtos != null)
            {
                _copies = _batchPrintDtos.First().Copies;
            }
        }


        public void HandleCommand(PrintingSystemCommand command, object[] args, IPrintControl printControl,
            ref bool handled)
        {
            if (!CanHandleCommand(command, printControl))
            {
                return;
            }

            DialogResult dialogResult = command switch
            {
                PrintingSystemCommand.Print => CreatePrintDialog(),
                PrintingSystemCommand.PrintDirect => DialogResult.OK,
                _ => DialogResult.None
            };

            if (dialogResult != DialogResult.OK)
            {
                handled = true;
                return;
            }

            _reportPrintTool.PrintingSystem.StartPrint += PrintingSystem_StartPrint;
            PrintBeforeHandler();

            // 防止调用默认打印过程
            handled = true;
        }

        private DialogResult CreatePrintDialog()
        {
            _printDialog = new PrintDialog();
            _printDialog.PrinterSettings.Copies = _copies;
            _printDialog.PrinterSettings.Collate = true;
            return _printDialog.ShowDialog();
        }

        private void PrintingSystem_StartPrint(object sender, PrintDocumentEventArgs e)
        {
            e.PrintDocument.PrinterSettings.Copies = _printDialog != null
                ? _printDialog.PrinterSettings.Copies
                : _copies;
        }

        public bool CanHandleCommand(PrintingSystemCommand command, IPrintControl printControl)
        {
            return command == PrintingSystemCommand.Print || command == PrintingSystemCommand.PrintDirect;
        }

        private void PrintBeforeHandler()
        {
            var batchPrintDto = _batchPrintDtos.First();
            if (batchPrintDto.BoxNumber != Constants.DefaulutBoxNumber)
            {
                // 如果是箱标重打，重新保存一份数据
                if (batchPrintDto.AgainPrintFlag)
                {
                    var validScanQtyGeZero = ReceiveDetailLpnDal.ValidScanQtyGeZero(batchPrintDto.BoxNumber);
                    if (validScanQtyGeZero)
                    {
                        CustomMessageBox.Warning($"该箱号【{batchPrintDto.BoxNumber}】,已经进行过收货处理，不允许重打箱标！");
                        return;
                    }
                    batchPrintDto.ReceiveDetailLpns.ForEach(d => { d.BoxCode = batchPrintDto.BoxNumber; });
                    SaveData();
                }

                // 打开的预览页面没有关闭时，只生成一次箱码，只保存一次数据
                Print();
                return;
            }

            //只有用户正式点击预览打印页面的打印按钮时才生成箱码
            var skuNameList = batchPrintDto.SkuDetails.Select(d => d.Sku.SkuName).Distinct().ToList();
            string boxNumber = _printDateTime.HasValue
                ? WmsApiHelper.GetBoxNumber(batchPrintDto.BoxType, skuNameList, batchPrintDto.Model,
                    _printDateTime.Value)
                : WmsApiHelper.GetBoxNumber(batchPrintDto.BoxType, skuNameList, batchPrintDto.Model);

            foreach (var item in _batchPrintDtos)
            {
                if (item.BoxNumber != Constants.DefaulutBoxNumber)
                {
                    continue;
                }

                item.BoxNumber = boxNumber;
            }

            batchPrintDto.ReceiveDetailLpns.ForEach(d => { d.BoxCode = boxNumber; });

            SaveData();

            _xtraReport.CreateDocument();
            Print();
        }

        private void Print()
        {
            bool savePrintPreviewFlag = Convert.ToBoolean(ConfigurationManager.AppSettings["SavePrintPreviewFlag"]);
            if (!savePrintPreviewFlag)
            {
                _reportPrintTool.Print();
            }
        }

        private void SaveData()
        {
            try
            {
                var batchPrintDto = _batchPrintDtos.First();
                ReceiveDetailLpnDal.SaveBatch(batchPrintDto);
            }
            catch (Exception ex)
            {
                Serilog.Log.Fatal("打印前入库保存异常：{}", ex);
                CustomMessageBox.Exception($"打印前入库保存异常:{ex.GetOriginalException().Message}");
            }
        }
    }
}