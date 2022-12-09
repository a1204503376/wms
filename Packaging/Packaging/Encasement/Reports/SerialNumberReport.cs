using System.Collections.Generic;
using System.IO;
using DataAccess.Dto;
using DataAccess.Encasement;

namespace Packaging.Encasement.Reports
{
    public partial class SerialNumberReport : DevExpress.XtraReports.UI.XtraReport
    {
        public readonly List<SerialNumberPrintDto> SerialNumberPrintDtoList;

        private SerialNumberReport()
        {
            InitializeComponent();
        }

        public SerialNumberReport(SerialNumberPrintDto serialNumberPrintDto) 
            : this(new List<SerialNumberPrintDto>{serialNumberPrintDto})
        {

        }

        public SerialNumberReport(List<SerialNumberPrintDto> serialNumberPrintDtoList) : this()
        {
            var reportItem = PackingReportItemDal.GetByName("SerialNumberReport");
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
