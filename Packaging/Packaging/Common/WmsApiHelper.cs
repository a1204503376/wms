using System;
using System.Configuration;
using System.Diagnostics.CodeAnalysis;
using Flurl;
using Flurl.Http;

namespace Packaging.Common
{
    public class WmsApiHelper
    {
        private static string _userName = string.Empty;
        private static string _hashPassword = string.Empty;

        public static WmsLogin Login(string userName, string password)
        {
            try
            {
                _userName = userName;
                _hashPassword = Md5Helper.Hash(password);

                return 
                    $"{WmsUrl}/blade-auth/oauth/token"
                        .SetQueryParams(new
                        {
                            tenantId = "000000",
                            username = _userName,
                            password = _hashPassword,
                            grant_type = "password"
                        })    
                        .WithHeader("Authorization", "Basic c2FiZXI6c2FiZXJfc2VjcmV0")
                        .PostAsync()
                        .ReceiveJson<WmsLogin>()
                        .Result;
            }
            // 默认超时100秒
            catch (FlurlHttpTimeoutException timeoutException)
            {
                Serilog.Log.Fatal("登录WMS超时", timeoutException);
            }
            catch (FlurlHttpException ex)
            {
                Serilog.Log.Fatal("登录WMS异常",ex);
            }

            throw new ApplicationException("登录WMS失败");
        }

        public static string GetBoxNumber(string lpnTypeCode)
        {
            return GetBoxNumber(lpnTypeCode, string.Empty, string.Empty);
        }

        public static string GetBoxNumber(string lpnTypeCode, string skuName, string spec)
        {
            var result = $"{WmsUrl}/wms/scheduling/generateBoxCode"
                .SetQueryParams(new
                {
                    lpnTypeCode,
                    skuName,
                    spec
                })
                .GetAsync()
                .ReceiveJson()
                .Result;
            if (result.success)
            {
                return result.data;
            }

            var msg = $"获取箱号时，WMS返回失败：{result.msg}";
            Serilog.Log.Warning(msg);
            throw new Exception(msg);
        }

        private static string WmsUrl => ConfigurationManager.AppSettings["WmsUrl"];
    }

    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class WmsLogin
    {
        public string user_name { get; set; }
        public string nick_name { get; set; }
        public string access_token { get; set; }
        public string refresh_token { get; set; }
        public int expires_in { get; set; }
        public int? error_code { get; set; }
        public string error_description { get; set; }
        public long user_id { get; set; }
        public long dept_id { get; set; }
    }
}