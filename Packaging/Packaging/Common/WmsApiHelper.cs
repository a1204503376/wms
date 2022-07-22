﻿using System;
using System.Configuration;
using System.Threading.Tasks;
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

        private static string WmsUrl => ConfigurationManager.AppSettings["WmsUrl"];
    }

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
    }
}