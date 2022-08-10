using System;
using System.Collections.Generic;
using System.Drawing;
using System.Threading;
using System.Windows.Forms;
using TUpdater.Updater;

namespace TUpdater
{
    public partial class FrmMain : Form
    {
        private readonly string _mainAppExecutablePath;
        private readonly UpdateService _updateService;
        private bool _isExistAfterUpdate;

        public FrmMain(string mainAppExecutablePath)
        {
            _isExistAfterUpdate = false;
            this._mainAppExecutablePath = mainAppExecutablePath;

            InitializeComponent();

            _updateService = new UpdateService(new AppVersionManager());
            _updateService.UpdateProgessHandler += UpdateService_UpdateProgessHandler;
        }

        private void UpdateService_UpdateProgessHandler(string obj)
        {
            if (InvokeRequired)
            {
                BeginInvoke(new Action<string>(UpdateService_UpdateProgessHandler), obj);
            }
            else
            {
                toolStripStatusLabel1.Text = obj ?? "";
            }
        }

        private void lboxUpdateMsg_MeasureItem(object sender, MeasureItemEventArgs e)
        {
            e.ItemHeight = 24;
        }

        private void lboxUpdateMsg_DrawItem(object sender, DrawItemEventArgs e)
        {
            e.Graphics.FillRectangle(new SolidBrush(e.BackColor), e.Bounds);
            if (e.Index >= 0)
            {
                var sStringFormat = new StringFormat {LineAlignment = StringAlignment.Center};
                e.Graphics.DrawString(((ListBox)sender).Items[e.Index].ToString(), e.Font, new SolidBrush(e.ForeColor), e.Bounds, sStringFormat);
            }
            e.DrawFocusRectangle();
        }

        private void FrmMain_Load(object sender, EventArgs e)
        {
            var needUpdate = _updateService.NeedUpdate(_mainAppExecutablePath);
            lbCurrentVersion.Text = _updateService.VersionManager.ClientVersion.GetVersion();
            lbNewVersion.Text = _updateService.VersionManager.ServerVersion.GetVersion();
            var updateContent = new List<string>();
            updateContent.AddRange(_updateService.VersionManager.ServerVersion.GetUpdateContents());
            lboxUpdateMsg.DataSource = updateContent;
            if (needUpdate)
            {
                var updateThread = new Thread(() =>
                {
                    _updateService.RunUpdate(_mainAppExecutablePath);
                    _isExistAfterUpdate = true;
                    _updateService.ExistCurrentAppAndStartMain(_mainAppExecutablePath);
                }) {IsBackground = true};
                updateThread.Start();
                return;
            }

            _isExistAfterUpdate = true;
            _updateService.ExistCurrentAppAndStartMain(_mainAppExecutablePath);
        }

        private void FrmMain_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (_isExistAfterUpdate)
                return;

            if (MessageBox.Show(@"确定退出自动更新程序吗？退出之后程序无法正常启动的。", @"退出提示", 
                MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.No)
            {
                e.Cancel = true;
            }
        }
    }
}
