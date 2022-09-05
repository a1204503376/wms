using System;
using System.Windows.Forms;
using Packaging.Utility;
using DataAccess.Encasement;

namespace Packaging.Encasement
{
    public partial class CartonLabelForm : DevExpress.XtraEditors.XtraForm
    {
        private readonly string _type;

        public CartonLabelForm(string type, Form owner)
        {
            InitializeComponent();
            this._type = type;
            this.Owner = owner;
        }

        private void btnSearch_Click(object sender, EventArgs e)
        {
            if (!dxValidationProvider1.Validate())
            {
                return;
            }

            var boxNumber = txtBoxNumber.Text.ToUpper();
            if (_type == "Serial")
            {
                SerialData(boxNumber);
            }
            else
            {
                BatchData(boxNumber);
            }

            this.Close();
        }

        private void SerialData(string boxNumber)
        {
            var existBoxNumber = PackingSerialDal.ExistSerialBoxNumber(boxNumber);
            if (!existBoxNumber)
            {
                CustomMessageBox.Warning($"箱号[{boxNumber}]不存在，请扫描序列号重新装箱!");
                this.Close();
                return;
            }

            var packingSerialHeader = PackingSerialDal.GetPackingSerialHeader(boxNumber);
            if (packingSerialHeader == null)
            {
                CustomMessageBox.Warning($"根据箱号[{boxNumber}]无法找到数据!");
                this.Close();
                return;
            }

            var packingSerialDetails = PackingSerialDal.GetPackingSerialDetails(packingSerialHeader.Id);
            if (packingSerialDetails == null || packingSerialDetails.Count == 0)
            {
                CustomMessageBox.Warning($"根据箱号[{boxNumber}]无法找到明细数据!");
                this.Close();
                return;
            }

            if (!(this.Owner is SerialNumberPackingForm serialNumberPackingForm))
            {
                CustomMessageBox.Warning("无法找到OwnerForm!");
                this.Close();
                return;
            }

            serialNumberPackingForm.SetReprintDataSource(packingSerialHeader, packingSerialDetails);
        }

        private void BatchData(string boxNumber)
        {
            var existBoxNumber = PackingSerialDal.ExistBatchBoxNumber(boxNumber);
            if (!existBoxNumber)
            {
                CustomMessageBox.Warning($"箱号[{boxNumber}]不存在，请扫描序列号重新装箱!");
                this.Close();
                return;
            }

            var packingBatchHeader = PackingSerialDal.GetPackingBatchHeader(boxNumber);
            if (packingBatchHeader == null)
            {
                CustomMessageBox.Warning($"根据箱号[{boxNumber}]无法找到数据!");
                this.Close();
                return;
            }

            var packingBatchDetails = PackingSerialDal.GetPackingBatchDetails(packingBatchHeader.Id);
            if (packingBatchDetails == null || packingBatchDetails.Count == 0)
            {
                CustomMessageBox.Warning($"根据箱号[{boxNumber}]无法找到明细数据!");
                this.Close();
                return;
            }

            if (!(this.Owner is BatchPackingForm batchPackingForm))
            {
                CustomMessageBox.Warning("无法找到OwnerForm!");
                this.Close();
                return;
            }

            batchPackingForm.SetReprintDataSource(packingBatchHeader, packingBatchDetails);
        }
    }
}