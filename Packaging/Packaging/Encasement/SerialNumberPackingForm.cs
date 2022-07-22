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
using Packaging.Settings;
using Packaging.Utility;

namespace Packaging.Encasement
{
    [ModuleDefine(moduleId:101)]
    public partial class SerialNumberPackingForm : DevExpress.XtraEditors.XtraForm
    {
        private bool _flagCollect = false;

        public SerialNumberPackingForm()
        {
            InitializeComponent();
        }

        private void SerialNumberPackingForm_Load(object sender, EventArgs e)
        {
            SetSpeedClassDataSource();
            SetSkuDataSource();
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
            txtSerialNumber.ReadOnly = false;
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
        }

        private void SetReadOnlyFalse()
        {
            btnCollect.Text = @"开始采集";
            txtSerialNumber.ReadOnly = true;
            cbxBox.ReadOnly = false;
            sluSku.ReadOnly = false;
            luModel.ReadOnly = false;
            txtProductPlan.ReadOnly = false;
            txtPo.ReadOnly = false;
            txtWo.ReadOnly = false;
            txtSpecialCustomer.ReadOnly = false;
            sluSpeedClass.ReadOnly = false;
            txtPrintNumber.ReadOnly = false;
        }

        private void txtSerialNumber_Leave(object sender, EventArgs e)
        {
            if (sender is TextEdit textEdit)
            {
                System.Console.WriteLine(textEdit.EditValue);
                textEdit.EditValue = null;
                textEdit.Focus();
            }
        }
    }
}