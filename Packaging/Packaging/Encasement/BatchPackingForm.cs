using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Windows.Forms;
using DataAccess.Dto;
using DataAccess.Enitiies;
using DataAccess.Enums;
using DataAccess.Wms;
using DevExpress.Utils.Extensions;
using DevExpress.XtraEditors.DXErrorProvider;
using DevExpress.XtraGrid.Columns;
using DevExpress.XtraGrid.Views.Grid;
using DevExpress.XtraReports.UI;
using Packaging.Common;
using Packaging.Settings;
using Packaging.Utility;

namespace Packaging.Encasement
{
    [ModuleDefine(moduleId: 102)]
    public partial class BatchPackingForm : DevExpress.XtraEditors.XtraForm
    {
        private BindingList<SkuDetail> _skuDetails;
        private List<Sku> _sluSkuDataSource;
        private bool _flagSkuDataSource = false;

        public BatchPackingForm()
        {
            InitializeComponent();
        }

        private void BatchPackingForm_Load(object sender, System.EventArgs e)
        {
            _sluSkuDataSource = SkuDal.GetAll();
            sluSku.DisplayMember = "SkuName";
            sluSku.KeyMember = "SkuId";
            sluSku.DataSource = _sluSkuDataSource;
            InitializeSkuDetails();
        }

        private void InitializeSkuDetails()
        {
            _skuDetails = new BindingList<SkuDetail>()
            {
                AllowEdit = true,
                AllowNew = true,
                AllowRemove = true,
                RaiseListChangedEvents = true
            };
            gridControl1.DataSource = _skuDetails;
        }

        private void gridView1_CellValueChanged(object sender, DevExpress.XtraGrid.Views.Base.CellValueChangedEventArgs e)
        {
            if (e.Column.FieldName != "Sku")
            {
                return;
            }

            if (!(gridView1.GetFocusedRowCellValue(e.Column.FieldName) is Sku sku))
            {
                return;
            }

            gridView1.SetRowCellValue(e.RowHandle, gridView1.Columns["SkuCode"], sku.SkuName);
            gridView1.SetRowCellValue(e.RowHandle, "SkuSpec", sku.SkuSpec);
        }

        private void btnSavePrint_Click(object sender, System.EventArgs e)
        {
            if (_skuDetails.Count == 0)
            {
                CustomMessageBox.Warning("请输入物品信息");
                return;
            }

            BatchPrintDto batchPrintDto = GetBatchPrintDto();

            var batchPackingReport = new BatchPackingReport(batchPrintDto);
            batchPackingReport.Print();
        }

        private BatchPrintDto GetBatchPrintDto()
        {
            var batchPrintDto = new BatchPrintDto
            {
                BoxType = cbxBox.Text,
                SpecialCustomer = txtSpecialCustomer.Text,
                BoxNumber = Constants.DefaulutBoxNumber,
                Copies = Convert.ToInt16(txtPrintNumber.Text),
                PrintDate = DateTime.Now.ToString("yyMMdd"),
                UserName = GlobalSettings.UserName,
                SkuDetails = _skuDetails.ToList()
            };
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
                    SkuSpec = sku.SkuSpec,
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
                    SkuLot2 = batchPrintDto.Model,
                    SkuLot4 = batchPrintDto.SpecialCustomer,
                    Udf1 = txtProductPlan.Text,
                    Udf2 = txtPo.Text,
                    Udf3 = txtWo.Text,
                    CreateUser = GlobalSettings.UserId,
                    CreateDept = GlobalSettings.UserDeptId,
                }).ToList();

            batchPrintDto.ReceiveDetailLpns = receiveDetailLpns;
            return batchPrintDto;
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

            if (_skuDetails.Count==0
                || _skuDetails.All(d=>string.IsNullOrWhiteSpace(d.SkuSpec)))
            {
                sluSku.DataSource = _sluSkuDataSource;
            }
        }

        private void gridView1_ValidateRow(object sender, DevExpress.XtraGrid.Views.Base.ValidateRowEventArgs e)
        {
            var colSku = gridView1.Columns["Sku"];
            if (!(gridView1.GetRowCellValue(e.RowHandle, colSku) is Sku sku))
            {
                e.Valid = false;
                e.ErrorText = "请选择物品";
                return;
            }

            var colSkuLot1 = gridView1.Columns["SkuLot1"];
            var skuLot1 = gridView1.GetRowCellValue(e.RowHandle, colSkuLot1);
            if (skuLot1==null || string.IsNullOrWhiteSpace(skuLot1.ToString()))
            {
                e.Valid = false;
                e.ErrorText = "批次不允许为空";
                return;
            }

            var colPlanQty = gridView1.Columns["PlanQty"];
            var planQty = gridView1.GetRowCellValue(e.RowHandle, colPlanQty);
            if (planQty==null 
                || !decimal.TryParse(planQty.ToString(),out decimal qty)
                || qty<=0)
            {
                e.Valid = false;
                e.ErrorText = "请输入大于0的数量";
                return;
            }

            if (string.IsNullOrWhiteSpace(sku.SkuSpec))
            {
                return;
            }
            _flagSkuDataSource = true;
            sluSku.DataSource = _sluSkuDataSource.Where(d => string.IsNullOrWhiteSpace(d.SkuSpec) || d.SkuSpec == sku.SkuSpec)
                .ToList();
        }

        private void btnResetAll_Click(object sender, EventArgs e)
        {
            cbxBox.SelectedIndex = 0;
            txtSpecialCustomer.EditValue = null;
            txtPrintNumber.EditValue = 2;
            txtProductPlan.EditValue = null;
            txtPo.EditValue = null;
            txtWo.EditValue = null;

            _skuDetails = new BindingList<SkuDetail>();
            gridControl1.DataSource = _skuDetails;

            if (_flagSkuDataSource)
            {
                sluSku.DataSource = _sluSkuDataSource;
                _flagSkuDataSource = false;
            }
        }

        private void btnPreviewPrint_Click(object sender, EventArgs e)
        {
            BatchPrintDto batchPrintDto = GetBatchPrintDto();

            var batchPackingReport = new BatchPackingReport(batchPrintDto);
            batchPackingReport.ShowPreview();
        }
    }
}