using System;
using System.Windows.Forms;

namespace TUpdater
{
    static class Program
    {
        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        /// <param name="arg">参数格式：主程序的路径,包含程序本身名称</param>
        [STAThread]
        static void Main(string[] arg)
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            if (arg.Length < 1)
            {
                MessageBox.Show("自动更新程序缺少启动参数", "自动更新程序异常提示", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            Application.Run(new FrmMain(string.Join(" ", arg)));  // 使用string.join是为了防止主程序的路径中有空格时被分割了
        }
    }
}
