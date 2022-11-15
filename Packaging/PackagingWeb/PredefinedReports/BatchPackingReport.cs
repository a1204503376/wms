using System.Collections.Generic;
using System.IO;
using DataAccess.Dto;
using DataAccess.Encasement;

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
            var reportItem = PackingReportItemDal.GetByName("BatchPackingReport");
            if (reportItem != null)
            {
                using MemoryStream ms = new MemoryStream(reportItem.LayoutData);
                LoadLayout(ms);
            }

            this.objectDataSource1.DataSource = batchPrintDtoList;
        }
    }
}
