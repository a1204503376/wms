using System;

namespace PackagingWeb {    public partial class Viewer : System.Web.UI.Page {
        protected void Page_Load(object sender, EventArgs e) {
            ASPxWebDocumentViewer1.OpenReport("TestReport");
        }
    }
}