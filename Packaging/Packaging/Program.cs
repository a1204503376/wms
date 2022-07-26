using System;
using System.Diagnostics;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Text;
using System.Windows.Forms;
using Packaging.Settings;
using Serilog;

namespace Packaging
{
    internal static class Program
    {
        [DllImport("User32.dll")]
        public static extern bool ShowWindowAsync(IntPtr hWnd, int cmdShow);
        [DllImport("User32.dll")]
        public static extern bool SetForegroundWindow(IntPtr hWnd);

        [STAThread]
        private static void Main() 
        {
            Log.Logger = new LoggerConfiguration()
                    //设置最小日志级别
                    .MinimumLevel.Debug()
                    //将日志写到文件
                    .WriteTo.File($"logs/{DateTime.Now:yyyy-MM-dd}/log.txt", //日志按照天为单位创建文件夹
                        outputTemplate: @"{Timestamp:yyyy-MM-dd HH:mm-ss.fff }[{Level:u3}] {Message:lj}{NewLine}{Exception}",  // 设置输出格式，显示详细异常信息
                        rollingInterval: RollingInterval.Day, //日志按天保存
                        rollOnFileSizeLimit: true,            // 限制单个文件的最大长度
                        fileSizeLimitBytes: 1 * 1024 * 1024,        // 单个文件最大长度1M
                        encoding: Encoding.UTF8,              // 文件字符编码
                        retainedFileCountLimit: 30            // 最大保存文件数,超过最大文件数会自动覆盖原有文件
                    )
                    .CreateLogger();

            Application.ThreadException += new System.Threading.ThreadExceptionEventHandler(Application_ThreadException);
            AppDomain.CurrentDomain.UnhandledException += new UnhandledExceptionEventHandler(CurrentDomain_UnhandledException);

            var instance = RunningInstance();
            if (instance == null)
            {
                Application.EnableVisualStyles();
                Application.SetCompatibleTextRenderingDefault(false);
                //Use the application context to start things up.
                var mainApplicationContext = new MainApplicationContext();
                if (mainApplicationContext.ShouldRun == true)
                {
                    Application.Run(mainApplicationContext);
                }
            }
            else
            {
                HandleRunningInstance(instance);
            }
        }

        private static Process RunningInstance()
        {
            var current = Process.GetCurrentProcess();
            var processes = Process.GetProcessesByName(current.ProcessName);
            //Loop through the running processes in with the same name.
            foreach (var process in processes)
            {
                //Ignore the current process.
                if (process.Id != current.Id)
                {
                    //Make sure that the process is running from the exe file.  
                    if (current.MainModule != null && Assembly.GetExecutingAssembly().Location.Replace("/", "\\") 
                        == current.MainModule.FileName)
                    {
                        //Return the other process instance.
                        return process;
                    }
                }
            }
            //No other instance was found, return null.
            return null;
        }
        /// <summary>
        /// 处理运行时实例
        /// There is another instance of this process.
        /// </summary>
        /// <param name="instance">应用程序运行时实例</param>
        private static void HandleRunningInstance(Process instance)
        {
            //Make sure the window is not minimized or maximized.
            ShowWindowAsync(instance.MainWindowHandle, 1);
            //Set the real intance to foreground window.
            SetForegroundWindow(instance.MainWindowHandle);
        }

        /// <summary>
        /// 用来捕获应用程序中的异常情况
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private static void Application_ThreadException(object sender, System.Threading.ThreadExceptionEventArgs e)
        {
            MessageBox.Show(e.Exception.Message);//throw new NotImplementedException();
        }

        private static void CurrentDomain_UnhandledException(object sender, UnhandledExceptionEventArgs e)
        {
            MessageBox.Show(e.ToString());
        }


    }
}
