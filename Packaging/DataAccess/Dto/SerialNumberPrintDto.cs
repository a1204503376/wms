using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using DataAccess.Common;
using DataAccess.Enitiies;

namespace DataAccess.Dto
{
    public class SerialNumberPrintDto
    {
        /// <summary>
        /// 每页序列号分组数量
        /// </summary>
        public const int SerialGroupNumber = 6;
        /// <summary>
        /// 纸箱序列号默认数量
        /// </summary>

        public const int PaperSerialGroupNumber = 4;

        public string BoxType { get; set; }
        public long SkuId { get; set; }
        public string SkuCode { get; set; }
        public string SkuName { get; set; }
        public string SkuNameS { get; set; }
        public string Model { get; set; }
        public string PrintDate { get; set; } = DateTime.Now.ToString("yyMMdd");
        public string UserName { get; set; }
        public string SpeedClass { get; set; } = "";
        public decimal Qty { get; set; }
        /// <summary>
        /// 旧箱号
        /// 库内打印使用
        /// </summary>
        public string Udf2 { get; set; }
        /// <summary>
        /// 箱标重打标志
        /// </summary>
        public bool AgainPrintFlag { get; set; }
        public string QtyLabel
        {
            get
            {
                return $"{Qty:#########} {WspName}/箱";
            }
        }

        public string BoxNumber { get; set; }
        public string BoxNumberLabel
        {
            get
            {
                return $"{ConfigurationManager.AppSettings["BoxCodePrefix"]}{BoxNumber}";
            }
        }

        public string BoxNumberSuffix
        {
            get => BoxNumber.Substring(BoxNumber.Length - 4, 4);
        }

        public string ProductIdentificationCode { get; set; } = "";
        public string SpecialCustomer { get; set; }
        public List<SerialNumberRange> SerialNumberRanges { get; set; }

        public string SerialKey1 { get; set; }
        public string SerialBegin1 { get; set; }
        public string SerialEnd1 { get; set; }
        public string SerialKey2 { get; set; }
        public string SerialBegin2 { get; set; }
        public string SerialEnd2 { get; set; }
        public string SerialKey3 { get; set; }
        public string SerialBegin3 { get; set; }
        public string SerialEnd3 { get; set; }
        public string SerialKey4 { get; set; }
        public string SerialBegin4 { get; set; }
        public string SerialEnd4 { get; set; }
        public string SerialKey5 { get; set; }
        public string SerialBegin5 { get; set; }
        public string SerialEnd5 { get; set; }
        public string SerialKey6 { get; set; }
        public string SerialBegin6 { get; set; }
        public string SerialEnd6 { get; set; }

        //纸箱固定的8个辅助代码
        public string AuxiliaryCode1 { get; set; }
        public string AuxiliaryCode2 { get; set; }
        public string AuxiliaryCode3 { get; set; }
        public string AuxiliaryCode4 { get; set; }
        public string AuxiliaryCode5 { get; set; }
        public string AuxiliaryCode6 { get; set; }
        public string AuxiliaryCode7 { get; set; }
        public string AuxiliaryCode8 { get; set; }

        public List<ReceiveDetailLpn> ReceiveDetailLpns { get; set; }
        public short Copies { get; set; }

        public List<PackingSerialDetail> SerialDetails { get; set; }
        public string ProductionPlan { get; set; }
        public string PoCode { get; set; }
        public string WoCode { get; set; }
        public string WspName { get; set; }
        public string AssemblePeople { get; set; }

        public void SetSerial(IReadOnlyList<SerialNumberRange> serialNumberRanges)
        {
            var length = serialNumberRanges.Count < SerialGroupNumber
                ? serialNumberRanges.Count
                : SerialGroupNumber;

            for (var i = 0; i < length; i++)
            {
                var serialNumberRange = serialNumberRanges[i];
                switch (i)
                {
                    case 0:
                        SerialKey1 = serialNumberRange.Key;
                        SerialBegin1 = serialNumberRange.Begin;
                        SerialEnd1 = serialNumberRange.End;
                        break;
                    case 1:
                        SerialKey2 = serialNumberRange.Key;
                        SerialBegin2 = serialNumberRange.Begin;
                        SerialEnd2 = serialNumberRange.End;
                        break;
                    case 2:
                        SerialKey3 = serialNumberRange.Key;
                        SerialBegin3 = serialNumberRange.Begin;
                        SerialEnd3 = serialNumberRange.End;
                        break;
                    case 3:
                        SerialKey4 = serialNumberRange.Key;
                        SerialBegin4 = serialNumberRange.Begin;
                        SerialEnd4 = serialNumberRange.End;
                        break;
                    case 4:
                        SerialKey5 = serialNumberRange.Key;
                        SerialBegin5 = serialNumberRange.Begin;
                        SerialEnd5 = serialNumberRange.End;
                        break;
                    case 5:
                        SerialKey6 = serialNumberRange.Key;
                        SerialBegin6 = serialNumberRange.Begin;
                        SerialEnd6 = serialNumberRange.End;
                        break;
                }
            }
        }

        public void SetPaperSerial(IReadOnlyList<int> serialNumberRanges)
        {
            var length = serialNumberRanges.Count < PaperSerialGroupNumber
                ? serialNumberRanges.Count
                : PaperSerialGroupNumber;

            for (var i = 0; i < length; i++)
            {
                switch (i)
                {
                    case 0:
                        AuxiliaryCode1 = serialNumberRanges[i].ToString();
                        break;
                    case 1:
                        AuxiliaryCode2 = serialNumberRanges[i].ToString();
                        break;
                    case 2:
                        AuxiliaryCode3 = serialNumberRanges[i].ToString();
                        break;
                    case 3:
                        AuxiliaryCode4 = serialNumberRanges[i].ToString();
                        break;
                    case 4:
                        AuxiliaryCode5 = serialNumberRanges[i].ToString();
                        break;
                    case 5:
                        AuxiliaryCode6 = serialNumberRanges[i].ToString();
                        break;
                    case 6:
                        AuxiliaryCode7 = serialNumberRanges[i].ToString();
                        break;
                    case 7:
                        AuxiliaryCode8 = serialNumberRanges[i].ToString();
                        break;
                }
            }
        }

        private static IEnumerable<SerialNumberRange> GetSerialNumberRanges(List<int> serialNumberList)
        {
            Dictionary<string, List<string>> serialNumberDictionary = new Dictionary<string, List<string>>();
            
            serialNumberList.Sort();

            foreach (var item in serialNumberList)
            {
                var serialNumber = item.ToString();
                if (serialNumber.Length != 9)
                {
                    continue;
                }
                // 前4位：yyMM
                // 后五位：序列号
                var key = serialNumber.Substring(0, 4);
                var value = serialNumber.Substring(key.Length);
                if (serialNumberDictionary.ContainsKey(key))
                {
                    serialNumberDictionary[key].Add(value);
                }
                else
                {
                    serialNumberDictionary.Add(key, new List<string>
                    {
                        value
                    });
                }
            }

            var serialNumberRanges = new List<SerialNumberRange>();
            foreach (var pair in serialNumberDictionary)
            {
                pair.Value.Sort();
                var serialNumberRange = new SerialNumberRange
                {
                    Key = pair.Key,
                    Begin = pair.Value.First(),
                    End = pair.Value.Last()
                };
                serialNumberRanges.Add(serialNumberRange);
            }

            return serialNumberRanges;
        }

        public static List<SerialNumberRange> GetContinuousRanges(List<int> serialNumberList)
        {
            var continuousSerialNumbersList = PrintHelper.ContinusFind(serialNumberList.ToArray());

            var numberRanges = new List<SerialNumberRange>();
            foreach (var list in continuousSerialNumbersList)
            {
               numberRanges.AddRange(GetSerialNumberRanges(list));
            }

            return numberRanges;
        }

        public static List<SerialNumberRange> GetDoubleSerialNumberRanges(List<int> serialNumberList)
        {
            Dictionary<string, List<int>> serialNumberDictionary = new Dictionary<string, List<int>>();
            serialNumberList.Sort();
            foreach (var item in serialNumberList)
            {
                var serialNumber = item.ToString();
                if (serialNumber.Length != 9)
                {
                    continue;
                }
                // 前4位：yyMM
                // 后五位：序列号
                var key = serialNumber.Substring(0, 4);
                var flag = int.TryParse(serialNumber.Replace(key, string.Empty),out var value);
                if (!flag)
                {
                    continue;
                }

                if (serialNumberDictionary.ContainsKey(key))
                {
                    serialNumberDictionary[key].Add(value);
                }
                else
                {
                    serialNumberDictionary.Add(key, new List<int>
                    {
                        value
                    });
                }
            }

            var serialNumberRanges = new List<SerialNumberRange>();
            foreach (var pair in serialNumberDictionary)
            {
                pair.Value.Sort();
                var continusFind = PrintHelper.ContinusFind(pair.Value.ToArray());
                if (continusFind.Count == 0)
                {
                    var serialNumberRange = new SerialNumberRange
                    {
                        Key = pair.Key,
                        Begin = GetSerialPadLeft(pair.Value.First()),
                        End = GetSerialPadLeft(pair.Value.Last())
                    };
                    serialNumberRanges.Add(serialNumberRange);
                }
                else
                {
                    foreach (var ints in continusFind)
                    {
                        var serialNumberRange = new SerialNumberRange
                        {
                            Key = pair.Key,
                            Begin = GetSerialPadLeft(ints.First()),
                            End = GetSerialPadLeft(ints.Last())
                        };
                        serialNumberRanges.Add(serialNumberRange);
                    }
                }
            }

            return serialNumberRanges;
        }

        private static string GetSerialPadLeft(int serial)
        {
            return serial.ToString().PadLeft(5, '0');
        }

        public static bool IsDobuleSerialNumber(List<string> packingSequenctNumberPairs,SerialNumberPrintDto serialNumberPrintDto)
        {
            return IsDobuleSerialNumber(packingSequenctNumberPairs, serialNumberPrintDto.Model,
                serialNumberPrintDto.SkuName);
        }

        public static bool IsDobuleSerialNumber(List<string> packingSequenctNumberPairs,string model,string skuName)
        {
            return packingSequenctNumberPairs != null
                   && packingSequenctNumberPairs.Count > 0
                   && packingSequenctNumberPairs.Contains(model)
                   && skuName.Contains("闸片");
        }
    }

    public class SerialNumberRange
    {
        public string Key { get; set; }
        public string Begin { get; set; }
        public string End { get; set; }
    }
}