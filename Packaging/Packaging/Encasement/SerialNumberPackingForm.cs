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
using System.Configuration;
using System.IO;
using DevExpress.Utils.Extensions;
using DevExpress.XtraPrinting;

namespace Packaging.Encasement
{
    [ModuleDefine(moduleId:Constants.SerialFormId)]
    public partial class SerialNumberPackingForm : XtraForm
    {
        private string _boxNumber;
        private bool _flagCollect;
        private List<string> _packingSequenctNumberPairs;
        private List<Sku> _skus;
        private List<PackingSpeedClass> _packingSpeedClasses;
        private List<PackingSerialDetail> _packingSerialDetails = new List<PackingSerialDetail>();
        private readonly Dictionary<string, List<string>> _skuCodeSkuSpecListDict = new Dictionary<string, List<string>>();
        public SerialNumberPackingForm()
        {
            InitializeComponent();
        }

        private void SerialNumberPackingForm_Load(object sender, EventArgs e)
        {
            SetSluSkuDataSource();
            SetSluSpeedClassDataSource();
            GetSequenceNumberPairsData();
            gridControl1.DataSource = _packingSerialDetails;
        }

        private void GetSequenceNumberPairsData()
        {
            _packingSequenctNumberPairs = BladeDictDal.GetPackingSequenctNumberPairs();
        }

        private void SetSluSpeedClassDataSource()
        {
            _packingSpeedClasses = SpeedClassDal.GetAll();
            sluSpeedClass.Properties.DisplayMember = "SpeedClass";
            sluSpeedClass.Properties.KeyMember = "Id";
            sluSpeedClass.Properties.DataSource = _packingSpeedClasses;
        }

        private void SetSluSkuDataSource()
        {
            _skus = WmsSkuDal.GetAll();
            sluSku.Properties.DisplayMember = "SkuName";
            sluSku.Properties.KeyMember = "SkuId";
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
                .Where(d => !string.IsNullOrWhiteSpace(d))
                .Select(skuSpec =>
                    new Sku
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

            btnExport.Enabled = false;
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
                btnExport.Enabled = true;
            }
            btnResetAll.Enabled = true;
            btnResetSerialNumber.Enabled = true;
        }

        private void txtSerialNumber_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(luModel.Text)
                || string.IsNullOrWhiteSpace(txtSerialNumber.Text))
            {
                Serilog.Log.Warning($"二维码内容({txtSerialNumber.Text})或者型号({luModel.Text})为空");
                ResetSerialNumberFocus(txtSerialNumber);
                return;
            }

            // 分析二维码内容
            // 255987000075190801820;200-250km/h 燕尾I-C9型粉末冶金闸片;北京天宜上佳高新材料股份有限公司
            var qrCodeSeparator = ConfigurationManager.AppSettings["QrCodeSeparator"];
            var qrCodeSeparatorList = qrCodeSeparator.Split(',');
            var qrCodeSeparatorCharArray = new char[qrCodeSeparatorList.Length];
            for (var i = 0; i < qrCodeSeparatorList.Length; i++)
            {
                qrCodeSeparatorCharArray[i] = Convert.ToChar(qrCodeSeparatorList[i]);
            }
            var qrCodeArray = txtSerialNumber.Text.Split(qrCodeSeparatorCharArray);
            if (qrCodeArray.Length==1)
            {
                Serilog.Log.Warning($"二维码分割长度只有一个， QR CODE:{txtSerialNumber.Text}");
                return;
            }
            // 产品标识代码 和 产品辅助代码
            // 255987000075190801820
            var productIdentificationCode = qrCodeArray[0].Substring(0,12);
            if (GetCurSku(out Sku sku))
            {
                CustomMessageBox.Warning("当前物品选中的对象非SKU");
                return;
            }

            if (!string.IsNullOrWhiteSpace(sku.SkuBarcodeList) && sku.SkuBarcodeList!=productIdentificationCode)
            {
                SoundPlayerHelper.ProductIdentificationCodeNotMatch();
                CustomMessageBox.Warning($"当前扫描的产品标识代码[{productIdentificationCode}]与配置[{sku.SkuBarcodeList}]的不匹配!");
                return;
            }

            var productSupportCodeString = qrCodeArray[0].Substring(12, 9);
            var flag = int.TryParse(productSupportCodeString, out var productSupportCode);
            if (!flag)
            {
                SoundPlayerHelper.Error();
                CustomMessageBox.Warning($"当前扫描的序列号非数字，参数：{productSupportCodeString}");
                return;
            }


            var packingAutoIdentifications = GetGridDataSource();
            if (packingAutoIdentifications.FindIndex(d =>
                    d.ProductSupportCode == productSupportCode 
                    && d.ProductIdentificationCode == productIdentificationCode) > -1)
            {
                SoundPlayerHelper.Duplicate();
                ResetSerialNumberFocus(txtSerialNumber);
                return;
            }

            var packingAutoIdentification = new PackingSerialDetail
            {
                ProductIdentificationCode = productIdentificationCode,
                ProductSupportCode = productSupportCode,
                CreateTime = DateTime.Now
            };

            _packingSerialDetails.Add(packingAutoIdentification);

            gridControl1.RefreshDataSource();

            ResetSerialNumberFocus(txtSerialNumber);
        }

        private List<PackingSerialDetail> GetGridDataSource()
        {
            if (gridControl1.DataSource is List<PackingSerialDetail> packingAutoIdentifications)
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
            if (!(info is { IsRowIndicator: true }))
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
            var dialogResult = CustomMessageBox.Confirm("确定清空所有明细？");
            if (dialogResult == DialogResult.No)
            {
                return;
            }
            ResetSerialNumber();
        }

        private void ResetSerialNumber()
        {
            _packingSerialDetails = new List<PackingSerialDetail>();
            gridControl1.DataSource = _packingSerialDetails;
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
            ResetReprint();
        }

        private void btnSavePrint_Click(object sender, EventArgs e)
        {
            if (!ValidList())
            {
                return;
            }
            var serialNumberReport = GetSerialNumberReport();
            serialNumberReport.PrintDialog();
            ResetReprint();
        }

        private void ResetReprint()
        {
            btnReprint.Text = @$"箱标重打";
            _boxNumber = string.Empty;
        }

        private SerialNumberReport GetSerialNumberReport()
        {
            var serialNumberPrintDtos = GetSerialNumberPrintDtos();
            var serialNumberReport = new SerialNumberReport(serialNumberPrintDtos);
            return serialNumberReport;
        }

        private List<SerialNumberPrintDto> GetSerialNumberPrintDtos()
        {
            Sku sku = GetCurSku();

            if (!(gridControl1.DataSource is List<PackingSerialDetail> packingSerialDetails))
            {
                return null;
            }
            var serialNumberPrintDtos = new List<SerialNumberPrintDto>();
            // 辅助代码6个一组，多一组多打印一张
            var serialNumberRanges = SerialNumberPrintDto.GetSerialNumberRanges(packingSerialDetails.Select(d => d.ProductSupportCode).ToList());
            var groupSerialNumber = (int)Math.Ceiling((double)serialNumberRanges.Count / SerialNumberPrintDto.SerialGroupNumber);
            for (int i = 0; i < groupSerialNumber; i++)
            {
                var serialNumberPrintDto = new SerialNumberPrintDto
                {
                    SkuId = sku.SkuId,
                    SkuCode = sku.SkuCode,
                    SkuName = sku.SkuName,
                    SkuNameS = sku.SkuNameS,
                    WspName = sku.WspName,
                    BoxType = cbxBox.Text,
                    Model = luModel.Text,
                    ProductionPlan = txtProductPlan.Text,
                    PoCode = txtPo.Text,
                    WoCode = txtWo.Text,
                    AssemblePeople = txtAssemblePeople.Text,
                    UserName = GlobalSettings.UserName,
                    Qty = packingSerialDetails.Count,
                    SpeedClass = sluSpeedClass.Text,
                    ProductIdentificationCode = _packingSerialDetails.First().ProductIdentificationCode,
                    SpecialCustomer = txtSpecialCustomer.Text,
                    Copies = Convert.ToInt16(txtPrintNumber.EditValue),
                    BoxNumber = Constants.DefaulutBoxNumber,
                    SerialDetails = packingSerialDetails
                };

                if (!string.IsNullOrWhiteSpace(_boxNumber) && !NomatcBoxType())
                {
                    serialNumberPrintDto.BoxNumber = _boxNumber;
                }

                int index = (i * SerialNumberPrintDto.SerialGroupNumber);
                int count;
                if (i==0 && serialNumberRanges.Count < SerialNumberPrintDto.SerialGroupNumber)
                {
                    count = serialNumberRanges.Count;
                }
                else if ((i+1)==groupSerialNumber && (serialNumberRanges.Count - index) < SerialNumberPrintDto.SerialGroupNumber)
                {
                    count = serialNumberRanges.Count - index;
                }
                else
                {
                    count = SerialNumberPrintDto.SerialGroupNumber;
                }
                var numberRanges = serialNumberRanges.GetRange(index, count);
                serialNumberPrintDto.SetSerial(numberRanges);
                serialNumberPrintDtos.Add(serialNumberPrintDto);
            }

            var serialNumberPrintDtoFirst = serialNumberPrintDtos.First();
            // 组装给WMS的入库数据
            // 物品型号是指定的成对物品，并且使用2个序列号时，WMS保存奇数，过滤掉偶数的
            if (SerialNumberPrintDto.IsDobuleSerialNumber(_packingSequenctNumberPairs, serialNumberPrintDtoFirst)
                && packingSerialDetails.Count >= 2
                && packingSerialDetails.Any(d => d.ProductSupportCode % 2 != 0))
            {
                packingSerialDetails = packingSerialDetails.OrderBy(d => d.ProductSupportCode)
                    .Where(d => d.ProductSupportCode % 2 != 0)
                    .ToList();

                // 箱标显示的数量重新赋值
                serialNumberPrintDtos.ForEach(d => d.Qty = packingSerialDetails.Count);
            }

            // WMS要求根据钢背批次和摩擦块批次进行分组管理
            var groupBy =
                packingSerialDetails.GroupBy(d =>
                                   d.SteelBackBatch + Constants.Underline
                                   + d.FrictionBlockBatch);

            var wmsSkuPackageDetail = WmsSkuPackageDetailDal.GetFirst(sku.WspId);

            var receiveDetailLpns = new List<ReceiveDetailLpn>();
            foreach (var pair in groupBy)
            {
                var keyArray = pair.Key.Split(Constants.Underline);
                var steelBackBatch = keyArray[0];
                var frictionBlockBatch = keyArray[1];
                var receiveDetailLpn = new ReceiveDetailLpn
                {
                    Id = SnowflakeIds.Instance.NextId(),
                    DetailStatus = ReceiveDetailStatusEnum.NotReceipt,
                    SkuId = sku.SkuId,
                    SkuCode = sku.SkuCode,
                    SkuName = sku.SkuName,
                    SkuSpec = serialNumberPrintDtoFirst.Model,
                    WspId = sku.WspId,
                    BoxCode = serialNumberPrintDtoFirst.BoxNumber,
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
                    SkuLot1 = frictionBlockBatch,
                    SkuLot2 = serialNumberPrintDtoFirst.Model,
                    SkuLot4 = serialNumberPrintDtoFirst.SpecialCustomer,
                    SkuLot5 = steelBackBatch,
                    SkuLot6 = frictionBlockBatch,
                    SkuLot7 = pair.First().ProductIdentificationCode,
                    SkuLot8 = serialNumberPrintDtoFirst.SpeedClass,
                    Udf1 = txtProductPlan.Text,
                    Udf2 = txtPo.Text,
                    Udf3 = txtWo.Text,
                    CreateUser = GlobalSettings.UserId,
                    CreateDept = GlobalSettings.UserDeptId,
                };
                // 成对的序列号，WMS保存奇数即可，如：160个序列号，2个一对，箱贴显示：80对/箱，WMS存1，3，5，7... 序列号
                // 固定指定的成品（闸片）的型号成对时，采用2个序列号
                SetSnCodePlanQty(pair.OrderBy(d=>d.ProductSupportCode)
                        .Select(d => d.ProductSupportCode).ToList(),
                    serialNumberPrintDtoFirst,
                    receiveDetailLpn);
                receiveDetailLpns.Add(receiveDetailLpn);
            }

            serialNumberPrintDtoFirst.ReceiveDetailLpns = receiveDetailLpns;

            return serialNumberPrintDtos;
        }

        private Sku GetCurSku()
        {
            if (!(sluSku.EditValue is Sku sku))
            {
                throw new Exception("请选择物品");
            }

            return sku;
        }

        private static void SetSnCodePlanQty(IReadOnlyList<int> serialNumberList,
            SerialNumberPrintDto serialNumberPrintDto,
            ReceiveDetailLpn receiveDetailLpn)
        {
            receiveDetailLpn.SnCode = string.Join(Constants.DefaultSeparator, serialNumberList);
            receiveDetailLpn.PlanQty = serialNumberList.Count;
        }

        private void btnPreviewPrint_Click(object sender, EventArgs e)
        {
            if (!ValidList())
            {
                return;
            }

            var serialNumberReport = GetSerialNumberReport();
            serialNumberReport.ShowPreviewDialog();
        }

        private bool ValidList()
        {
            var packingAutoIdentifications = GetGridDataSource();
            var packingAutoIdentification = packingAutoIdentifications.Find(
                d => string.IsNullOrWhiteSpace(d.FrictionBlockBatch)
                     || string.IsNullOrWhiteSpace(d.SteelBackBatch));
            if (packingAutoIdentification == null)
            {
                return true;
            }

            gvSerialNumber.ActiveFilterEnabled = false;
            int currentRowHandle = gvSerialNumber.GetVisibleRowHandle(0);
            while (currentRowHandle != DevExpress.XtraGrid.GridControl.InvalidRowHandle)
            {
                if (!(gvSerialNumber.GetRow(currentRowHandle) is PackingSerialDetail row))
                {
                    continue;
                }

                if (row.ProductIdentificationCode == packingAutoIdentification.ProductIdentificationCode
                    && row.ProductSupportCode == packingAutoIdentification.ProductSupportCode)
                {
                    gvSerialNumber.FocusedRowHandle = currentRowHandle;
                    break;
                }

                currentRowHandle = gridView1.GetNextVisibleRow(currentRowHandle);
            }

            var msg = string.IsNullOrWhiteSpace(packingAutoIdentification.FrictionBlockBatch) ? "摩擦块批次" : "钢背批次";
            CustomMessageBox.Warning(
                $"行号[{currentRowHandle + 1}], 辅助代码[{packingAutoIdentification.ProductSupportCode}]的{msg}为空!");
            return false;
        }

        private void gvSerialNumber_KeyDown(object sender, KeyEventArgs e)
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
            gvSerialNumber.DeleteRow(gvSerialNumber.FocusedRowHandle);
        }

        private void gvSerialNumber_CellValueChanged(object sender, DevExpress.XtraGrid.Views.Base.CellValueChangedEventArgs e)
        {
            var packingAutoIdentifications = GetGridDataSource();

            // 当前行 被包含在选中的集合内才允许一起变更
            if (!(gvSerialNumber.GetFocusedRow() is PackingSerialDetail row))
            {
                return;
            }

            if (!packingAutoIdentifications.Exists(d =>
                    d.Checked
                    && d.ProductIdentificationCode == row.ProductIdentificationCode
                    && d.ProductSupportCode == row.ProductSupportCode))
            {
                return;
            }

            if (packingAutoIdentifications.Count(d => d.Checked) <= 1)
            {
                return;
            }
            // 摩擦块批次
            var isFrictionBlockBatch = e.Column== colFrictionBlockBatch;
            // 钢背批次
            var isSteelBackBatch = e.Column==colSteelBackBatch;
            // 组装日期
            var isAssembleDate = e.Column == colAssembleDate;
            // 追踪号
            var isTrackingNumber = e.Column == colTrackingNumber;
            // 卡簧批次
            var isSpringBatch = e.Column == colSpringBatch;
            // 弹性元件批次
            var isElasticElementBatch = e.Column == colElasticElementBatch;
            // 备注
            var isRemark = e.Column == colRemark;

            if (isFrictionBlockBatch||isSteelBackBatch||isAssembleDate||isTrackingNumber
                ||isSpringBatch||isElasticElementBatch||isRemark)
            {
                packingAutoIdentifications.Where(d=>d.Checked)
                    .ForEach(d =>
                    {
                        var eValue = (string)e.Value;
                        if (isFrictionBlockBatch)
                        {
                            d.FrictionBlockBatch = eValue;
                        }
                        else if (isSteelBackBatch)
                        {
                            d.SteelBackBatch = eValue;
                        }
                        else if (isAssembleDate)
                        {
                            d.AssembleDate = eValue;
                        }
                        else if (isTrackingNumber)
                        {
                            d.TrackingNumber = eValue;
                        }
                        else if (isSpringBatch)
                        {
                            d.SpringBatch = eValue;
                        }
                        else if (isElasticElementBatch)
                        {
                            d.ElasticElementBatch = eValue;
                        }
                        else if (isRemark)
                        {
                            d.Remark = eValue;
                        }
                    });
                gridControl1.RefreshDataSource();
            }
        }

        private void btnExport_Click(object sender, EventArgs e)
        {
            var packingAutoIdentifications = GetGridDataSource();
            if (packingAutoIdentifications.Count==0)
            {
                CustomMessageBox.Warning("没有序列号数据可以导出");
                return;
            }

            var dialogResult = xtraFolderBrowserDialog1.ShowDialog();
            if (dialogResult!= DialogResult.OK)
            {
                return;
            }
            var packingAutoIdentification = packingAutoIdentifications.First();
            var curSku = GetCurSku();
            // 物品型号包含：/，放在后面
            var model = luModel.Text;
            if (luModel.Text.Contains("/"))
            {
                model = luModel.Text.Replace("/", " ");
            }
            var fileName = Path.Combine(xtraFolderBrowserDialog1.SelectedPath,
                @$"{packingAutoIdentification.ProductIdentificationCode}_{curSku.SkuNameS}({model})_{GlobalSettings.UserName}_{DateTime.Now:yyyyMMdd}.xlsx");
            if (File.Exists(fileName) 
                   && CustomMessageBox.Confirm("该文件名已存在，是否覆盖？")!=DialogResult.Yes)
            {
                return;
            }

            try
            {
                // gvSerialNumber.ExportToXlsx(fileName, new XlsxExportOptions
                // {
                //     TextExportMode = TextExportMode.Value,
                // });
                ExcelHelper.ExportSerialNumber(packingAutoIdentifications,fileName);
                CustomMessageBox.Information("数据导出成功！");
            }
            catch (Exception ex)
            {
                CustomMessageBox.Warning(ex.Message.Contains("正由另一进程使用") 
                    ? "数据导出失败！文件正由另一个程序占用！" 
                    : ex.Message);
            }
        }

        private void cbxBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            SetFilterSkuDataSource();
            if (NomatcBoxType())
            {
                CustomMessageBox.Information($"当前选择的箱型[{cbxBox.Text}]与重打的箱号[{_boxNumber}]不匹配，系统将生成新的箱号!");
            }
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
            sluSku.EditValue = null;
            List<Sku> dataSource;
            if (new List<string> { "A", "B", "C" }.Contains(cbxBox.Text))
            {
                dataSource = _skus.Where(d => d.Udf1 == cbxBox.Text).ToList();
            }
            else
            {
                dataSource = _skus.Where(d => d.Udf1 == cbxBox.Text
                                              || string.IsNullOrWhiteSpace(d.Udf1)).ToList();
            }

            sluSku.Properties.DataSource = dataSource;
        }

        private void sluSku_EditValueChanged(object sender, EventArgs e)
        {
            if (GetCurSku(out Sku sku))
            {
                return;
            }

            if (!string.IsNullOrWhiteSpace(sku.SkuRemark))
            {
                var packingSpeedClass = _packingSpeedClasses.FirstOrDefault(d => d.SpeedClass == sku.SkuRemark);
                if (packingSpeedClass!=null)
                {
                    sluSpeedClass.EditValue = packingSpeedClass;
                }
            }
            else
            {
                sluSpeedClass.EditValue = null;
            }

            if (!string.IsNullOrWhiteSpace(sku.SkuSpec))
            {
                SetModelDataSource(sku.SkuSpec);
                luModel.ReadOnly = true;
            }
            else
            {
                // 通用件的型号为空时，有字典配置值时，过滤相关数据
                var skuSpecList = _skuCodeSkuSpecListDict.ContainsKey(sku.SkuCode) 
                    ? _skuCodeSkuSpecListDict[sku.SkuCode] 
                    : _skus.Select(d => d.SkuSpec).Distinct().ToList();

                SetModelDataSource(skuSpecList);
                luModel.ReadOnly = false;
            }
        }

        private bool GetCurSku(out Sku sku)
        {
            sku = sluSku.EditValue as Sku;
            return sku == null;
        }

        private void btnImportDetail_Click(object sender, EventArgs e)
        {
            xtraOpenFileDialog1.Filter = @"Excel File|*.xlsx;*.xls";
            xtraOpenFileDialog1.Title = @"导入序列号装箱明细";
            xtraOpenFileDialog1.Multiselect = false;

            xtraOpenFileDialog1.ShowDialog();
        }

        private void xtraOpenFileDialog1_FileOk(object sender, System.ComponentModel.CancelEventArgs e)
        {
            var filePath = xtraOpenFileDialog1.FileName;
            SetSerialDetailDataSource(ExcelHelper.ImportSerialDetail(filePath));
        }

        private void btnReprint_Click(object sender, EventArgs e)
        {
            var cartonLabelForm = new CartonLabelForm("Serial",this);
            cartonLabelForm.ShowDialog();
        }

        public void SetReprintDataSource(PackingSerialHeader packingSerialHeader,List<PackingSerialDetail> packingSerialDetails)
        {
            cbxBox.EditValue = packingSerialHeader.BoxType;
            SetFilterSkuDataSource();
            sluSku.EditValue = _skus.FirstOrDefault(d => d.SkuId == packingSerialHeader.SkuId);
            luModel.EditValue = packingSerialHeader.Model;
            txtPo.EditValue = packingSerialHeader.PoCode;
            txtWo.EditValue = packingSerialHeader.WoCode;
            txtProductPlan.EditValue = packingSerialHeader.ProductionPlan;
            txtSpecialCustomer.EditValue = packingSerialHeader.SpecialCustomer;
            sluSpeedClass.EditValue =_packingSpeedClasses.FirstOrDefault(d=>d.SpeedClass == packingSerialHeader.SpeedClass);

            SetSerialDetailDataSource(packingSerialDetails);
            _boxNumber = packingSerialHeader.BoxNumber;
            btnReprint.Text = @$"箱标重打({packingSerialHeader.BoxNumber})";
            SetReadOnlyFalse();
        }

        private void SetSerialDetailDataSource(List<PackingSerialDetail> packingSerialDetails)
        {
            _packingSerialDetails = packingSerialDetails;
            gridControl1.DataSource = _packingSerialDetails;
        }
    }
}