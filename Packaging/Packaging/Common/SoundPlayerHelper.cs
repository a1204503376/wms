using System.Media;
using System.Windows.Forms;

namespace Packaging.Common
{
    public class SoundPlayerHelper
    {
        private static void Play(string url)
        {
            SoundPlayer player = new SoundPlayer();
            player.SoundLocation = url;
            player.Load();
            player.Play();
        }

        /// <summary>
        /// 重复的序列号
        /// </summary>
        public static void Duplicate()
        {
            Play($"{Application.StartupPath}\\Sound\\Duplicate serial number.wav");
        }

        /// <summary>
        /// 产品标识代码不匹配
        /// </summary>
        public static void ProductIdentificationCodeNotMatch()
        {
            Play($"{Application.StartupPath}\\Sound\\product identification code not match.wav");
        }

        /// <summary>
        /// 业务校验错误
        /// </summary>
        public static void Error()
        {
            Play($"{Application.StartupPath}\\Sound\\Error.wav");
        }
    }
}