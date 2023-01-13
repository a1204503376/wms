using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Windows.Forms;
using DataAccess.Dto;
using DataAccess.Enitiies;
using DataAccess.Enums;
using DataAccess.Wms;
using DevExpress.XtraReports.UI;
using Packaging.Common;
using Packaging.Encasement.Reports;
using Packaging.Settings;
using Packaging.Utility;

namespace Packaging.Encasement
{
    [ModuleDefine(moduleId: Constants.BatchFormId)]
    public partial class BatchPackingForm : DevExpress.XtraEditors.XtraForm
    {
        private string _againPrintDate;
        private bool _againPrintFlag;
        private string _boxNumber;
        private BindingList<SkuDetailDto> _skuDetails;
        private List<Sku> _skus;

        private readonly Dictionary<string, List<string>> _skuCodeSkuSpecListDict =
            new Dictionary<string, List<string>>();

        public BatchPackingForm()
        {
            InitializeComponent();
        }

        private void BatchPackingForm_Load(object sender, System.EventArgs e)
        {
            SetSluSkuDataSource();
            SetLuModelDataSource(_skus.Select(d => d.SkuSpec)
                .Where(d => !string.IsNullOrWhiteSpace(d))
                .Distinct().ToList());
            InitializeSkuDetails();
            txtPrintNumber.EditValue = ConfigurationManager.AppSettings["Copies"];
        }

        private void SetSluSkuDataSource()
        {
            _skus = WmsSkuDal.GetAll();
            sluSku.DisplayMember = "SkuName";
            sluSku.KeyMember = "SkuId";
            SetFilterSkuDataSource();
            SetSkuSpecEmptyDataSource();
        }

        private void SetSkuSpecEmptyDataSource()
        {
            var dictKeys = new Dictionary<string, string>();
            var skuSpecEmptyList = _skus.Where(d => string.IsNullOrWhiteSpace(d.SkuSpec)).ToList();
            foreach (var sku in skuSpecEmptyList)
            {
                if (!ConfigurationManager.AppSettings.AllKeys.Contains(sku.SkuCode))
                {
                    continue;
                }

                dictKeys.Add(ConfigurationManager.AppSettings[sku.SkuCode], sku.SkuCode);
            }

            var skuSpecDict = BladeDictDal.GetSkuSpecList(dictKeys.Keys.ToList());

            foreach (var keyValuePair in dictKeys.Where(keyValuePair => skuSpecDict.ContainsKey(keyValuePair.Key)))
            {
                _skuCodeSkuSpecListDict.Add(keyValuePair.Value, skuSpecDict[keyValuePair.Key]);
            }
        }

        private void SetLuModelDataSource(IReadOnlyCollection<string> skuSpecList)
        {
            var skuList = skuSpecList
                .Select(skuSpec =>
                    new Sku
                    {
                        SkuSpec = skuSpec
                    }).ToList();

            if (skuSpecList.Count > 1)
            {
                skuList.Insert(0, new Sku
                {
                    SkuSpec = string.Empty
                });
            }

            luModel.DisplayMember = "SkuSpec";
            luModel.ValueMember = "SkuSpec";
            luModel.DataSource = skuList;
        }

        private void InitializeSkuDetails()
        {
            _skuDetails = new BindingList<SkuDetailDto>()
            {
                AllowEdit = true,
                AllowNew = true,
                AllowRemove = true,
                RaiseListChangedEvents = true
            };
            gridControl1.DataSource = _skuDetails;
        }

        private BatchPrintDto GetBatchPrintDto()
        {
            var batchPrintDto = new BatchPrintDto
            {
                BoxType = cbxBox.Text,
                ProductionPlan = txtProductPlan.Text,
                PoCode = txtPo.Text,
                WoCode = txtWo.Text,
                SpecialCustomer = txtSpecialCustomer.Text,
                BoxNumber = Constants.DefaulutBoxNumber,
                Copies = Convert.ToInt16(txtPrintNumber.Text),
                UserName = GlobalSettings.UserName,
                SkuDetails = GetGridDataSource(),
                PrintDate = GetPrintDate(),
                AgainPrintFlag = _againPrintFlag
            };

            if (!string.IsNullOrWhiteSpace(_boxNumber) && !NomatcBoxType())
            {
                batchPrintDto.BoxNumber = _boxNumber;
            }

            var receiveDetailLpns = (from skuDetail in batchPrintDto.SkuDetails
                let sku = skuDetail.Sku
                let wmsSkuPackageDetail = WmsSkuPackageDetailDal.GetFirst(sku.WspId)
                select new ReceiveDetailLpn
                {
                    Id = SnowflakeIds.Instance.NextId(),
                    DetailStatus = ReceiveDetailStatusEnum.NotReceipt,
                    SkuId = sku.SkuId,
                    SkuCode = sku.SkuCode,
                    SkuName = sku.SkuName,
                    SkuSpec = string.IsNullOrWhiteSpace(sku.SkuSpec) ? skuDetail.SkuSpec : sku.SkuSpec,
                    WspId = sku.WspId,
                    PlanQty = skuDetail.PlanQty,
                    ScanQty = 0,
                    SkuLevel = 1,
                    ConvertQty = 1,
                    UmCode = wmsSkuPackageDetail.WsuCode,
                    UmName = wmsSkuPackageDetail.WsuName,
                    BaseUmCode = wmsSkuPackageDetail.WsuCode,
                    BaseUmName = wmsSkuPackageDetail.WsuName,
                    WhId = Constants.WhId,
                    WhCode = Constants.WhCode,
                    WoId = Constants.WoId,
                    OwnerCode = Constants.OwnerCode,
                    SkuLot1 = skuDetail.SkuLot1,
                    SkuLot2 = string.IsNullOrWhiteSpace(sku.SkuSpec) ? skuDetail.SkuSpec : sku.SkuSpec,
                    SkuLot4 = batchPrintDto.SpecialCustomer,
                    SkuLot9 = batchPrintDto.PrintDate,
                    Udf1 = txtProductPlan.Text,
                    Udf2 = txtPo.Text,
                    Udf3 = txtWo.Text,
                    CreateUser = GlobalSettings.UserId,
                    CreateDept = GlobalSettings.UserDeptId,
                }).ToList();

            batchPrintDto.ReceiveDetailLpns = receiveDetailLpns;

            batchPrintDto.SetQty();

            return batchPrintDto;
        }

        private string GetPrintDate()
        {
            if (_againPrintFlag && !string.IsNullOrWhiteSpace(_againPrintDate))
            {
                return _againPrintDate;
            }
            else
            {
                return _printDateTime.HasValue
                    ? _printDateTime.Value.ToString("yyMMdd")
                    : DateTime.Now.ToString("yyMMdd");
            }
        }

        private void gridView1_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode != Keys.Delete || e.Modifiers != Keys.Control)
            {
                return;
            }

            var dialogResult = CustomMessageBox.Confirm("确认删除当前行？");
            if (dialogResult != DialogResult.Yes)
            {
                return;
            }

            gridView1.DeleteRow(gridView1.FocusedRowHandle);

            if (_skuDetails.Count == 0
                || _skuDetails.All(d => string.IsNullOrWhiteSpace(d.SkuSpec)))
            {
                sluSku.DataSource = _skus;
            }
        }

        private void gridView1_ValidateRow(object sender, DevExpress.XtraGrid.Views.Base.ValidateRowEventArgs e)
        {
            if (!(gridView1.GetRowCellValue(e.RowHandle, colSku) is Sku sku))
            {
                e.Valid = false;
                e.ErrorText = "请选择物品";
                return;
            }

            var skuSpec = gridView1.GetRowCellValue(e.RowHandle, colSkuSpec);
            if (skuSpec == null || string.IsNullOrWhiteSpace(skuSpec.ToString()))
            {
                e.Valid = false;
                e.ErrorText = "请选择型号";
                return;
            }

            var skuLot1 = gridView1.GetRowCellValue(e.RowHandle, colSkuLot1);
            if (skuLot1 == null || string.IsNullOrWhiteSpace(skuLot1.ToString()))
            {
                e.Valid = false;
                e.ErrorText = "批次不允许为空";
                return;
            }

            var planQty = gridView1.GetRowCellValue(e.RowHandle, colPlanQty);
            if (planQty == null
                || !decimal.TryParse(planQty.ToString(), out decimal qty)
                || qty <= 0)
            {
                e.Valid = false;
                e.ErrorText = "请输入大于0的数量";
                return;
            }

            ResetPrintEnable();
        }

        private void btnResetAll_Click(object sender, EventArgs e)
        {
            var dialogResult = CustomMessageBox.Confirm("确定清空所有？");
            if (dialogResult == DialogResult.No)
            {
                return;
            }

            cbxBox.SelectedIndex = 0;
            txtSpecialCustomer.EditValue = null;
            txtPrintNumber.EditValue = 2;
            txtProductPlan.EditValue = null;
            txtPo.EditValue = null;
            txtWo.EditValue = null;

            SetFilterSkuDataSource();
            ResetReprint();
            ResetDetail();
        }

        private void ResetReprint()
        {
            btnReprint.Text = @"箱标重打";
            _boxNumber = string.Empty;
            _againPrintFlag = false;
            _againPrintDate = string.Empty;
        }

        private XtraReport GetBatchPackingReport(bool paperFlag = false)
        {
            var batchPrintDto = GetBatchPrintDto();
            if (cbxBox.Text == @"B" && batchPrintDto.SkuDetails.GroupBy(d => d.Sku.SkuId).Count() > 1)
            {
                CustomMessageBox.Warning("B箱不允许装不同物品，请检查！");
                return null;
            }

            if (batchPrintDto.SkuDetails.Select(d => d.SkuSpec).Distinct().Count() != 1)
            {
                CustomMessageBox.Warning("只允许同一个型号进行装箱，请检查物品的型号是否一致！");
                return null;
            }

            XtraReport xtraReport;
            if (paperFlag)
            {
                xtraReport = new BatchPaperReport(batchPrintDto);
            }
            else
            {
                xtraReport = new BatchPackingReport(batchPrintDto);
            }

            return xtraReport;
        }

        private void cbxBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            SetFilterSkuDataSource();
            if (NomatcBoxType())
            {
                CustomMessageBox.Information($"当前选择的箱型[{cbxBox.Text}]与重打的箱号[{_boxNumber}]不匹配，系统将生成新的箱号!");
            }

            // 切换箱型之后，重置明细
            ResetDetail();
        }

        private bool NomatcBoxType()
        {
            if (string.IsNullOrWhiteSpace(_boxNumber))
            {
                return false;
            }

            var boxType = _boxNumber.Substring(0, 1);
            return cbxBox.Text != boxType;
        }

        /// <summary>
        /// 据箱型对应的箱号过滤物品列表
        /// </summary>
        /// <returns></returns>
        private void SetFilterSkuDataSource()
        {
            List<Sku> dataSource;
            if (new List<string> { "A", "B", "C" }.Contains(cbxBox.Text))
            {
                dataSource = _skus.Where(d => d.Udf1 == cbxBox.Text).ToList();
            }
            else
            {
                dataSource = _skus.Where(d => d.Udf1 == cbxBox.Text
                                              || _skuCodeSkuSpecListDict.ContainsKey(d.SkuCode)).ToList();
            }

            sluSku.DataSource = dataSource;
        }

        public void SetReprintDataSource(PackingBatchHeader packingBatchHeader,
            List<PackingBatchDetail> packingBatchDetailList)
        {
            cbxBox.EditValue = packingBatchHeader.BoxType;
            txtSpecialCustomer.EditValue = packingBatchHeader.SpecialCustomer;
            txtProductPlan.EditValue = packingBatchHeader.ProductionPlan;
            txtPo.EditValue = packingBatchHeader.PoCode;
            txtWo.EditValue = packingBatchHeader.WoCode;

            ResetGridDataSource(() =>
            {
                foreach (var batchDetail in packingBatchDetailList)
                {
                    _skuDetails.Add(new SkuDetailDto()
                    {
                        Sku = _skus.FirstOrDefault(d => d.SkuId == batchDetail.SkuId),
                        SkuCode = batchDetail.SkuCode,
                        SkuSpec = batchDetail.Model,
                        PlanQty = batchDetail.Qty,
                        SkuLot1 = batchDetail.SkuLot1,
                        TrackingNumber = batchDetail.TrackingNumber
                    });
                }
            });
            _boxNumber = packingBatchHeader.BoxNumber;
            btnReprint.Text = $@"箱标重打({packingBatchHeader.BoxNumber})";
            _againPrintFlag = true;
            _againPrintDate = packingBatchHeader.PrintDate;
            ResetPrintEnable();
        }

        private void ResetPrintEnable()
        {
            btnPrint.Enabled = true;
            btnDatePrint.Enabled = true;
        }

        private void btnReprint_Click(object sender, EventArgs e)
        {
            var cartonLabelForm = new CartonLabelForm("Batch", this);
            cartonLabelForm.ShowDialog();
        }

        private void btnExport_Click(object sender, EventArgs e)
        {
            var skuDetails = GetGridDataSource();
            if (skuDetails.Count == 0)
            {
                CustomMessageBox.Warning("没有序列号数据可以导出");
                return;
            }

            var dialogResult = xtraFolderBrowserDialog1.ShowDialog();
            if (dialogResult != DialogResult.OK)
            {
                return;
            }

            var skuDetail = skuDetails.First();
            var curSku = skuDetail.Sku;
            var fileName = Path.Combine(xtraFolderBrowserDialog1.SelectedPath,
                $"{curSku.SkuSpec}_{curSku.SkuNameS}_{DateTime.Now:yyyy-MM-dd}_{GlobalSettings.UserName}.xlsx");
            if (File.Exists(fileName)
                && CustomMessageBox.Confirm("该文件名已存在，是否覆盖？") != DialogResult.Yes)
            {
                return;
            }

            try
            {
                // gridView1.ExportToXlsx(fileName, new XlsxExportOptions
                // {
                //     TextExportMode = TextExportMode.Value,
                // });
                ExcelHelper.ExportBatchDetail(skuDetails, fileName);
                CustomMessageBox.Information("数据导出成功！");
            }
            catch (Exception ex)
            {
                CustomMessageBox.Warning(ex.Message.Contains("正由另一进程使用")
                    ? "数据导出失败！文件正由另一个程序占用！"
                    : ex.Message);
            }
        }

        private List<SkuDetailDto> GetGridDataSource()
        {
            if (!(gridControl1.DataSource is BindingList<SkuDetailDto> gridControl1DataSource))
            {
                throw new Exception("Grid绑定的数据类型错误！");
            }

            return gridControl1DataSource.ToList<SkuDetailDto>();
        }

        private void btnImportDetail_Click(object sender, EventArgs e)
        {
            xtraOpenFileDialog1.Filter = @"Excel File|*.xlsx;*.xls";
            xtraOpenFileDialog1.Title = @"导入批次装箱明细";
            xtraOpenFileDialog1.Multiselect = false;

            xtraOpenFileDialog1.ShowDialog();
        }

        private void btnResetSerialNumber_Click(object sender, EventArgs e)
        {
            var dialogResult = CustomMessageBox.Confirm("确定清空所有明细？");
            if (dialogResult == DialogResult.No)
            {
                return;
            }

            ResetDetail();
        }

        private void ResetDetail()
        {
            ResetGridDataSource(null);
            btnPrint.Enabled = false;
            btnDatePrint.Enabled = false;
        }

        private void ResetGridDataSource(Action action)
        {
            _skuDetails.Clear();
            action?.Invoke();
            gridView1.RefreshData();
        }

        private void xtraOpenFileDialog1_FileOk(object sender, CancelEventArgs e)
        {
            var filePath = xtraOpenFileDialog1.FileName;
            var skuDetails = ExcelHelper.ImportBatchDetail(filePath);
            foreach (var skuDetail in skuDetails)
            {
                skuDetail.Sku = _skus.FirstOrDefault(d => d.SkuCode == skuDetail.SkuCode);
            }

            ResetGridDataSource(() =>
            {
                foreach (var skuDetail in skuDetails)
                {
                    _skuDetails.Add(skuDetail);
                }
            });
            ResetPrintEnable();
        }

        private void sluSku_EditValueChanged(object sender, EventArgs e)
        {
            // if (!(sender is SearchLookUpEdit searchLookUpEdit))
            // {
            //     return;
            // }
            //
            // if (!(searchLookUpEdit.EditValue is Sku sku))
            // {
            //     return;
            // }
            //
            // gridView1.SetRowCellValue(gridView1.FocusedRowHandle, colSkuCode, sku.SkuCode);
            // var gridDataSource = GetGridDataSource();
            // var skuSpec = sku.SkuSpec;
            // if (string.IsNullOrWhiteSpace(sku.SkuSpec) && gridDataSource.Count > 1)
            // {
            //     skuSpec = gridDataSource.First().SkuSpec;
            // }
            //
            // if (_skuCodeSkuSpecListDict.ContainsKey(sku.SkuCode))
            // {
            //     SetLuModelDataSource(_skuCodeSkuSpecListDict[sku.SkuCode]);
            // }
            //
            // gridView1.SetRowCellValue(gridView1.FocusedRowHandle, colSkuSpec, skuSpec);
        }

        private void gridView1_CellValueChanged(object sender,
            DevExpress.XtraGrid.Views.Base.CellValueChangedEventArgs e)
        {
            if (e.Column != colSku)
            {
                return;
            }

            var sku = gridView1.GetRowCellValue(e.RowHandle, e.Column) as Sku;
            if (sku == null)
            {
                return;
            }

            var gridDataSource = GetGridDataSource();
            var skuSpec = sku.SkuSpec;
            if (string.IsNullOrWhiteSpace(sku.SkuSpec) && gridDataSource.Count > 1)
            {
                skuSpec = gridDataSource.First().SkuSpec;
            }

            if (_skuCodeSkuSpecListDict.ContainsKey(sku.SkuCode))
            {
                SetLuModelDataSource(_skuCodeSkuSpecListDict[sku.SkuCode]);
            }

            gridView1.SetRowCellValue(e.RowHandle, colSkuCode, sku.SkuCode);
            gridView1.SetRowCellValue(e.RowHandle, colSkuSpec, skuSpec);
        }

        private void btnDatePrint_Click(object sender, EventArgs e)
        {
            var datePrintForm = new DatePrintForm("Batch", this);
            datePrintForm.ShowDialog();
        }

        private void btnPrint_Click(object sender, EventArgs e)
        {
            Print(null);
        }

        private DateTime? _printDateTime;

        public void Print(DateTime? printDateTime)
        {
            _printDateTime = printDateTime;
            var batchPackingReport = GetBatchPackingReport();
            if (batchPackingReport == null)
            {
                return;
            }

            ReportPrintTool reportPrintTool = new ReportPrintTool(batchPackingReport);
            reportPrintTool.PrintingSystem.AddCommandHandler(new BatchCommandHandler(batchPackingReport,
                reportPrintTool,printDateTime));
            reportPrintTool.ShowPreviewDialog();
            _printDateTime = null;
        }
    }
}