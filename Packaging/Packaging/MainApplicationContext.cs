using System;
using System.ComponentModel;
using System.Windows.Forms;
using Packaging.Common;
using Packaging.Settings;
using Packaging.SystemManager;
using Packaging.Utility;

namespace Packaging
{
    internal class MainApplicationContext : ApplicationContext
    {
        private readonly System.ComponentModel.BackgroundWorker _backgroundWorkerLogon;
        private readonly LoginForm _logonDialog;
        private string _userName = string.Empty;

        /// <summary>
        /// ��ȡ��Ӧ�ó����Ƿ�Ӧ������
        /// </summary>
        public bool ShouldRun { get; } = true;

        /// <summary>
        /// Initializes a new instance of the MainApplicationContext class.
        /// </summary>
        public MainApplicationContext()
        {
            //The background thread is required to allow the UI thread to be free to animate.
            this._backgroundWorkerLogon = new System.ComponentModel.BackgroundWorker();
            this._backgroundWorkerLogon.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorkerLogon_DoWork);
            this._backgroundWorkerLogon.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorkerLogon_RunWorkerCompleted);

            this._logonDialog = new LoginForm();
            try
            {
                //The clicked event will call the background thread.
                this._logonDialog.OkClicked += new EventHandler(this.logonDialog_OkClicked);
                if (this._logonDialog.ShowDialog() == DialogResult.OK)
                {
                    var formMain = new MainForm();
                    formMain.FormClosed += new FormClosedEventHandler(formMain_FormClosed);

                    formMain.Show();
                }
                else
                {
                    this.ShouldRun = false;
                    this.ExitThread();
                }
            }
            catch (Exception ex)
            {
                this.ShouldRun = false;            
                CustomMessageBox.Exception(ex.Message);
            }
            finally
            {
                this._logonDialog.Dispose();
            }
        }

        private void backgroundWorkerLogon_DoWork(object sender, DoWorkEventArgs e)
        {
            var success = false;

            var password = e.Argument.ToString();

            if (!string.IsNullOrEmpty(password))
            {
                try
                {
                    var validateUser = WmsApiHelper.Login(_userName, password);

                    if (validateUser == null)
                    {
                        CustomMessageBox.Warning("用户名或密码错误");
                        return;
                    }

                    if (validateUser.error_code.HasValue)
                    {
                        CustomMessageBox.Warning(validateUser.error_description);
                        return;
                    }

                    GlobalSettings.UserName = validateUser.user_name;
                    GlobalSettings.UserId = validateUser.user_id;
                    GlobalSettings.NickName = validateUser.nick_name;
                    GlobalSettings.UserDeptId = validateUser.dept_id;
                    success = true;

                }
                catch (Exception ex)
                {
                    CustomMessageBox.Exception(ex.GetOriginalException().Message);
                }
            }
            e.Result = success;
        }

        private void backgroundWorkerLogon_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            var success = e.Result!=null && bool.Parse(e.Result.ToString());

            if (success)
            {
                this._logonDialog.DialogResult = DialogResult.OK;
            }
            else
            {
                this._logonDialog.ShowBadUserPassword();
            }
        }

        private void logonDialog_OkClicked(object sender, EventArgs e)
        {
            try
            {
                Application.DoEvents();

                var password = ((LoginForm)sender).Password;
                _userName = this._logonDialog.UserName;

                this._backgroundWorkerLogon.RunWorkerAsync(password);
            }
            catch (Exception)
            {
                ((LoginForm)sender).ShowBadUserPassword();
            }
        }
        /// <summary>
        /// �˳�ʱ��������
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void formMain_FormClosed(object sender, System.EventArgs e)
        {
            System.Diagnostics.Process.GetCurrentProcess().Kill();
            System.Environment.Exit(0);
        }
    }

}
