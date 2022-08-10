using Ionic.Zip;
using System;
using System.Linq;
using System.Text;

namespace TUpdater.Utils
{
    internal static class ZipUtil
    {
        /// <summary>
        /// 解压ZIP文件
        /// </summary>
        /// <param name="strZipPath">待解压的ZIP文件</param>
        /// <param name="strUnZipPath">解压的目录,为空时解压到当前目录下同名文件夹内</param>
        /// <param name="overWrite">是否覆盖</param>
        /// <returns>成功：true/失败：false</returns>
        public static bool Decompression(string strZipPath, string strUnZipPath, bool overWrite = true)
        {
            try
            {
                if (string.IsNullOrEmpty(strUnZipPath))
                {
                    strUnZipPath = strZipPath.Split('.').First();
                }

                var options = new ReadOptions
                {
                    Encoding = Encoding.Default //设置编码，解决解压文件时中文乱码
                };
                using (var zip = ZipFile.Read(strZipPath, options))
                {
                    foreach (var entry in zip)
                    {
                        entry.Extract(strUnZipPath,
                            overWrite
                                ? ExtractExistingFileAction.OverwriteSilently
                                : ExtractExistingFileAction.DoNotOverwrite);
                    }
                    return true;
                }
            }
            catch (Exception)
            {
                return false;
            }
        }
    }
}
