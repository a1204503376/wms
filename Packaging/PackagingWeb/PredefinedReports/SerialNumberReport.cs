using System.Collections.Generic;
using System.IO;
using DataAccess.Dto;
using DataAccess.Encasement;

namespace PackagingWeb.PredefinedReports
{
    public partial class SerialNumberReport : DevExpress.XtraReports.UI.XtraReport
    {

        public SerialNumberReport()
        {
            InitializeComponent();
        }

        public SerialNumberReport(IReadOnlyCollection<SerialNumberPrintDto> serialNumberPrintDtoList) : this()
        {
            var reportItem = PackingReportItemDal.GetByName("SerialNumberReport");
            if (reportItem != null)
            {
                using MemoryStream ms = new MemoryStream(reportItem.LayoutData);
                LoadLayout(ms);
            }

            this.objectDataSource1.DataSource = serialNumberPrintDtoList;
        }
    }
}
