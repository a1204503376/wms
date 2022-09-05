using System;
using System.ComponentModel;
using System.Data;
using DataAccess.Encasement;
using DataAccess.Enitiies;
using DevExpress.DataAccess.Excel;
using Packaging.Common;
using Packaging.Utility;

namespace Packaging.Encasement
{
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

            var worksheetSettings = new ExcelWorksheetSettings("Sheet1", "A2:L2000");
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
            if (_excelDataSource == null)
            {
                CustomMessageBox.Warning("请导入数据");
                return;
            }

            var dataTable = _excelDataSource.ToDataTable();
            foreach (var dataTableColumn in dataTable.Columns)
            {
                if (!(dataTableColumn is DataColumn tableColumn))
                {
                    continue;
                }

                if (!tableColumn.ColumnName.Contains("Column"))
                {
                    continue;
                }

                gridControl1.DataSource = null;
                gridView1.Columns.Clear();
                CustomMessageBox.Warning("导入的数据格式与模板不一致");
                return;
            }
        
            var packingAutoIdentifications = ModelConvertHelper<PackingSerialDetail>.ConvertToModel(dataTable);

            var flag = PackingSerialDal.SaveImportExcel(packingAutoIdentifications);
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