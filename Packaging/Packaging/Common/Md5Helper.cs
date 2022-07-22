using System;
using System.Security.Cryptography;
using System.Text;

namespace Packaging.Common
{
    public class Md5Helper
    {
        public static string Hash(string value)
        {
            var bytes = Encoding.UTF8.GetBytes(value);
            var md5 = MD5.Create();
            var computeHash = md5.ComputeHash(bytes);
            return BitConverter.ToString(computeHash).Replace("-", "").ToLower();
        }
    }
}