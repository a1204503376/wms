using System;
using System.Windows.Forms;

namespace Packaging.Encasement
{
    public partial class DatePrintForm : DevExpress.XtraEditors.XtraForm
    {
        private readonly string _type;

        public DatePrintForm(string type, Form owner)
        {
            InitializeComponent();
            this._type = type;
            this.Owner = owner;
        }

        private void simpleButton1_Click(object sender, EventArgs e)
        {
            if (!dxValidationProvider1.Validate())
            {
                return;
            }

            var printDateTime = dateEdit1.DateTime;
            if (_type == "Serial")
            {
                SerialData(printDateTime);
            }
            else
            {
                BatchData(printDateTime);
            }

            this.Close();
        }

        private void BatchData(DateTime printDateTime)
        {
            var batchPackingForm = this.Owner as BatchPackingForm;
            if (batchPackingForm == null)
            {
                return;
            }
            batchPackingForm.Print(printDateTime);
        }

        private void SerialData(DateTime printDateTime)
        {
            var serialNumberPackingForm = this.Owner as SerialNumberPackingForm;
            serialNumberPackingForm?.Print(printDateTime);
        }
    }
}