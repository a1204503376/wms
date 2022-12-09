using System.Collections.Generic;
using System.IO;
using DataAccess.Dto;
using DataAccess.Encasement;

namespace Packaging.Encasement.Reports
{
    public partial class BatchPackingReport : DevExpress.XtraReports.UI.XtraReport
    {
        public readonly List<BatchPrintDto> BatchPrintDtoList;

        public BatchPackingReport()
        {
            InitializeComponent();
        }

        public BatchPackingReport(BatchPrintDto batchPrintDto) 
            : this(new List<BatchPrintDto>{batchPrintDto})
        {

        }

        private BatchPackingReport(List<BatchPrintDto> batchPrintDtoList) : this()
        {
            var reportItem = PackingReportItemDal.GetByName("BatchPackingReport");
            if (reportItem!=null)
            {
                using MemoryStream ms = new MemoryStream(reportItem.LayoutData);
                LoadLayout(ms);
            }

            BatchPrintDtoList = batchPrintDtoList;
            objectDataSource1.DataSource = batchPrintDtoList;
        }
    }
}
