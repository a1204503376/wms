using System;
using DataAccess.Encasement;
using DataAccess.Enitiies;
using Packaging.Common;
using Packaging.Settings;
using Packaging.Utility;

namespace Packaging.Encasement
{
    [ModuleDefine(moduleId: 103)]
    public partial class SpeedClassForm : DevExpress.XtraEditors.XtraForm
    {
        public SpeedClassForm()
        {
            InitializeComponent();
        }

        private void ApplicableSpeedClassForm_Load(object sender, EventArgs e)
        {
            SetDataSource();
        }

        public void SetDataSource()
        {
            var packingSpeedClasses = SpeedClassDal.GetAll();
            gridControl1.DataSource = packingSpeedClasses;
        }

        private void barManager1_ItemClick(object sender, DevExpress.XtraBars.ItemClickEventArgs e)
        {
            switch (e.Item.Tag)
            {
                case UiTags.Refresh:
                    SetDataSource();
                    break;
                case UiTags.New:
                    New();
                    break;
                case UiTags.Edit:
                    Edit();
                    break;
                case UiTags.Remove:
                    Remove();
                    break;
            }
        }

        private void Remove()
        {
            if (GetCurRow(out var focusedRow))
            {
                return;
            }

            SpeedClassDal.Remove(focusedRow.Id);
            SetDataSource();
        }

        private void Edit()
        {
            if (GetCurRow(out var focusedRow))
            {
                return;
            }

            var rolesEditForm = new SpeedClassEditForm(focusedRow.Id, this);
            rolesEditForm.Show();
        }

        private void New()
        {
            var rolesEditForm = new SpeedClassEditForm(this);
            rolesEditForm.Show();
        }

        private bool GetCurRow(out PackingSpeedClass focusedRow)
        {
            focusedRow = gridView1.GetFocusedRow() as PackingSpeedClass;
            if (focusedRow != null)
            {
                return false;
            }

            CustomMessageBox.Warning("请选择一条记录");
            return true;
        }
    }
}