using System;
using System.Collections.Generic;
using System.IO;
using System.Windows.Forms;
using DataAccess.Dto;
using DataAccess.Encasement;
using DevExpress.Utils;
using DevExpress.XtraPrinting;
using Packaging.Common;
using Packaging.Settings;
using Packaging.Utility;

namespace Packaging.Encasement
{
    [ModuleDefine(moduleId: Constants.ReportFormId)]
    public partial class ReportForm : DevExpress.XtraEditors.XtraForm
    {
        public ReportForm()
        {
            InitializeComponent();
        }

        private void btnSerialSearch_Click(object sender, System.EventArgs e)
        {
            SetSerialDataSource();
        }

        private void btnSerialReport_Click(object sender, System.EventArgs e)
        {
            var reportSerialDtos = GetSerialDataSource();
            if (reportSerialDtos==null|| reportSerialDtos.Count == 0)
            {
                CustomMessageBox.Warning("请选择日期范围进行搜索后导出");
                return;
            }

            var dialogResult = xtraFolderBrowserDialog1.ShowDialog();
            if (dialogResult != DialogResult.OK)
            {
                return;
            }

            var fileName = Path.Combine(xtraFolderBrowserDialog1.SelectedPath,
                $"ReportSerial_{DateTime.Now:yyyyMMdd_HHmmss}.xlsx");
            if (File.Exists(fileName)
                && CustomMessageBox.Confirm("该文件名已存在，是否覆盖？") != DialogResult.Yes)
            {
                return;
            }

            try
            {
                gvSerial.ExportToXlsx(fileName, new XlsxExportOptionsEx
                {
                    TextExportMode = TextExportMode.Text,
                    AllowGrouping = DefaultBoolean.True
                });
                CustomMessageBox.Information("数据导出成功！");
            }
            catch (Exception ex)
            {
                CustomMessageBox.Warning(ex.Message.Contains("正由另一进程使用")
                    ? "数据导出失败！文件正由另一个程序占用！"
                    : ex.Message);
            }
        }

        private void SetSerialDataSource()
        {
            if (!dxVpSerial.Validate())
            {
                return;
            }

            var reportSerialDtos = PackingSerialDal.GetReportSerials(deSerialBegin.DateTime, deSerialEnd.DateTime);
            gcSerial.DataSource = reportSerialDtos;
            gcSerial.RefreshDataSource();
        }

        private List<ReportSerialDto> GetSerialDataSource()
        {
            if (gcSerial.DataSource == null)
            {
                return null;
            }
            if (!(gcSerial.DataSource is List<ReportSerialDto> reportSerialDtos))
            {
                throw new Exception("Grid绑定的数据类型错误！");
            }

            return reportSerialDtos;
        }

        private void btnBatchSearch_Click(object sender, EventArgs e)
        {
            SetBatchDataSource();
        }

        private void btnBatchReport_Click(object sender, EventArgs e)
        {
            var reportBatchDtos = GetBatchDataSource();
            if (reportBatchDtos == null || reportBatchDtos.Count == 0)
            {
                CustomMessageBox.Warning("请选择日期范围进行搜索后导出");
                return;
            }

            var dialogResult = xtraFolderBrowserDialog1.ShowDialog();
            if (dialogResult != DialogResult.OK)
            {
                return;
            }

            var fileName = Path.Combine(xtraFolderBrowserDialog1.SelectedPath,
                $"ReportBatch_{DateTime.Now:yyyyMMdd_HHmmss}.xlsx");
            if (File.Exists(fileName)
                && CustomMessageBox.Confirm("该文件名已存在，是否覆盖？") != DialogResult.Yes)
            {
                return;
            }

            try
            {
                gvBatch.ExportToXlsx(fileName, new XlsxExportOptionsEx
                {
                    TextExportMode = TextExportMode.Text,
                    AllowGrouping = DefaultBoolean.True
                });
                CustomMessageBox.Information("数据导出成功！");
            }
            catch (Exception ex)
            {
                CustomMessageBox.Warning(ex.Message.Contains("正由另一进程使用")
                    ? "数据导出失败！文件正由另一个程序占用！"
                    : ex.Message);
            }
        }

        private void SetBatchDataSource()
        {
            if (!dxVpBatch.Validate())
            {
                return;
            }

            var reportSerialDtos = PackingBatchDal.GetReportBatchDtos(deBatchBegin.DateTime,deBatchEnd.DateTime);
            gcBatch.DataSource = reportSerialDtos;
            gcBatch.RefreshDataSource();
        }

        private List<ReportBatchDto> GetBatchDataSource()
        {
            if (gcBatch.DataSource == null)
            {
                return null;
            }
            if (!(gcBatch.DataSource is List<ReportBatchDto> reportBatchDtos))
            {
                throw new Exception("Grid绑定的数据类型错误！");
            }

            return reportBatchDtos;
        }
    }
}