using System;
using System.Collections.Generic;
using System.Windows.Forms;
using DataAccess.Encasement;
using DataAccess.Enitiies;
using Packaging.Common;

namespace Packaging.Encasement
{
    public partial class SpeedClassEditForm : DevExpress.XtraEditors.XtraForm
    {
        private readonly long _id;
        private readonly bool _isEdit;
        private CustomValidation _customValidation;

        public SpeedClassEditForm(long id, Form owner)
        {
            InitializeComponent();
            this._id = id;
            this._isEdit = id != 0;
            this.Owner = owner;
        }

        public SpeedClassEditForm(Form owner):this(0,owner)
        {
        }

        private void SpeedClassEditForm_Load(object sender, EventArgs e)
        {
            if (_isEdit)
            {
                PackingSpeedClass packingSpeedClass = SpeedClassDal.GetSpeedClass(_id);
                txtSpeedClass.EditValue = packingSpeedClass.SpeedClass;
                txtOrder.EditValue = packingSpeedClass.Order;
            }
            SetValid();
        }



        private void SetValid()
        {
            _customValidation = new CustomValidation
            {
                RuleList = new List<ControlRule>
                {
                    new ControlRule(txtSpeedClass, ControlRule.NotEmpty()),
                    new ControlRule(txtOrder,ControlRule.IsPositiveInteger())
                }
            };
        }

        private void btnSave_Click(object sender, EventArgs e)
        {
            if (!_customValidation.Validate())
            {
                return;
            }
            SpeedClassDal.Save(new PackingSpeedClass()
            {
                Id = _id,
                SpeedClass = txtSpeedClass.Text,
                Order = Convert.ToInt32(txtOrder.EditValue)
            });

            SpeedClassForm owner = this.Owner as SpeedClassForm;
            owner?.SetDataSource();

            this.Close();
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}