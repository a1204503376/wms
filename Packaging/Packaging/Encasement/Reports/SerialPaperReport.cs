using DataAccess.Dto;
using DataAccess.Encasement;
using DevExpress.XtraReports.UI;
using System.Collections.Generic;
using System.IO;
using DevExpress.XtraPrinting.BarCode;

namespace Packaging.Encasement.Reports
{
    public partial class SerialPaperReport : DevExpress.XtraReports.UI.XtraReport
    {
        public readonly List<SerialNumberPrintDto> SerialNumberPrintDtoList;

        private SerialPaperReport()
        {
            InitializeComponent();
            // var qrCode = new XRBarCode
            // {
            //     Alignment = DevExpress.XtraPrinting.TextAlignment.MiddleCenter,
            //     TextAlignment = DevExpress.XtraPrinting.TextAlignment.MiddleCenter,
            //     AutoModule = true,
            //     Dpi = 254F,
            //     ShowText = false,
            //     LocationFloat = new DevExpress.Utils.PointFloat(660.7466F, 25.00001F),
            //     Padding = new DevExpress.XtraPrinting.PaddingInfo(10, 10, 0, 0, 96F),
            //     Module = 5.08F,
            //     SizeF = new System.Drawing.SizeF(314.2534F, 380F),
            //     Symbology = new QRCodeGenerator
            //     {
            //         CompactionMode = QRCodeCompactionMode.Byte,
            //         Version = QRCodeVersion.Version1
            //     }
            // };
            // qrCode.StylePriority.UseTextAlignment = false;
            // qrCode.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            //     new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[BoxNumberLabel]")});
            // this.xrTableCell6.Controls.Add(qrCode);
        }

        public SerialPaperReport(SerialNumberPrintDto serialNumberPrintDto)
            : this(new List<SerialNumberPrintDto> { serialNumberPrintDto })
        {
            
        }

        public SerialPaperReport(List<SerialNumberPrintDto> serialNumberPrintDtoList) : this()
        {
            var reportItem = PackingReportItemDal.GetByName("SerialPaperReport");
            if (reportItem != null)
            {
                using MemoryStream ms = new MemoryStream(reportItem.LayoutData);
                LoadLayout(ms);
            }

            SerialNumberPrintDtoList = serialNumberPrintDtoList;
            this.objectDataSource1.DataSource = SerialNumberPrintDtoList;
        }

    }
}
