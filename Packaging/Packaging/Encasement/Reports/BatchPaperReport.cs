using DataAccess.Dto;
using DataAccess.Encasement;
using DevExpress.XtraReports.UI;
using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.IO;

namespace Packaging.Encasement.Reports
{
    public partial class BatchPaperReport : DevExpress.XtraReports.UI.XtraReport
    {
        public readonly List<BatchPrintDto> BatchPrintDtoList;

        public BatchPaperReport()
        {
            InitializeComponent();
        }

        public BatchPaperReport(BatchPrintDto batchPrintDto)
            : this(new List<BatchPrintDto> { batchPrintDto })
        {

        }

        private BatchPaperReport(List<BatchPrintDto> batchPrintDtoList) : this()
        {
            var reportItem = PackingReportItemDal.GetByName("BatchPaperReport");
            if (reportItem != null)
            {
                using MemoryStream ms = new MemoryStream(reportItem.LayoutData);
                LoadLayout(ms);
            }

            BatchPrintDtoList = batchPrintDtoList;
            objectDataSource1.DataSource = batchPrintDtoList;
        }
    }
}
