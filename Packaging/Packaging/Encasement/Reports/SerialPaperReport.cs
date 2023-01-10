using DataAccess.Dto;
using DataAccess.Encasement;
using System.Collections.Generic;
using System.IO;

namespace Packaging.Encasement.Reports
{
    public partial class SerialPaperReport : DevExpress.XtraReports.UI.XtraReport
    {
        public readonly List<SerialNumberPrintDto> SerialNumberPrintDtoList;

        public SerialPaperReport()
        {
            InitializeComponent();
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
