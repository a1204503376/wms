using System;
using System.Collections;
using System.ComponentModel;
using System.IO;
using System.Net;
using System.Windows.Forms;

namespace Packaging.Utility
{
	public class AppUpdater : IDisposable
	{
		#region 成员与字段属性
		private string _updaterUrl;
		private bool _disposed = false;
		private IntPtr _handle;
		private readonly Component _component = new Component();
		[System.Runtime.InteropServices.DllImport("Kernel32")]
		private extern static Boolean CloseHandle(IntPtr handle);


		public string UpdaterUrl
		{
			set { _updaterUrl = value; }
			get { return this._updaterUrl; }
		}
		#endregion

		/// <summary>
		/// AppUpdater构造函数
		/// </summary>
		public AppUpdater()
		{
		}
		public void Dispose()
		{
			Dispose(true);
			GC.SuppressFinalize(this);
		}
		private void Dispose(bool disposing)
		{
			if (!this._disposed)
			{
				if (disposing)
				{

					_component.Dispose();
				}
				CloseHandle(_handle);
				_handle = IntPtr.Zero;
			}
			_disposed = true;
		}

		~AppUpdater()
		{
			Dispose(false);
		}


		/// <summary>
		/// 检查更新文件
		/// </summary>
		/// <param name="serverXmlFile"></param>
		/// <param name="localXmlFile"></param>
		/// <param name="updateFileList"></param>
		/// <returns></returns>
		public int CheckForUpdate(string serverXmlFile, string localXmlFile, out Hashtable updateFileList)
		{
			updateFileList = new Hashtable();
			if (!File.Exists(localXmlFile) || !File.Exists(serverXmlFile))
			{
				return -1;
			}

			var serverXmlFiles = new XmlFiles(serverXmlFile);
			var localXmlFiles = new XmlFiles(localXmlFile);

			var newNodeList = serverXmlFiles.GetNodeList("Manifest/Files");
			var oldNodeList = localXmlFiles.GetNodeList("Manifest/Files");

			var k = 0;
			for (var i = 0; i < newNodeList.Count; i++)
			{
				var fileList = new string[3];

				var newFileName = newNodeList.Item(i).Attributes["Name"].Value.Trim();
				var newVer = newNodeList.Item(i).Attributes["Ver"].Value.Trim();

				var oldFileAl = new ArrayList();
				for (var j = 0; j < oldNodeList.Count; j++)
				{
					var oldFileName = oldNodeList.Item(j).Attributes["Name"].Value.Trim();
					var oldVer = oldNodeList.Item(j).Attributes["Ver"].Value.Trim();

					oldFileAl.Add(oldFileName);
					oldFileAl.Add(oldVer);

				}
				var pos = oldFileAl.IndexOf(newFileName);
				if (pos == -1)
				{
					fileList[0] = newFileName;
					fileList[1] = newVer;
					updateFileList.Add(k, fileList);
					k++;
				}
				else if (pos > -1 && newVer.CompareTo(oldFileAl[pos + 1].ToString()) > 0)
				{
					fileList[0] = newFileName;
					fileList[1] = newVer;
					updateFileList.Add(k, fileList);
					k++;
				}

			}
			return k;
		}

		/// <summary>
		/// 检查更新文件
		/// </summary>
		/// <param name="serverXmlFile"></param>
		/// <param name="localXmlFile"></param>
		/// <param name="updateFileList"></param>
		/// <returns></returns>
		public int CheckForUpdate()
		{
			var localXmlFile = Application.StartupPath + "\\manifest.xml";
			if (!File.Exists(localXmlFile))
			{
				return -1;
			}

			var updaterXmlFiles = new XmlFiles(localXmlFile);


			var tempUpdatePath = Environment.GetEnvironmentVariable("Temp") + "\\" + "_" + updaterXmlFiles.FindNode("//Application").Attributes["applicationId"].Value + "\\";
			this.UpdaterUrl = updaterXmlFiles.GetNodeValue("//Url") + "/manifest.xml";
			this.DownAutoUpdateFile(tempUpdatePath);

			var serverXmlFile = tempUpdatePath + "\\manifest.xml";
			if (!File.Exists(serverXmlFile))
			{
				return -1;
			}

			var serverXmlFiles = new XmlFiles(serverXmlFile);
			var localXmlFiles = new XmlFiles(localXmlFile);

			var newNodeList = serverXmlFiles.GetNodeList("Manifest/Files");
			var oldNodeList = localXmlFiles.GetNodeList("Manifest/Files");

			var k = 0;
			for (var i = 0; i < newNodeList.Count; i++)
			{
				var fileList = new string[3];

				var newFileName = newNodeList.Item(i).Attributes["Name"].Value.Trim();
				var newVer = newNodeList.Item(i).Attributes["Ver"].Value.Trim();

				var oldFileAl = new ArrayList();
				for (var j = 0; j < oldNodeList.Count; j++)
				{
					var oldFileName = oldNodeList.Item(j).Attributes["Name"].Value.Trim();
					var oldVer = oldNodeList.Item(j).Attributes["Ver"].Value.Trim();

					oldFileAl.Add(oldFileName);
					oldFileAl.Add(oldVer);

				}
				var pos = oldFileAl.IndexOf(newFileName);
				if (pos == -1)
				{
					fileList[0] = newFileName;
					fileList[1] = newVer;
					k++;
				}
				else if (pos > -1 && newVer.CompareTo(oldFileAl[pos + 1].ToString()) > 0)
				{
					fileList[0] = newFileName;
					fileList[1] = newVer;
					k++;
				}

			}
			return k;
		}
		/// <summary>
		/// 返回下载更新文件的临时目录
		/// </summary>
		/// <returns></returns>
		public void DownAutoUpdateFile(string downpath)
		{
			if (!System.IO.Directory.Exists(downpath))
				System.IO.Directory.CreateDirectory(downpath);
			var serverXmlFile = downpath + @"/manifest.xml";

			var req = WebRequest.Create(this.UpdaterUrl);
			var res = req.GetResponse();
			if (res.ContentLength > 0)
			{
				try
				{
					var wClient = new WebClient();
					wClient.DownloadFile(this.UpdaterUrl, serverXmlFile);
				}
				catch
				{
					return;
				}
			}
		}
	}
}
