using System.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using DataAccess.Dto;
using DataAccess.Encasement;
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

        public SerialCommandHandler(XtraReport xtraReport, ReportPrintTool reportPrintTool, bool paperFlag = false)
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

            DialogResult dialogResult = DialogResult.None;
            switch (command)
            {
                case PrintingSystemCommand.Print:
                    dialogResult = CreatePrintDialog();
                    break;
                case PrintingSystemCommand.PrintDirect:
                    dialogResult = DialogResult.OK;
                    break;
            }

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
                    SaveSerialNumberData(serialNumberPrintDto);
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
            var boxNumber = WmsApiHelper.GetBoxNumber(serialNumberPrintDto.BoxType, serialNumberPrintDto.SkuName,
                serialNumberPrintDto.Model);

            foreach (var item in _serialNumberPrintDtos)
            {
                if (item.BoxNumber != Constants.DefaulutBoxNumber)
                {
                    continue;
                }

                item.BoxNumber = boxNumber;
            }

            serialNumberPrintDto.ReceiveDetailLpns.ForEach(d => { d.BoxCode = boxNumber; });

            try
            {
                SaveData();
            }
            catch (Exception ex)
            {
                Serilog.Log.Fatal("打印前入库保存异常：{}", ex);
                CustomMessageBox.Exception($"打印前入库保存异常:{ex.GetOriginalException().Message}");
            }

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
            var serialNumberPrintDto = _serialNumberPrintDtos.First();
            // 重新打印箱贴的业务场景
            // 1.生产车间重新打印
            // 2.库房内的重新打印（WMS走出库，再入库流程）
            ReceiveDetailLpnDal.Save(serialNumberPrintDto.BoxNumber, serialNumberPrintDto.ReceiveDetailLpns);

            SaveSerialNumberData(serialNumberPrintDto);
        }

        private static void SaveSerialNumberData(SerialNumberPrintDto serialNumberPrintDto)
        {
            Task.Run(() => { PackingSerialDal.SaveSerialData(serialNumberPrintDto); });
        }
    }
}