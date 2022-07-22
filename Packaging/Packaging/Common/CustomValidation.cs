using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using DevExpress.XtraEditors;
using DevExpress.XtraEditors.DXErrorProvider;

namespace Packaging.Common
{
    /// <summary>
    /// 控件验证
    /// </summary>
    public class CustomValidation
    {
        /// <summary>
        /// DXValidationProvider
        /// </summary>
        private readonly DXValidationProvider _provider = new DXValidationProvider();
        /// <summary>
        /// 条件列表
        /// </summary>
        public List<ControlRule> RuleList { get; set; }
        /// <summary>
        /// 构造函数
        /// </summary>
        public CustomValidation()
        {
            _provider.ValidationMode = ValidationMode.Manual;
        }
        /// <summary>
        /// 执行验证
        /// </summary>
        /// <returns></returns>
        public bool Validate()
        {
            bool flag;
            if (RuleList != null && _provider != null)
            {
                foreach (var item in RuleList)
                {
                    _provider.SetIconAlignment(item.Control, ErrorIconAlignment.MiddleRight);
                    _provider.SetValidationRule(item.Control, item.Rule);
                }
                flag = _provider.Validate();
            }
            else
            {
                flag = false;
            }
            return flag;
        }
    }
    /// <summary>
    /// 验证列表
    /// </summary>
    public class ControlRule
    {
        /// <summary>
        /// 验证的控件
        /// </summary>
        public Control Control;
        /// <summary>
        /// 验证的规则
        /// </summary>
        public ValidationRuleBase Rule;
        /// <summary>
        /// 验证字符长度
        /// </summary>
        public bool IsMaxLength = false;
        /// <summary>
        /// 构造函数
        /// </summary>
        /// <param name="control"></param>
        /// <param name="rule"></param>
        public ControlRule(Control control, ValidationRuleBase rule)
        {
            this.Control = control;
            this.Rule = rule;
        }
        /// <summary>
        /// 判断控件值是否为空
        /// </summary>
        /// <param name="errorMsg">错误消息</param>
        /// <returns></returns>
        public static ConditionValidationRule NotEmpty(string errorMsg = "不允许为空")
        {
            var rule = new ConditionValidationRule
            {
                ConditionOperator = ConditionOperator.IsNotBlank,
                ErrorText = errorMsg
            };
            return rule;
        }
        /// <summary>
        /// 判断控件值是否为空
        /// </summary>
        /// <param name="errorMsg">错误消息</param>
        /// <returns></returns>
        public static ValidationRuleBase NotEmptyList(string errorMsg)
        {
            var rule = new CustomValidationCheckedComboBoxEdit(errorMsg);
            return rule;
        }
        /// <summary>
        /// 判断是否大于等于某个值
        /// </summary>
        /// <param name="errorMsg">错误消息(必须包含'{0}')</param>
        /// <param name="obj">值</param>
        public static ConditionValidationRule GreaterOrEqual(string errorMsg, object obj)
        {
            var rule = new ConditionValidationRule
            {
                ConditionOperator = ConditionOperator.GreaterOrEqual,
                ErrorText = string.Format(errorMsg, obj),
                Value1 = obj
            };
            return rule;
        }
        /// <summary>
        /// 判断是否大于某个值
        /// </summary>
        /// <param name="errorMsg">错误消息(必须包含'{0}')</param>
        /// <param name="obj">值</param>
        public static ConditionValidationRule Greater(string errorMsg, object obj)
        {
            var rule = new ConditionValidationRule
            {
                ConditionOperator = ConditionOperator.Greater,
                ErrorText = string.Format(errorMsg, obj),
                Value1 = obj
            };
            return rule;
        }

        /// <summary>
        /// 判断是否在指定值（包含）之间
        /// </summary>
        /// <param name="errorMsg"></param>
        /// <param name="small">小数</param>
        /// <param name="big">大数</param>
        /// <returns></returns>
        public static ConditionValidationRule Between(string errorMsg, object small, object big)
        {
            var rule = new ConditionValidationRule
            {
                ConditionOperator = ConditionOperator.Between,
                ErrorText = string.Format(errorMsg, small, big),
                Value1 = small,
                Value2 = big
            };
            return rule;
        }
        /// <summary>
        ///  调用正则表达式判断输入格式
        /// </summary>
        /// <param name="errorMsg">未匹配到表达式时的错误消息</param>
        /// <param name="strRex">正则表达式</param>
        /// <param name="isnotblank">是否允许空值</param>
        /// <param name="blankMsg">空值错误消息（如允许空值,传string.Empty）</param>
        /// <returns></returns>
        private static CustomRuleByRegex ValueRex(string errorMsg, string strRex, bool isnotblank, string blankMsg = "")
        {
            var rule = new CustomRuleByRegex(strRex, isnotblank, blankMsg, false)
            {
                ErrorText = errorMsg
            };
            return rule;
        }
        public static CustomRuleByRegex IsPositiveInteger()
        {
            return ValueRex("只允许正整数", CustomRuleByRegex.PositiveInteger, false);
        }
        public static CustomRuleByRegex IsPositiveIntegerOne()
        {
            return ValueRex("只允许一位正整数", CustomRuleByRegex.PositiveIntegerOne, false);
        }
        public static CustomRuleByRegex IsNumericTwo()
        {
            return ValueRex("只允许数字，小数保留2位", CustomRuleByRegex.DicmalTwo, true);
        }
        public static CustomRuleByRegex IsIpPort()
        {
            return ValueRex("请输入[0-65535]区间内的端口号", CustomRuleByRegex.IpPort, false);
        }

        public static CustomRuleByRegex IsIpAddress()
        {
            return ValueRex("请输入正确的IP地址格式", CustomRuleByRegex.IpAddress, false);
        }
        /// <summary>
        /// 判断是否等于某个值
        /// </summary>
        /// <param name="errorMsg">错误消息</param>
        /// <param name="obj">值</param>
        public static ConditionValidationRule ValueEquals(string errorMsg, object obj)
        {
            var rule = new ConditionValidationRule
            {
                ConditionOperator = ConditionOperator.Equals,
                ErrorText = errorMsg,
                Value1 = obj
            };
            return rule;
        }
        /// <summary>
        ///  调用正则表达式验证字符长度
        /// </summary>
        /// <param name="length">正则表达式</param>
        /// <param name="isnotblank">是否允许空值</param>
        /// <param name="blankMsg">空值错误消息（如允许空值,传string.Empty）</param>
        /// <returns></returns>
        public static CustomRuleByRegex MaxLengthRexAnd(int length, bool isnotblank, string blankMsg)
        {
            var strRex = @"^.{1," + length + "}$";
            var rule = new CustomRuleByRegex(strRex, isnotblank, blankMsg, true)
            {
                ErrorText = "长度不能大于" + length.ToString() + "位，当前输入{0}位!"
            };
            return rule;
        }
        #region "获取字符长度（中文+3）"
        /// <summary>
        /// 获取字符长度（中文+3）
        /// </summary>
        /// <param name="str"></param>
        /// <returns></returns>
        public static int GetLength(string str)
        {
            if (str.Length == 0)
                return 0;
            var ascii = new ASCIIEncoding();
            var tempLen = 0;
            var s = ascii.GetBytes(str);
            for (var i = 0; i < s.Length; i++)
            {
                if ((int)s[i] == 63)
                {
                    tempLen += 3;
                }
                else
                {
                    tempLen += 1;
                }
            }
            return tempLen;
        }
        #endregion
    }
    /// <summary>
    /// 正则表达式验证
    /// </summary>
    public class CustomRuleByRegex : ValidationRule
    {
        private readonly string _regex;// 表达式
        private readonly bool _isnotblank;// 是否为空
        private readonly string _blankMsg;// 空值错误消息
        private readonly bool _isMaxLength;
        /// <summary>
        /// 是否为数字
        /// </summary>
        public static string IsNumeric = "^[\\+\\-]?[0-9]*\\.?[0-9]+$";
        /// <summary>
        /// 正整数
        /// </summary>
        public static string PositiveInteger = @"^[1-9]\d*$";
        /// <summary>
        /// 一位正整数
        /// </summary>
        public static string PositiveIntegerOne = @"^[0-9]{1}$";
        public static string IpAddress =
            @"^(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)$";

        public static string IpPort = @"^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$";
        public static string DicmalTwo = "^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,2})?$";
        /// <summary>
        /// 电话号码
        /// </summary>
        public static string IsPhone = @"(^(\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$|(1(([35][0-9])|(47)|[8][01236789]))\d{8}$";
        /// <summary>
        /// Emal
        /// </summary>
        public static string IsEmail = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        /// <summary>
        /// 非中文
        /// </summary>
        public static string IsNonChinese = @"^[^\u4e00-\u9fa5]+$";

        /// <summary>
        /// 使用正则表达式验证
        /// </summary>
        /// <param name="regex">正则表达式</param>
        /// <param name="isnotblank">是否允许为空</param>
        /// <param name="blankMsg">空值错误消息</param>
        /// <param name="isMaxLength"></param>
        public CustomRuleByRegex(string regex, bool isnotblank, string blankMsg, bool isMaxLength)
        {
            this._regex = regex;
            this._isnotblank = isnotblank;
            this._blankMsg = string.IsNullOrWhiteSpace(blankMsg) ? "不允许为空" : blankMsg;
            this._isMaxLength = isMaxLength;
        }
        /// <summary>
        /// 验证正则表达式
        /// </summary>
        /// <param name="control"></param>
        /// <param name="value"></param>
        /// <returns></returns>
        public override bool Validate(Control control, object value)
        {
            bool flag;
            try
            {
                if (value == null || value.ToString().Trim() == string.Empty)
                {
                    if (_isnotblank)
                    {
                        flag = true;
                    }
                    else
                    {
                        flag = false;
                        this.ErrorText = _blankMsg;
                    }
                }
                else
                {
                    if (_isMaxLength)
                    {
                        var valueLength = ControlRule.GetLength(value.ToString());
                        var s = "a";
                        value = s.PadLeft(valueLength, '0');
                        this.ErrorText = string.Format(this.ErrorText, valueLength.ToString());
                    }
                    flag = Regex.IsMatch(value.ToString(), _regex);
                }
            }
            catch (Exception ex)
            {
                this.ErrorText = ex.Message;
                flag = false;
            }
            return flag;
        }
    }

    public class CustomValidationCode : ValidationRule
    {
        private readonly Guid _id;
        private readonly Func<string, Guid, bool> _func;

        public CustomValidationCode(Guid id, Func<string, Guid, bool> predicate)
        {
            this._id = id;
            this._func = predicate;
        }

        public override bool Validate(Control control, object value)
        {
            if (!(control is TextEdit textEdit) || textEdit.Name != "txtCode")
            {
                return false;
            }

            if (value == null || string.IsNullOrWhiteSpace(value.ToString()))
            {
                ErrorText = "不允许为空";
                return false;
            }

            ErrorText = "该编码已存在";
            return !_func(value.ToString(), _id);
        }
    }
}