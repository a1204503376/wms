using System.Runtime.InteropServices;
using System;

namespace Packaging.Common
{
    /// <summary>
    /// 封装了一些Win32 
    /// </summary>
    internal class NativeMethods
    {
        public const int HwndBroadcast = 0xffff;

        public static readonly int WmShowme = RegisterWindowMessage("WM_SHOWME");

        [DllImport("user32")]
        public static extern bool PostMessage(IntPtr hwnd, int msg, IntPtr wparam, IntPtr lparam);

        [DllImport("user32")]
        private static extern int RegisterWindowMessage(string message);
    }
}