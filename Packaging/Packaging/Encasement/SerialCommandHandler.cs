using System.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using DataAccess.Dto;
using DataAccess.Wms;
using DevExpress.XtraPrinting;
using DevExpress.XtraReports.UI;
using Packaging.Common;
using Packaging.Encasement.Reports;
using Packaging.Utility;
using PrintDialog = System.Windows.Forms.PrintDialog;

namespace Packaging.Encasement
{
    public class SerialCommandHandler : ICommandHandler
    {
        private readonly XtraReport _xtraReport;
        private readonly List<SerialNumberPrintDto> _serialNumberPrintDtos;
        private readonly ReportPrintTool _reportPrintTool;
        private readonly short _copies;
        private PrintDialog _printDialog;
        private readonly bool _paperFlag;
        private DateTime? _printDateTime;

        public SerialCommandHandler(XtraReport xtraReport, ReportPrintTool reportPrintTool,
            DateTime? printDateTime = null, bool paperFlag = false)
        {
            if (paperFlag)
            {
                if (xtraReport is SerialPaperReport serialPaperReport)
                {
                    _serialNumberPrintDtos = serialPaperReport.SerialNumberPrintDtoList;
                }
            }
            else
            {
                if (xtraReport is SerialNumberReport serialNumberReport)
                {
                    _serialNumberPrintDtos = serialNumberReport.SerialNumberPrintDtoList;
                }
            }

            _printDateTime = printDateTime;
            _xtraReport = xtraReport;
            _reportPrintTool = reportPrintTool;
            if (_serialNumberPrintDtos != null)
            {
                _copies = _serialNumberPrintDtos.First().Copies;
            }

            _paperFlag = paperFlag;
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

        public bool CanHandleCommand(PrintingSystemCommand command, IPrintControl printControl)
        {
            return command == PrintingSystemCommand.Print || command == PrintingSystemCommand.PrintDirect;
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

        private void PrintBeforeHandler()
        {
            var serialNumberPrintDto = _serialNumberPrintDtos.First();
            if (serialNumberPrintDto.BoxNumber != Constants.DefaulutBoxNumber)
            {
                // 如果是箱标重打，重新保存数据
                if (serialNumberPrintDto.AgainPrintFlag)
                {
                    var validScanQtyGeZero = ReceiveDetailLpnDal.ValidScanQtyGeZero(serialNumberPrintDto.BoxNumber);
                    if (validScanQtyGeZero)
                    {
                        CustomMessageBox.Warning($"该箱号【{serialNumberPrintDto.BoxNumber}】,已经进行过收货处理，不允许重打箱标！");
                        return;
                    }
                    serialNumberPrintDto.ReceiveDetailLpns.ForEach(d => { d.BoxCode = serialNumberPrintDto.BoxNumber; });
                    SaveData();
                }

                // 打开的预览页面没有关闭时，只生成一次箱码，只保存一次数据
                Print();
                return;
            }

            //只有用户正式点击预览打印页面的打印按钮时才生成箱码
            if (_paperFlag)
            {
                serialNumberPrintDto.BoxType = "Z";
            }

            string boxNumber;
            if (_printDateTime.HasValue)
            {
                boxNumber = WmsApiHelper.GetBoxNumber(serialNumberPrintDto.BoxType, serialNumberPrintDto.SkuName,
                    serialNumberPrintDto.Model, _printDateTime.Value);
            }
            else
            {
                boxNumber = WmsApiHelper.GetBoxNumber(serialNumberPrintDto.BoxType, serialNumberPrintDto.SkuName,
                    serialNumberPrintDto.Model);
            }

            foreach (var item in _serialNumberPrintDtos)
            {
                if (item.BoxNumber != Constants.DefaulutBoxNumber)
                {
                    continue;
                }

                item.BoxNumber = boxNumber;
            }

            serialNumberPrintDto.ReceiveDetailLpns.ForEach(d => { d.BoxCode = boxNumber; });

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
                var serialNumberPrintDto = _serialNumberPrintDtos.First();
                // 重新打印箱贴的业务场景
                // 1.生产车间重新打印
                // 2.库房内的重新打印（WMS走出库，再入库流程）
                ReceiveDetailLpnDal.SaveSerial(serialNumberPrintDto);
            }
            catch (Exception ex)
            {
                Serilog.Log.Fatal("打印前入库保存异常：{}", ex.GetOriginalException().Message);
                CustomMessageBox.Exception($"打印前入库保存异常:{ex.GetOriginalException().Message}");
            }
        }
    }
}