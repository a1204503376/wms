using System.Collections.Generic;
using System.Linq;
using System.Xml.Linq;

namespace TUpdater.Updater
{
    public class VersionOfServer : IVersionInfo
    {
        private readonly VersionOfClient _clientVersion;
        private readonly List<string> _updateContents;
        private string _remoteVersion;

        public VersionOfServer(VersionOfClient clientVersion)
        {
            _clientVersion = clientVersion;
            _updateContents = new List<string>();
        }

        public string GetVersion()
        {
            if (string.IsNullOrWhiteSpace(_remoteVersion))
                ReadFromWeb();

            return _remoteVersion;
        }

        private void ReadFromWeb()
        {
            var remoteVersionFileUrl = _clientVersion.GetRemoteVersionUrl();
            var version = XDocument.Load(remoteVersionFileUrl);
            _remoteVersion = version.Element("root")?.Element("version")?.Value;
            var contents = version.Element("root")?.Element("update")?.Elements("item").ToList();
            if (contents == null || !contents.Any())
            {
                return;
            }
            foreach (var item in contents)
            {
                _updateContents.Add(item.Value);
            }
        }

        public IEnumerable<string> GetUpdateContents()
        {
            return _updateContents;
        }
    }
}
