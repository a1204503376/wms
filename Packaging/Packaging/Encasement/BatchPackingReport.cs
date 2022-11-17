using System.Collections.Generic;
using System.IO;
using DataAccess.Dto;
using DataAccess.Encasement;

namespace Packaging.Encasement
{
    public partial class BatchPackingReport : DevExpress.XtraReports.UI.XtraReport
    {
        public readonly IReadOnlyCollection<BatchPrintDto> BatchPrintDtoList;

        private BatchPackingReport()
        {
            InitializeComponent();
        }

        public BatchPackingReport(BatchPrintDto batchPrintDto) 
            : this(new List<BatchPrintDto>{batchPrintDto})
        {

        }

        private BatchPackingReport(IReadOnlyCollection<BatchPrintDto> batchPrintDtoList) : this()
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
