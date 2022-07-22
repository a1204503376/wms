using System;
using System.ComponentModel;
using System.Windows.Forms;

namespace Packaging.SystemManager
{
    public partial class LoginForm : DevExpress.XtraEditors.XtraForm
    {
        /// <summary>
        /// Occurs when the OK button is clicked. 
        /// </summary>
        public event EventHandler OkClicked;

        public string UserName
        {
            get => this.userNameEdit.Text.Trim();
            set => this.userNameEdit.Text = value;
        }

        public string Password => this.passwordEdit.Text.Trim();

        public LoginForm()
        {
            InitializeComponent();
        }

        private void TxtUserName_Properties_Validating(object sender, CancelEventArgs e)
        {
            var message = string.IsNullOrEmpty(userNameEdit.Text) 
                ? "用户名不能为空"
                : string.Empty;
            dxErrorProvider1.SetError(userNameEdit, message);
        }

        private void PasswordEdit_Properties_Validating(object sender, CancelEventArgs e)
        {
            var message = string.IsNullOrEmpty(passwordEdit.Text)
                ? "密码不能为空"
                : string.Empty;
            dxErrorProvider1.SetError(passwordEdit, message);
        }

        private void BtnLogin_Click(object sender, EventArgs e)
        {
            this.OkPressed();
        }

        private void OkPressed()
        {
            this.userNameEdit.Enabled = false;
            this.passwordEdit.Enabled = false;
            this.btnLogin.Enabled = false;
            this.OnOkClicked(this, EventArgs.Empty);
        }

        /// <summary>
        /// Raises the OkClicked event.
        /// </summary>
        /// <param name="sender">The sender of the event.</param>
        /// <param name="e">A System.EventArgs that contains the event data.</param>
        private void OnOkClicked(object sender, EventArgs e)
        {
            if (this.OkClicked != null)
            {
                OkClicked(sender, e);
            }
        }

        private void BtnClose_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        /// <summary>
        /// Shows the bad user name or password message. Also resets the dialog.
        /// </summary>
        public void ShowBadUserPassword()
        {
            if (this.Disposing) return;
            this.Reset();

            this.passwordEdit.Text = string.Empty;
            this.passwordEdit.Focus();
                
            Application.DoEvents();
        }

        /// <summary>
        /// 重置对话框并且将控件置于初始状态
        /// </summary>
        private void Reset()
        {
            if (this.Disposing) return;
            Application.DoEvents();

            this.userNameEdit.Enabled = true;
            this.passwordEdit.Enabled = true;
            this.btnLogin.Enabled = true;
        }

        private void LoginForm_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
            {
                this.OkPressed();
            }
        }

        private void LoginForm_Load(object sender, EventArgs e)
        {
        }
    }
}