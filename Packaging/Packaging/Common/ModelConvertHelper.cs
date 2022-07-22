using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Reflection;

namespace Packaging.Common
{
    /// <summary>    
    /// 实体转换辅助类    
    /// </summary>    
    public class ModelConvertHelper<T> where T : new()
    {
        public static List<T> ConvertToModel(DataTable dt)
        {
            // 定义集合    
            List<T> ts = new List<T>();

            foreach (DataRow dr in dt.Rows)
            {
                var t = new T();
                // 获得此模型的公共属性      
                var propertys = t.GetType().GetProperties();
                foreach (var pi in propertys)
                {
                    var tempName = pi.Name;
                    var displayNameAttribute = pi.GetCustomAttribute<DisplayNameAttribute>();
                    if (displayNameAttribute!=null)
                    {
                        tempName = displayNameAttribute.DisplayName;
                    }

                    if (!dt.Columns.Contains(tempName)) continue;

                    // 判断此属性是否有Setter      
                    if (!pi.CanWrite) continue;

                    var value = dr[tempName];
                    if (value == DBNull.Value)
                    {
                        continue;
                    }

                    if (pi.PropertyType.IsGenericType)
                    {
                        var genericTypeDefinition = pi.PropertyType.GetGenericTypeDefinition();
                        if (genericTypeDefinition != typeof(Nullable<>)) continue;
                        if (value!=null&&string.IsNullOrEmpty(value.ToString()))
                        {
                            value = Convert.ChangeType(value,
                                Nullable.GetUnderlyingType(pi.PropertyType)
                                ?? throw new InvalidOperationException());

                        }
                        pi.SetValue(t, value, null);
                    }
                    else
                    {
                        pi.SetValue(t, 
                            Convert.ChangeType(value, pi.PropertyType), null);
                    }
                    
                }
                ts.Add(t);
            }
            return ts;
        }
    }
}