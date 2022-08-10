using System.IO;

namespace TUpdater.Utils
{
    internal static class DirectoryUtil
    {
        public static void Copy(string sourceDirectory, string targetDirectory)
        {
            var files = Directory.GetFiles(sourceDirectory);
            var directorys = Directory.GetDirectories(sourceDirectory);

            if (!Directory.Exists(targetDirectory))
                Directory.CreateDirectory(targetDirectory);

            foreach (var item in files)
            {
                var targetFile = Path.Combine(targetDirectory, Path.GetFileName(item));
                File.Copy(item, targetFile, true);
            }

            foreach (var item in directorys)
            {
                var targetSubDirectory = Path.Combine(targetDirectory, Path.GetFileName(item));
                Copy(item, targetSubDirectory);
            }
        }

        public static void Delete(string targetDirectory, bool recursive = true)
        {
            if (!Directory.Exists(targetDirectory))
                return;

            //去除文件夹和子文件的只读属性
            //去除文件夹的只读属性
            var dir = new DirectoryInfo(targetDirectory);
            dir.Attributes = FileAttributes.Normal & FileAttributes.Directory;
            //去除文件的只读属性
            System.IO.File.SetAttributes(targetDirectory, System.IO.FileAttributes.Normal);

            var fileinfo = dir.GetFileSystemInfos();  //返回目录中所有文件和子目录
            foreach (var i in fileinfo)
            {
                if (i is DirectoryInfo)            //判断是否文件夹
                {
                    var subdir = new DirectoryInfo(i.FullName);
                    subdir.Delete(true);          //删除子目录和文件
                }
                else
                {
                    File.Delete(i.FullName);      //删除指定文件
                }
            }

            if (recursive)
                Directory.Delete(targetDirectory);
        }
    }
}
