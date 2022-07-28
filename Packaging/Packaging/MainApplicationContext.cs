using System;
using System.ComponentModel;
using System.Diagnostics;
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
        private string _updateUrl = string.Empty;
        private string _tempUpdatePath = string.Empty;
        private XmlFiles _xmlUpdater = null;
        private int _availableUpdate = 0;

        /// <summary>
        /// 获取主应用程序是否应该启动
        /// </summary>
        public bool ShouldRun { get; } = true;

        /// <summary>
        /// 检查是否需要更新
        /// </summary>
        /// <returns>结果 true-更新 false-不更新</returns>
        private bool CheckUpdate()
        {
            return false;
            // var strManifestFile = Application.StartupPath + "\\manifest.xml";
            // var strServerFile = string.Empty;
            //
            // try
            // {
            //     //从本地读取更新配置文件信息
            //     _xmlUpdater = new XmlFiles(strManifestFile);
            //
            //     //获取服务器地址
            //     _updateUrl = _xmlUpdater.GetNodeValue("//Url");
            //
            //     var appUpdater = new AppUpdater
            //     {
            //         UpdaterUrl = _updateUrl + "/manifest.xml"
            //     };
            //
            //     _tempUpdatePath = Environment.GetEnvironmentVariable("Temp") + "\\" + "_" + _xmlUpdater.FindNode("//Application").Attributes["applicationId"].Value + "\\";
            //     appUpdater.DownAutoUpdateFile(_tempUpdatePath);
            //
            //     //获取更新文件列表
            //     var htUpdateFile = new Hashtable();
            //     strServerFile = _tempUpdatePath + "\\manifest.xml";
            //
            //     _availableUpdate = appUpdater.CheckForUpdate(strServerFile, strManifestFile, out htUpdateFile);
            //     if (_availableUpdate > 0)
            //     {
            //         return true;
            //     }
            // }
            // catch (Exception ex)
            // {
            //     XtraMessageBox.Show(ex.Message);
            // }
            // return false;
        }

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
                    if (CheckUpdate())
                    {
                        var pro = new Process();

                        var appPath = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;
                        pro.StartInfo.FileName = Application.StartupPath + System.IO.Path.DirectorySeparatorChar + "Updater.exe";
                        pro.StartInfo.CreateNoWindow = true;
                        pro.StartInfo.WindowStyle = ProcessWindowStyle.Normal;
                        pro.Start();

                        this.ShouldRun = false;
                        this.ExitThread();
                    }

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
                        CustomMessageBox.Warning("账户名或密码错误");
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
        /// 退出时结束进程
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void formMain_FormClosed(object sender, System.EventArgs e)
        {
            this.ExitThread();
        }
    }

}
