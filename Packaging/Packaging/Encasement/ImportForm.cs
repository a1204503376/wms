using DevExpress.XtraEditors;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using DataAccess.Encasement;
using DataAccess.Enitiies;
using DevExpress.DataAccess.Excel;
using Packaging.Common;
using Packaging.Settings;
using Packaging.Utility;

namespace Packaging.Encasement
{
    [ModuleDefine(moduleId: 104)]
    public partial class ImportForm : DevExpress.XtraEditors.XtraForm
    {
        private ExcelDataSource _excelDataSource;

        public ImportForm()
        {
            InitializeComponent();
        }

        private void simpleButton1_Click(object sender, EventArgs e)
        {
            openFileDialog1.Filter = @"Excel File|*.xlsx;*.xls";
            openFileDialog1.Title = @"Import Excel";
            openFileDialog1.Multiselect = false;

            openFileDialog1.ShowDialog();
        }

        private void openFileDialog1_FileOk(object sender, CancelEventArgs e)
        {
            var filePath = openFileDialog1.FileName;

            _excelDataSource = new ExcelDataSource
            {
                FileName = filePath
            };

            var worksheetSettings = new ExcelWorksheetSettings
            {
                WorksheetIndex = 0
            };
            var sourceOptions = new ExcelSourceOptions
            {
                ImportSettings = worksheetSettings,
                SkipHiddenRows = false,
                SkipHiddenColumns = false,
                UseFirstRowAsHeader = true
            };

            _excelDataSource.SourceOptions = sourceOptions;
            _excelDataSource.Fill();
            gridControl1.DataSource = _excelDataSource;
        }

        private void simpleButton2_Click(object sender, EventArgs e)
        {
            var dataTable = _excelDataSource.ToDataTable();
            var packingAutoIdentifications = ModelConvertHelper<PackingAutoIdentification>.ConvertToModel(dataTable);

            var flag = AutoIdentificationDal.SaveImportExcel(packingAutoIdentifications);
            if (!flag)
            {
                CustomMessageBox.Warning("保存失败");
            }
            else
            {
                CustomMessageBox.Information("保存成功");
            }
        }
    }
}