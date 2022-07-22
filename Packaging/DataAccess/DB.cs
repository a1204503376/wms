using System;
using System.Configuration;
using System.Diagnostics;
using FreeSql;
using FreeSql.Aop;
using FreeSql.Internal;

namespace DataAccess
{
    public class Db
    {
        private static readonly Lazy<IFreeSql> MySqlLazy = new Lazy<IFreeSql>(() =>
        {
            var freeSql = new FreeSqlBuilder()
                .UseConnectionString(DataType.SqlServer,
                    ConfigurationManager.ConnectionStrings["MySqlConnectionString"].ConnectionString,
                    typeof(FreeSql.MySql.MySqlProvider<>))
                .UseAutoSyncStructure(Convert.ToBoolean(ConfigurationManager.AppSettings["UseAutoSyncStructure"]))
                .UseNameConvert(NameConvertType.PascalCaseToUnderscoreWithLower)
                .UseMonitorCommand(
                    //监听SQL命令对象，在执行前
                    cmd =>
                    {
                        Trace.WriteLine(cmd.CommandText);
                        // LoggerHelper.Info(cmd.CommandText);
                    },
                    //监听SQL命令对象，在执行后
                    (cmd, traceLog) =>
                    {
                        Console.WriteLine(traceLog);
                    })
                .UseLazyLoading(false)
                .Build();

            // freeSql.SetDbContextOptions(opt => opt.EnableAddOrUpdateNavigateList = true);

            //https://github.com/dotnetcore/FreeSql/wiki/%e8%bf%87%e6%bb%a4%e5%99%a8
            // freeSql.GlobalFilter
            //     .Apply<IDeleteAudited>("BaseEntityDelFlag", d => d.DelFlag == false);

            freeSql.Aop.AuditValue += (s, e) =>
            {
                switch (e.AuditValueType)
                {
                    case AuditValueType.Insert:
                        switch (e.Property.Name)
                        {
                            case "CreateTime" when e.Value == null:
                                e.Value = DateTime.Now;
                                break;
                        }
                        break;
                    case AuditValueType.Update:
                        {
                            switch (e.Property.Name)
                            {
                                case "UpdateTime":
                                    e.Value = DateTime.Now;
                                    break;
                            }
                            break;
                        }
                    case AuditValueType.InsertOrUpdate:
                        break;
                    default:
                        throw new ArgumentOutOfRangeException();
                }
            };
            return freeSql;
        });

        public static IFreeSql FreeSql => MySqlLazy.Value;

    }
}
