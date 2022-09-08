using DevExpress.XtraReports.UI;
using System;
using System.Collections.Generic;

namespace PackagingWeb.PredefinedReports
{
    public static class ReportsFactory
    {
        static ReportsFactory ()
        {
            Reports.Add("BatchPackingReport", () => new BatchPackingReport());
            Reports.Add("SerialNumberReport", () => new SerialNumberReport());
        }
        public static readonly Dictionary<string, Func<XtraReport>> Reports = new Dictionary<string, Func<XtraReport>>();
    }
}