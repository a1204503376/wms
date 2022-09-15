using System.Collections.Generic;
using DataAccess.Dto;

namespace PackagingWeb.PredefinedReports
{
    public partial class BatchPackingReport : DevExpress.XtraReports.UI.XtraReport
    {
        public BatchPackingReport()
        {
            InitializeComponent();
        }

        public BatchPackingReport(IReadOnlyCollection<BatchPrintDto> batchPrintDtoList) : this()
        {
            this.objectDataSource1.DataSource = batchPrintDtoList;
        }
    }
}
