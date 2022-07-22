using System;
using System.Collections.Generic;
using System.Xml;

namespace Packaging.Utility
{
    public class XmlFiles : XmlDocument
    {
        #region 字段与属性
        private string _xmlFileName;
        public string XmlFileName
        {
            set { _xmlFileName = value; }
            get { return _xmlFileName; }
        }
        #endregion

        public XmlFiles(string xmlFile)
        {
            XmlFileName = xmlFile;

            this.Load(xmlFile);
        }

        public XmlFiles()
        {
        }
        /// <summary>
        /// 给定一个节点的xPath表达式并返回一个节点
        /// </summary>
        /// <param name="node"></param>
        /// <returns></returns>
        public XmlNode FindNode(string xPath)
        {
            var xmlNode = this.SelectSingleNode(xPath);
            return xmlNode;
        }
        /// <summary>
        /// 给定一个节点的xPath表达式返回其值
        /// </summary>
        /// <param name="xPath"></param>
        /// <returns></returns>
        public string GetNodeValue(string xPath)
        {
            var xmlNode = this.SelectSingleNode(xPath);
            return xmlNode.InnerText;
        }
        /// <summary>
        /// 给定一个节点的表达式返回此节点下的孩子节点列表
        /// </summary>
        /// <param name="xPath"></param>
        /// <returns></returns>
        public XmlNodeList GetNodeList(string xPath)
        {
            var nodeList = this.SelectSingleNode(xPath).ChildNodes;
            return nodeList;

        }

        #region 管理配置文件中的灌装头配置节点

        private readonly string _configPath = AppDomain.CurrentDomain.BaseDirectory + @"config.xml";
        private readonly string _configuration = "configuration";
        private readonly string _ipPorts = "IpPorts";
        private readonly string _childIpPort = "IpPort";

        /// <summary>
        /// 检查config文件是否有IpPorts节点
        /// </summary>
        public void CheckConfig(XmlDocument xml)
        {
            XmlNode root = xml[_configuration];
            var xmlNode = root.SelectSingleNode(_ipPorts);
            if (xmlNode == null)
            {
                var xe1 = xml.CreateElement(_ipPorts);
                root.AppendChild(xe1);
                xml.Save(_configPath);
            }
        }

        /// <summary>
        /// 加载IpPorts节点下的所有数据
        /// </summary>
        /// <returns></returns>
        public List<IpPort> LoadconfigData()
        {
            var xml = new XmlDocument();
            xml.Load(_configPath);

            CheckConfig(xml);

            var listIp = new List<IpPort>();

            XmlNode root = xml[_configuration][_ipPorts];
            foreach (XmlElement cNodes in root.ChildNodes)
            {
                var ip = cNodes.GetAttribute("IP").ToString();
                var port = cNodes.GetAttribute("Port").ToString();
                var id = cNodes.GetAttribute("ID").ToString();
                var weight = Convert.ToDecimal(cNodes.GetAttribute("Weight"));

                var ipPort = new IpPort(ip, port, id, weight);
                listIp.Add(ipPort);
            }
            return listIp;
        }

        /// <summary>
        /// 创建子节点
        /// </summary>
        /// <param name="ipPort"></param>
        /// <returns></returns>
        public int CreatChildNodes(IpPort ipPort)
        {
            var i = 0;
            var xml = new XmlDocument();
            xml.Load(_configPath);

            XmlNode root = xml[_configuration][_ipPorts];

            foreach (XmlElement cNodes in root.ChildNodes)
            {
                var id = cNodes.GetAttribute("ID").ToString();
                if (ipPort.Id == id)
                {
                    i = -1;
                    break;
                }
            }
            if (i == 0)
            {
                var xe1 = xml.CreateElement(_childIpPort);
                xe1.SetAttribute("ID", ipPort.Id);
                xe1.SetAttribute("IP", ipPort.Ip);
                xe1.SetAttribute("Port", ipPort.Port);
                xe1.SetAttribute("Weight", ipPort.Weight.ToString());
                root.AppendChild(xe1);
                xml.Save(_configPath);
            }
            return i;
        }

        /// <summary>
        /// 编辑子节点
        /// </summary>
        /// <param name="ipPort"></param>
        /// <returns></returns>
        public int EditChildNodes(IpPort ipPort)
        {
            var i = -1;
            var xml = new XmlDocument();
            xml.Load(_configPath);

            XmlNode root = xml[_configuration][_ipPorts];

            foreach (XmlElement cNodes in root.ChildNodes)
            {
                var id = cNodes.GetAttribute("ID").ToString();
                if (ipPort.Id == id)
                {
                    i = 0;
                    cNodes.SetAttribute("IP", ipPort.Ip);
                    cNodes.SetAttribute("Port", ipPort.Port);
                    cNodes.SetAttribute("Weight", ipPort.Weight.ToString());
                    xml.Save(_configPath);
                    break;
                }
            }
            return i;
        }

        /// <summary>
        /// 删除子节点
        /// </summary>
        /// <param name="ipPort"></param>
        /// <returns></returns>
        public int DeleteChildNodes(IpPort ipPort)
        {
            var i = -1;
            var xml = new XmlDocument();
            xml.Load(_configPath);

            XmlNode root = xml[_configuration][_ipPorts];

            foreach (XmlElement cNodes in root.ChildNodes)
            {
                var id = cNodes.GetAttribute("ID").ToString();
                if (ipPort.Id == id)
                {
                    i = 0;
                    root.RemoveChild(cNodes);
                    xml.Save(_configPath);
                    break;
                }
            }
            //如果子节点全部删除，则删除父节点
            XmlNode rootAlfer = xml[_configuration][_ipPorts];
            if (root.ChildNodes.Count <= 0)
            {
                XmlNode configurationroot = xml[_configuration];
                configurationroot.RemoveChild(rootAlfer);
                xml.Save(_configPath);
            }

            return i;
        }
        #endregion
    }

    public class IpPort
    {
        public IpPort()
        {
        }

        public IpPort(string ip, string port, string id, decimal weight)
        {
            this.Ip = ip;
            this.Port = port;
            this.Weight = weight;
            this.Id = id;
        }
        public IpPort(string ip, string port, int ioNumber, string sendData)
        {
            this.Ip = ip;
            this.Port = port;
            this.SendData = sendData;
            this.IoNumber = ioNumber;
        }

        public IpPort(string ip, string port, string connectType, string sendData)
        {
            this.Ip = ip;
            this.Port = port;
            this.SendData = sendData;
            this.ConnectType = connectType;
        }

        public IpPort(string ip, string port)
        {
            this.Ip = ip;
            this.Port = port;
        }

        public IpPort(string ip, int ioNumber, string connectType, string sendData)
        {
            this.Ip = ip;
            this.IoNumber = ioNumber;
            this.SendData = sendData;
            this.ConnectType = connectType;
        }

        public IpPort(string ip, string port, string id, decimal weight, string connectType, int ioNumber, int intervalTime)
        {
            this.Ip = ip;
            this.Port = port;
            this.Weight = weight;
            this.Id = id;
            this.ConnectType = connectType;
            this.IoNumber = ioNumber;
            this.IntervalTime = intervalTime;
        }


        /// <summary>
        /// IP地址
        /// </summary>
        public string Ip { get; set; }

        /// <summary>
        /// 端口号
        /// </summary>
        public string Port { get; set; }

        /// <summary>
        /// 重量
        /// </summary>
        public decimal Weight { get; set; }

        /// <summary>
        /// 设备编号
        /// </summary>
        public string Id { get; set; }

        /// <summary>
        /// 类型
        /// </summary>
        public string ConnectType { get; set; }

        /// <summary>
        /// IO号
        /// </summary>
        public int IoNumber { get; set; }

        /// <summary>
        /// 检测时间
        /// </summary>
        public int IntervalTime { get; set; }

        /// <summary>
        /// 发送的数据
        /// </summary>
        public string SendData { get; set; }

        /// <summary>
        /// 客户端IP
        /// </summary>
        public string ClientIp { get; set; }

        /// <summary>
        /// 接收的数据
        /// </summary>
        public string ReceiveData { get; set; }
    }
}
