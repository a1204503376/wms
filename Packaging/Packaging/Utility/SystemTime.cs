using System;
using System.Runtime.InteropServices;

namespace Packaging.Utility
{
    public struct Systemtime
    {
        public ushort WYear;
        public ushort WMonth;
        public ushort WDayOfWeek;
        public ushort WDay;
        public ushort WHour;
        public ushort WMinute;
        public ushort WSecond;
        public ushort WMilliseconds;

        /// <summary>
        /// 从System.DateTime转换。
        /// </summary>
        /// <param name="time">System.DateTime类型的时间。</param>
        public void FromDateTime(DateTime time)
        {
            WYear = (ushort)time.Year;
            WMonth = (ushort)time.Month;
            WDayOfWeek = (ushort)time.DayOfWeek;
            WDay = (ushort)time.Day;
            WHour = (ushort)time.Hour;
            WMinute = (ushort)time.Minute;
            WSecond = (ushort)time.Second;
            WMilliseconds = (ushort)time.Millisecond;
        }

        /// <summary>
        /// 转换为System.DateTime类型。
        /// </summary>
        /// <returns></returns>
        public DateTime ToDateTime()
        {
            return new DateTime(WYear, WMonth, WDay, WHour, WMinute, WSecond, WMilliseconds);
        }

        /// <summary>
        /// 静态方法。转换为System.DateTime类型。
        /// </summary>
        /// <param name="time">SYSTEMTIME类型的时间。</param>
        /// <returns></returns>
        public static DateTime ToDateTime(Systemtime time)
        {
            return time.ToDateTime();
        }
    }

    public class Win32Api
    {
        [DllImport("Kernel32.dll")]
        public static extern bool SetLocalTime(ref Systemtime time);
        [DllImport("Kernel32.dll")]
        public static extern void GetLocalTime(ref Systemtime time);
    }
}
