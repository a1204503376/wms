using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Windows.Forms;
using TUpdater.Utils;

namespace TUpdater.Updater
{
    public class UpdateService
    {
        public UpdateService(AppVersionManager appVersionManager)
        {
            this.VersionManager = appVersionManager;
        }

        public AppVersionManager VersionManager { get; }

        /// <summary>
        /// 更新过程进度, 参数：当前操作 
        /// </summary>
        public event Action<string> UpdateProgessHandler;

        public bool NeedUpdate(string mainAppExecutablePath)
        {
            if (string.IsNullOrWhiteSpace(mainAppExecutablePath))
                throw new Exception("工作目录为空");

            if (!File.Exists(mainAppExecutablePath))
                throw new Exception("自动更新程序启动参数错误，主程序["+ mainAppExecutablePath + "]不存在");

            return VersionManager.NeedUpdate();
        }

        public void RunUpdate(string mainAppExecutablePath)
        {
            UpdateProgessHandler?.Invoke("开始下载");
            var workDirectory = Path.GetDirectoryName(mainAppExecutablePath);
            BeginUpdate(workDirectory);
            UpdateProgessHandler?.Invoke("更新完毕");
            //ExistCurrentAppAndStartMain(mainAppPath);
        }

        private void BeginUpdate(string workDirectory)
        {
            try
            {
                // 1. 下载文件
                var downFile = Download();
                // 2. 解压更新包文件
                var tempDirectory = UnZipUpdatePackage(downFile);
                // 3. 替换更新文件
                ReplaceAndUpdate(tempDirectory, workDirectory);
                // 4. 更新本地的版本号
                VersionManager.UpdateClientVersion();
                // 5.删除临时文件
                DelTempFile(downFile, tempDirectory);
            }
            catch(Exception ex)
            {
                var log = $"自动更新程序异常/r/n{ex}";
                MessageBox.Show(log, @"自动更新程序异常提示", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void DelTempFile(string downFile, string tempDirectory)
        {
            File.Delete(downFile);
            DirectoryUtil.Delete(tempDirectory);
        }

        /// <summary>
        /// 下载文件
        /// </summary>
        /// <returns>下载文件的名称</returns>
        private string Download()
        {
            var fileName = VersionManager.ServerVersion.GetVersion() + ".zip";
            if (File.Exists(fileName))
                File.Delete(fileName);

            var downUrl = Path.Combine(VersionManager.ClientVersion.GetRemoteUrl(), fileName);
            var httpWebRequest = WebRequest.Create(downUrl) as HttpWebRequest;
            if (httpWebRequest != null)
            {
                httpWebRequest.Method = "get";
                var httpWebResponse = httpWebRequest.GetResponse() as HttpWebResponse;
                if (httpWebResponse != null)
                {
                    var webStream = httpWebResponse.GetResponseStream();

                    var downFileSteam = new FileStream(fileName, FileMode.Create);
                    var buffers = new byte[10240];
                    int read;
                    long totalReadSize = 0;
                    var totalSize = httpWebResponse.ContentLength;
                    while (webStream != null && (read = webStream.Read(buffers, 0, buffers.Length)) > 0)
                    {
                        downFileSteam.Write(buffers, 0, read);
                        totalReadSize += read;
                        UpdateProgessHandler?.Invoke("下载中，" + (totalReadSize * 100 / totalSize) + "%...");
                    }

                    downFileSteam.Flush();
                    downFileSteam.Close();

                    httpWebResponse.Close();
                }
            }

            UpdateProgessHandler?.Invoke("下载完毕，开始解压");

            return fileName;
        }

        /// <summary>
        /// 解压更新包
        /// </summary>
        /// <param name="downFileName"></param>
        /// <returns>解压之后的临时目录</returns>
        private string UnZipUpdatePackage(string downFileName)
        {
            UpdateProgessHandler?.Invoke("正在解压更新包...");
            var unZipPath = Path.Combine(Application.StartupPath, "tmp");
            
            if (!ZipUtil.Decompression(downFileName, unZipPath))
                throw new Exception("解压失败");

            UpdateProgessHandler?.Invoke("解压完成，开始更新");
            return unZipPath;
        }

        private void ReplaceAndUpdate(string tempDirectory, string workDirectory)
        {
            UpdateProgessHandler?.Invoke("更新中...");
            DirectoryUtil.Copy(tempDirectory, workDirectory);
            UpdateProgessHandler?.Invoke("更新完成，开始启动程序");
        }

        public void ExistCurrentAppAndStartMain(string mainAppPath)
        {
            Process.Start(mainAppPath);

            Application.Exit();
        }
    }
}
