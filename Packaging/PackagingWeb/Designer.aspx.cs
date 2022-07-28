using System;
using PackagingWeb.PredefinedReports;

namespace PackagingWeb {
    public partial class Designer : System.Web.UI.Page {
        protected void Page_Load(object sender, EventArgs e) {
            ASPxReportDesigner1.OpenReport(new TestReport());
        }
    }
}