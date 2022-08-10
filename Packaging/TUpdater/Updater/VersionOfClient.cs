using System;
using System.IO;
using System.Xml.Linq;

namespace TUpdater.Updater
{
    public class VersionOfClient : IVersionInfo
    {
        private string _version;
        private string _appName;
        private string _remoteUrl;

        private readonly string _clientVersionFilePath;

        public VersionOfClient()
        {
            _clientVersionFilePath = "tclient.xml";
        }

        public string GetVersion()
        {
            if (string.IsNullOrWhiteSpace(_version))
                ReadFromFile();

            return _version;
        }

        private void ReadFromFile()
        {
            if (!File.Exists(_clientVersionFilePath))
                throw new Exception("找不到自动更新的配置文件，请重新安装本程序");

            var clientFile = XDocument.Load(_clientVersionFilePath);
            _version = clientFile.Element("root")?.Element("version")?.Value;
            _appName = clientFile.Element("root")?.Element("name")?.Value;
            _remoteUrl = clientFile.Element("root")?.Element("baseUrl")?.Value;

            if (string.IsNullOrWhiteSpace(_version) ||
                string.IsNullOrWhiteSpace(_appName) ||
                string.IsNullOrWhiteSpace(_remoteUrl))
                throw new Exception("本地配置文件不正确，请重新安装本程序");
        }

        public string GetRemoteVersionUrl()
        {
            if (string.IsNullOrWhiteSpace(_remoteUrl))
                ReadFromFile();

            return Path.Combine(_remoteUrl, "server.xml");
        }

        public string GetRemoteUrl()
        {
            return _remoteUrl;
        }

        public void UpdateLocalClientVersion(string version)
        {
            var clientFile = XDocument.Load(_clientVersionFilePath);
            var versionNode = clientFile.Element("root")?.Element("version");
            if (versionNode != null) versionNode.Value = version;
            clientFile.Save(_clientVersionFilePath);
        }
    }
}
