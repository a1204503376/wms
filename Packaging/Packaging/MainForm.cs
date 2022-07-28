using System;
using System.Collections.Generic;
using System.Reflection;
using System.Windows.Forms;
using DevExpress.XtraBars;
using DevExpress.XtraBars.Docking2010.Views.Tabbed;
using DevExpress.XtraEditors;
using Packaging.Settings;

namespace Packaging
{
    public partial class MainForm : XtraForm
    {
        /// <summary>
        /// 功能模块
        /// </summary>
        private Dictionary<int, Type> FormTypes { get; }

        public MainForm()
        {
            FormTypes = new Dictionary<int, Type>();
            InitializeComponent();
            barManager1.MdiMenuMergeStyle = BarMdiMenuMergeStyle.Never;
            mainBar.OptionsBar.DrawBorder = false;
            mainBar.OptionsBar.AllowQuickCustomization = false;
            mainBar.DockStyle = BarDockStyle.Top;
        }

        /// <summary>
        /// 设置状态栏信息
        /// </summary>
        private void SetStatusState()
        {
            this.barManager1.StatusBar.AddItem(new BarStaticItem()
            {
                Caption = $@"当前登录:{GlobalSettings.UserName}"
            });
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
            LoadModules();
            SetStatusState();
        }

        private void barManager_ItemClick(object sender, ItemClickEventArgs e)
        {
            if (e.Item.Tag == null)
            {
                return;
            }
            var key = (int)e.Item.Tag;
            if (!FormTypes.ContainsKey(key))
            {
                return;
            }
            ActivatForm(FormTypes[key]);
        }

        private void ActivatForm(Type someType)
        {
            try
            {
                splashScreenManager1.ShowWaitForm();
                tabbedView1.BeginUpdate();
                foreach (var baseDocument in tabbedView1.Documents)
                {
                    var item = (Document)baseDocument;
                    if (item.Control.GetType() != someType) continue;
                    tabbedView1.Controller.Activate(item);
                    return;
                }

                var form = Activator.CreateInstance(someType) as Control;
                if (form == null)
                {
                    return;
                }
                tabbedView1.AddDocument(form);
            }
            finally
            {
                tabbedView1.EndUpdate();
                splashScreenManager1.CloseWaitForm();
            }
        }

        /// <summary>
        /// 预加载页面
        /// </summary>
        private void LoadModules()
        {
            var asm = Assembly.Load("Packaging");
            var types = asm.GetExportedTypes();
            foreach (var t in types)
            {
                var moduleDefineAttributeArray = t.GetCustomAttributes(typeof(ModuleDefineAttribute), true);
                if (moduleDefineAttributeArray.Length > 0)
                {
                    if (moduleDefineAttributeArray[0] is ModuleDefineAttribute attr)
                    {
                        FormTypes.Add(attr.ModuleId, t);
                    }
                }
            }
        }
    }
}
