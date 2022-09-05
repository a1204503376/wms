using System.Collections.Generic;
using System.Linq;
using DataAccess.Enitiies;

namespace DataAccess.Wms
{
    public class BladeDictDal
    {
        private static List<string> GetDictValueList(ICollection<string> codesList)
        {
            return Db.FreeSql.Select<BladeDict>()
                .Where(d => codesList.Contains(d.Code) && d.DictKey > 0 && d.IsSealed == 0)
                .ToList(d => d.DictValue);
        }

        private static List<string> GetDictValueList(string code)
        {
            return GetDictValueList(new List<string>
            {
                code
            });
        }

        /// <summary>
        /// 序列号成对对应的型号
        /// </summary>
        /// <returns></returns>
        public static List<string> GetPackingSequenctNumberPairs()
        {
            return GetDictValueList("packing_sequence_number_pairs");
        }

        /// <summary>
        /// 序列号成对对应的型号
        /// </summary>
        /// <returns></returns>
        public static Dictionary<string, List<string>> GetSkuSpecList(List<string> codesList)
        {
            var bladeDicts = Db.FreeSql.Select<BladeDict>()
                .Where(d => codesList.Contains(d.Code) && d.DictKey > 0 && d.IsSealed == 0)
                .ToList(d=>new BladeDict()
                {
                    Code = d.Code,
                    DictValue = d.DictValue
                });

            return bladeDicts.GroupBy(d => d.Code)
                .ToDictionary(d => d.Key, d => d.Select(m => m.DictValue).ToList());
        }
    }
}