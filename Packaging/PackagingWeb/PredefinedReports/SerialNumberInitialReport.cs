using DataAccess.Dto;
using DevExpress.XtraReports.UI;
using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;

namespace PackagingWeb.PredefinedReports
{
    public partial class SerialNumberInitialReport : DevExpress.XtraReports.UI.XtraReport
    {
        public SerialNumberInitialReport()
        {
            InitializeComponent();
        }

        public SerialNumberInitialReport(IReadOnlyCollection<SerialNumberPrintDto> serialNumberPrintDtoList) : this()
        {
            this.objectDataSource1.DataSource = serialNumberPrintDtoList;
        }
    }
}
