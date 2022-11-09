using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Threading.Tasks;
using DataAccess.Dto;
using DataAccess.Encasement;
using DataAccess.Wms;
using Packaging.Common;
using Packaging.Utility;

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
            BatchPrintDtoList = batchPrintDtoList;
            objectDataSource1.DataSource = batchPrintDtoList;
        }
    }
}
