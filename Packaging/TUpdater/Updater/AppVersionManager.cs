namespace TUpdater.Updater
{
    public class AppVersionManager
    {
        readonly VersionOfClient clientVersion;
        readonly VersionOfServer serverVersion;

        public AppVersionManager()
        {
            clientVersion = new VersionOfClient();
            serverVersion = new VersionOfServer(clientVersion);
        }

        public VersionOfClient ClientVersion
        {
            get { return clientVersion; }
        }

        public VersionOfServer ServerVersion
        {
            get { return serverVersion; }
        }

        public bool NeedUpdate()
        {
            string clientVersionInfo = clientVersion.GetVersion();
            string remoteVersionInfo = serverVersion.GetVersion();
            return clientVersionInfo != remoteVersionInfo;
        }

        public void UpdateClientVersion()
        {
            string remoteVersion = serverVersion.GetVersion();
            clientVersion.UpdateLocalClientVersion(remoteVersion);
        }
    }
}
