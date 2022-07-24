using DevExpress.XtraEditors;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using DataAccess.Dto;
using DataAccess.Encasement;
using DataAccess.Enitiies;
using DataAccess.Enums;
using DataAccess.Wms;
using DevExpress.Utils;
using DevExpress.Utils.Drawing;
using DevExpress.XtraReports.UI;
using Packaging.Common;
using Packaging.Settings;
using Packaging.Utility;

namespace Packaging.Encasement
{
    [ModuleDefine(moduleId:101)]
    public partial class SerialNumberPackingForm : DevExpress.XtraEditors.XtraForm
    {
        private bool _flagCollect = false;
        private List<PackingAutoIdentification> _packingAutoIdentifications = new List<PackingAutoIdentification>();

        public SerialNumberPackingForm()
        {
            InitializeComponent();
        }

        private void SerialNumberPackingForm_Load(object sender, EventArgs e)
        {
            SetSpeedClassDataSource();
            SetSkuDataSource();
            gridControl1.DataSource = _packingAutoIdentifications;
        }

        private void SetSpeedClassDataSource()
        {
            var packingSpeedClasses = SpeedClassDal.GetAll();
            sluSpeedClass.Properties.DisplayMember = "SpeedClass";
            sluSpeedClass.Properties.KeyMember = "Id";
            sluSpeedClass.Properties.DataSource = packingSpeedClasses;
        }

        private void SetSkuDataSource()
        {
            var skus = SkuDal.GetAll();
            sluSku.Properties.DisplayMember = "SkuName";
            sluSku.Properties.KeyMember = "SkuId";
            sluSku.Properties.DataSource = skus;
        }

        private void sluSku_Properties_Closed(object sender, DevExpress.XtraEditors.Controls.ClosedEventArgs e)
        {
            ChangSku(sender);
        }

        private void ChangSku(object sender)
        {
            if (!(sender is SearchLookUpEdit searchLookUpEdit))
            {
                return;
            }

            if (!(searchLookUpEdit.EditValue is Sku editValue))
            {
                return;
            }

            if (!string.IsNullOrWhiteSpace(editValue.SkuSpec))
            {
                SetModelDataSource(editValue.SkuSpec);
                luModel.ReadOnly = true;
            }
            else
            {
                if (!(searchLookUpEdit.Properties.DataSource is List<Sku> dataSource))
                {
                    return;
                }

                var skuSpecList = dataSource.Select(d => d.SkuSpec).Distinct().ToList();
                SetModelDataSource(skuSpecList);
                luModel.ReadOnly = false;
            }
        }

        private void SetModelDataSource(string skuSpec)
        {
            SetModelDataSource(new List<string>
            {
                skuSpec
            });
        }

        private void SetModelDataSource(IEnumerable<string> skuSpecList)
        {
            var list = skuSpecList
                .Where(d=>!string.IsNullOrWhiteSpace(d))
                .Select(skuSpec => new Sku
            {
                SkuSpec = skuSpec
            }).ToList();

            if (list.Count > 1)
            {
                list.Insert(0, new Sku
                {
                    SkuSpec = string.Empty
                });
            }

            luModel.Properties.DisplayMember = "SkuSpec";
            luModel.Properties.ValueMember = "SkuSpec";
            luModel.Properties.DataSource = list;
            luModel.EditValue = list.First().SkuSpec;
        }

        private void btnCollect_Click(object sender, EventArgs e)
        {
            if (_flagCollect)
            {
                SetReadOnlyFalse();
                _flagCollect = false;
                return;
            }

            if (sluSku.EditValue == null)
            {
                CustomMessageBox.Warning("请选择物品");
                return;
            }

            if (luModel.EditValue==null 
                || string.IsNullOrEmpty( luModel.EditValue.ToString()))
            {
                CustomMessageBox.Warning("请选择型号");
                return;
            }

            _flagCollect = true;
            SetReadOnlyTrue();
        }

        private void SetReadOnlyTrue()
        {
            btnCollect.Text = @"停止采集";
            txtSerialNumber.Enabled = true;
            txtSerialNumber.Focus();
            cbxBox.ReadOnly = true;
            sluSku.ReadOnly = true;
            luModel.ReadOnly = true;
            txtProductPlan.ReadOnly = true;
            txtPo.ReadOnly = true;
            txtWo.ReadOnly = true;
            txtSpecialCustomer.ReadOnly = true;
            sluSpeedClass.ReadOnly = true;
            txtPrintNumber.ReadOnly = true;

            btnSavePrint.Enabled = false;
            btnResetAll.Enabled = false;
            btnResetSerialNumber.Enabled = false;
            btnPreviewPrint.Enabled = false;
        }

        private void SetReadOnlyFalse()
        {
            btnCollect.Text = @"开始采集";
            txtSerialNumber.Enabled = false;
            cbxBox.ReadOnly = false;
            sluSku.ReadOnly = false;
            luModel.ReadOnly = false;
            txtProductPlan.ReadOnly = false;
            txtPo.ReadOnly = false;
            txtWo.ReadOnly = false;
            txtSpecialCustomer.ReadOnly = false;
            sluSpeedClass.ReadOnly = false;
            txtPrintNumber.ReadOnly = false;

            var packingAutoIdentifications = GetGridDataSource();
            if (packingAutoIdentifications.Count != 0)
            {
                btnSavePrint.Enabled = true;
                btnSavePrint.Focus();
                btnPreviewPrint.Enabled = true;
            }
            btnResetAll.Enabled = true;
            btnResetSerialNumber.Enabled = true;
        }

        private void txtSerialNumber_Leave(object sender, EventArgs e)
        {
            if (!(sender is TextEdit textEdit))
            {
                return;
            }

            if (string.IsNullOrWhiteSpace(luModel.Text)
                || string.IsNullOrWhiteSpace(textEdit.Text))
            {
                Serilog.Log.Warning($"序列号({textEdit.Text})或者型号为空({luModel.Text})");
                ResetSerialNumberFocus(textEdit);
                return;
            }

            var packingAutoIdentifications = GetGridDataSource();
            if (packingAutoIdentifications.FindIndex(d =>
                    d.ProductSupportCode == textEdit.Text) > -1)
            {
                ResetSerialNumberFocus(textEdit);
                return;
            }

            PackingAutoIdentification bySerialNumberAndModel
                = AutoIdentificationDal.GetBySerialNumberAndModel(luModel.Text, textEdit.Text)
                  ?? new PackingAutoIdentification
                  {
                      ProductSupportCode = textEdit.Text
                  };

            _packingAutoIdentifications.Add(bySerialNumberAndModel);

            gridControl1.RefreshDataSource();

            ResetSerialNumberFocus(textEdit);
        }

        private List<PackingAutoIdentification> GetGridDataSource()
        {
            if (gridControl1.DataSource is List<PackingAutoIdentification> packingAutoIdentifications)
            {
                return packingAutoIdentifications;
            }

            throw new ArgumentException("序列号列表绑定的数据类型错误");
        }

        private static void ResetSerialNumberFocus(TextEdit textEdit)
        {
            textEdit.EditValue = null;
            textEdit.Focus();
        }

        private void gvSerialNumber_CustomDrawRowIndicator(object sender,
            DevExpress.XtraGrid.Views.Grid.RowIndicatorCustomDrawEventArgs e)
        {
            IndicatorObjectInfoArgs info = e.Info;
            if (info == null || !info.IsRowIndicator)
            {
                return;
            }

            info.Appearance.TextOptions.HAlignment = HorzAlignment.Center;

            if (e.RowHandle >= 0)
            {
                info.DisplayText = (e.RowHandle + 1).ToString();
            }
            else if (e.RowHandle < 0 && e.RowHandle > -1000)
            {
                info.Appearance.BackColor = System.Drawing.Color.AntiqueWhite;
                info.DisplayText = "G" + e.RowHandle.ToString();
            }
        }

        private void gvSerialNumber_RowCountChanged(object sender, EventArgs e)
        {
            if (gvSerialNumber.RowCount == -1)
            {
                gvSerialNumber.IndicatorWidth = 30;
            }
            else
            {
                gvSerialNumber.IndicatorWidth = gridView1.RowCount.ToString().Length + 50;
            }
        }

        private void btnResetSerialNumber_Click(object sender, EventArgs e)
        {
            var dialogResult = CustomMessageBox.Confirm("是否确定清空所有序列号");
            if (dialogResult == DialogResult.No)
            {
                return;
            }
            ResetSerialNumber();
        }

        private void ResetSerialNumber()
        {
            _packingAutoIdentifications = new List<PackingAutoIdentification>();
            gridControl1.DataSource = _packingAutoIdentifications;
            btnSavePrint.Enabled = false;
            btnCollect.Focus();
        }

        private void btnResetAll_Click(object sender, EventArgs e)
        {
            var dialogResult = CustomMessageBox.Confirm("是否确定重置全部");
            if (dialogResult == DialogResult.No)
            {
                return;
            }
            ResetSerialNumber();
            cbxBox.SelectedIndex = 0;
            sluSku.EditValue = null;
            luModel.EditValue = null;
            luModel.ReadOnly = true;
            txtProductPlan.EditValue = null;
            txtPo.EditValue = null;
            txtWo.EditValue = null;
            txtSpecialCustomer.EditValue = null;
            sluSpeedClass.EditValue = null;
            txtPrintNumber.EditValue = 2;
            cbxBox.Focus();
        }

        private void btnSavePrint_Click(object sender, EventArgs e)
        {
            var serialNumberPrintDto = GetSerialNumberPrintDto();

            var serialNumberReport = new SerialNumberReport(serialNumberPrintDto);
            serialNumberReport.Print();
        }

        private SerialNumberPrintDto GetSerialNumberPrintDto()
        {
            var serialNumberPrintDto = new SerialNumberPrintDto();

            if (!(sluSku.EditValue is Sku sku))
            {
                throw new Exception("请选择物品");
            }

            var autoIdentification = _packingAutoIdentifications.First();
            serialNumberPrintDto.SkuName = sku.SkuName;
            serialNumberPrintDto.BoxType = cbxBox.Text;
            serialNumberPrintDto.Model = luModel.Text;
            serialNumberPrintDto.PrintDate = DateTime.Now.ToString("yyMMdd");
            serialNumberPrintDto.UserName = GlobalSettings.UserName;
            serialNumberPrintDto.Qty = $"{_packingAutoIdentifications.Count} {sku.WspName}/箱";
            serialNumberPrintDto.SpeedClass = sluSpeedClass.Text;
            serialNumberPrintDto.ProductIdentificationCode = autoIdentification.ProductIdentificationCode;
            serialNumberPrintDto.SpecialCustomer = txtSpecialCustomer.Text;
            serialNumberPrintDto.Copies = Convert.ToInt16(txtPrintNumber.EditValue);
            serialNumberPrintDto.BoxNumber = Constants.DefaulutBoxNumber;
            SerialNumberDictionary(serialNumberPrintDto);

            // 入库成功后打印
            var groupBy =
                _packingAutoIdentifications.GroupBy(d =>
                    d.AssembleDate + Constants.Underline
                                   + d.SteelBackBatch + Constants.Underline
                                   + d.FrictionBlockBatch);

            var wmsSkuPackageDetail = WmsSkuPackageDetailDal.GetFirst(sku.WspId);

            var receiveDetailLpns = (
                from pair in groupBy
                let keyArray = pair.Key.Split(Constants.Underline)
                let assembleDate = keyArray[0]
                let steelBackBatch = keyArray[1]
                let frictionBlockBatch = keyArray[2]
                select new ReceiveDetailLpn
                {
                    Id = SnowflakeIds.Instance.NextId(),
                    DetailStatus = ReceiveDetailStatusEnum.NotReceipt,
                    SkuId = sku.SkuId,
                    SkuCode = sku.SkuCode,
                    SkuName = sku.SkuName,
                    SkuSpec = sku.SkuSpec,
                    WspId = sku.WspId,
                    BoxCode = serialNumberPrintDto.BoxNumber,
                    SnCode = string.Join(Constants.DefaultSeparator, pair.Select(d => d.ProductSupportCode)),
                    PlanQty = pair.Count(),
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
                    SkuLot1 = assembleDate,
                    SkuLot2 = serialNumberPrintDto.Model,
                    SkuLot4 = serialNumberPrintDto.SpecialCustomer,
                    SkuLot5 = steelBackBatch,
                    SkuLot6 = frictionBlockBatch,
                    SkuLot7 = pair.First().ProductIdentificationCode,
                    Udf1 = txtProductPlan.Text,
                    Udf2 = txtPo.Text,
                    Udf3 = txtWo.Text,
                    CreateUser = GlobalSettings.UserId,
                    CreateDept = GlobalSettings.UserDeptId,
                }).ToList();
            serialNumberPrintDto.ReceiveDetailLpns = receiveDetailLpns;
            return serialNumberPrintDto;
        }

        private void SerialNumberDictionary(SerialNumberPrintDto serialNumberPrintDto)
        {
            Dictionary<string, List<string>> serialNumberDictionary = new Dictionary<string, List<string>>();
            foreach (var identification in _packingAutoIdentifications)
            {
                if (identification.ProductSupportCode.Length < 7)
                {
                    continue;
                }
                var key = identification.ProductSupportCode.Substring(0, 6);
                var value = identification.ProductSupportCode.Replace(key, string.Empty);
                if (serialNumberDictionary.ContainsKey(key))
                {
                    serialNumberDictionary[key].Add(value);
                }
                else
                {
                    serialNumberDictionary.Add(key, new List<string>
                    {
                        value
                    });
                }
            }

            var serialNumberRanges = new List<SerialNumberRange>();
            foreach (var pair in serialNumberDictionary)
            {
                pair.Value.Sort();
                var serialNumberRange = new SerialNumberRange
                {
                    Key = pair.Key,
                    Begin = pair.Value.First(),
                    End = pair.Value.Last()
                };
                serialNumberRanges.Add(serialNumberRange);
            }
            serialNumberPrintDto.SerialNumberRanges = serialNumberRanges;
        }

        private void btnPreviewPrint_Click(object sender, EventArgs e)
        {
            var serialNumberPrintDto = GetSerialNumberPrintDto();

            var serialNumberReport = new SerialNumberReport(serialNumberPrintDto);
            serialNumberReport.ShowPreview();
        }
    }
}