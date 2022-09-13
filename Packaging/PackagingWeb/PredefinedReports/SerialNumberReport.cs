using System.Collections.Generic;
using DataAccess.Dto;

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
            this.objectDataSource1.DataSource = serialNumberPrintDtoList;
        }
    }
}
