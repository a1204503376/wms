using System;
using System.Text;
using DataAccess.Common;
using DevExpress.CodeParser.Diagnostics;
using DevExpress.XtraReports.Web;
using PackagingWeb.Services;

namespace PackagingWeb {
    public class Global_asax : System.Web.HttpApplication {
        void Application_Start(object sender, EventArgs e)
        {
            System.Web.Routing.RouteTable.Routes.MapPageRoute("defaultRoute", "", "~/Default.aspx");
            DevExpress.XtraReports.Configuration.Settings.Default.UserDesignerOptions.DataBindingMode = DevExpress.XtraReports.UI.DataBindingMode.Expressions;
            DevExpress.XtraReports.Web.WebDocumentViewer.Native.WebDocumentViewerBootstrapper.SessionState = System.Web.SessionState.SessionStateBehavior.Default;
            DevExpress.XtraReports.Web.QueryBuilder.Native.QueryBuilderBootstrapper.SessionState = System.Web.SessionState.SessionStateBehavior.Default;
            DevExpress.XtraReports.Web.ReportDesigner.Native.ReportDesignerBootstrapper.SessionState = System.Web.SessionState.SessionStateBehavior.Default;
            // DevExpress.XtraReports.Web.Extensions.ReportStorageWebExtension.RegisterExtensionGlobal(new CustomReportStorageWebExtension(Server.MapPath("/Reports")));
            DevExpress.XtraReports.Web.Extensions.ReportStorageWebExtension.RegisterExtensionGlobal(new DbReportStorageWebExtension());

            System.Net.ServicePointManager.SecurityProtocol |= System.Net.SecurityProtocolType.Tls12;
            ASPxReportDesigner.StaticInitialize();

            DevExpress.XtraReports.Web.ClientControls.LoggerService.Initialize(
                (ex, message) =>
                {
                    // logger.Fatal(ex, $"[{DateTime.Now}]: Exception occurred. Message: '{message}'. ");
                });

            DevExpress.Web.ASPxWebControl.CallbackError += new EventHandler(Application_Error);

        }

        void Application_End(object sender, EventArgs e) {
            // Code that runs on application shutdown
        }
    
        void Application_Error(object sender, EventArgs e) {
            // Code that runs when an unhandled error occurs
        }
    
        void Session_Start(object sender, EventArgs e) {
            // Code that runs when a new session is started
        }
    
        void Session_End(object sender, EventArgs e) {
            // Code that runs when a session ends. 
            // Note: The Session_End event is raised only when the sessionstate mode
            // is set to InProc in the Web.config file. If session mode is set to StateServer 
            // or SQLServer, the event is not raised.
        }
    }
}