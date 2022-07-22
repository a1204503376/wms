using System.Collections.Generic;
using System.Windows.Forms;
using DevExpress.XtraEditors;
using DevExpress.XtraEditors.DXErrorProvider;

namespace Packaging.Common
{
    public class CustomValidationCheckedComboBoxEdit : ValidationRule
    {
        public CustomValidationCheckedComboBoxEdit(string errorMsg)
        {
            this.ErrorText = errorMsg;
            this.ErrorType = ErrorType.Critical;
        }

        public override bool Validate(Control control, object value)
        {
            if (control is CheckedComboBoxEdit checkedComboBoxEdit)
            {
                return checkedComboBoxEdit.Properties.GetCheckedItems()
                           is List<object> checkedItems
                       && checkedItems.Count > 0;
            }
            return false;
        }

    }
}