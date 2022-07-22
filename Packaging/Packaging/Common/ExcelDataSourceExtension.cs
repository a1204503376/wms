using System.ComponentModel;
using System.Data;
using System.Linq;
using DevExpress.DataAccess.Excel;

namespace Packaging.Common
{
    public static class ExcelDataSourceExtension
    {
        public static DataTable ToDataTable(this ExcelDataSource excelDataSource)
        {
            var list = ((IListSource)excelDataSource).GetList();
            var dataView = (DevExpress.DataAccess.Native.Excel.DataView)list;
            var props = dataView.Columns.ToList<PropertyDescriptor>();
            var table = new DataTable();
            foreach (var prop in props)
            {
                table.Columns.Add(prop.Name, prop.PropertyType);
            }
            var values = new object[props.Count];
            foreach (DevExpress.DataAccess.Native.Excel.ViewRow item in list)
            {
                for (var i = 0; i < values.Length; i++)
                {
                    values[i] = props[i].GetValue(item);
                }
                table.Rows.Add(values);
            }
            return table;
        }
    }
}