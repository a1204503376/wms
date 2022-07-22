using System;

namespace Packaging.Settings
{
    /// <summary>
    /// 页面特性类
    /// 主菜单根据此特性打开相应界面
    /// </summary>
    [AttributeUsage(AttributeTargets.Class, AllowMultiple = false, Inherited = false)]
    public class ModuleDefineAttribute : Attribute
    {
        public int ModuleId { get; set; }

        public ModuleDefineAttribute(int moduleId)
        {
            this.ModuleId = moduleId;
        }
    }
}