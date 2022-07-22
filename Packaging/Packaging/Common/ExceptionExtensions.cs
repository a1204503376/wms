using System;

namespace Packaging.Common
{
    public static class ExceptionExtensions
    {
        /// <summary>
        /// 获取最初引发的根异常
        /// </summary>
        /// <param name="ex"></param>
        /// <returns></returns>
        public static Exception GetOriginalException(this Exception ex)
        {
            while (true)
            {
                if (ex.InnerException == null) return ex;
                ex = ex.InnerException;
            }
        }
    }
}