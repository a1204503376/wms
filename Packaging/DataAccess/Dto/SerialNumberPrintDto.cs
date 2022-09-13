using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using DataAccess.Enitiies;

namespace DataAccess.Dto
{
    public class SerialNumberPrintDto
    {
        /// <summary>
        /// 每页序列号分组数量
        /// </summary>
        public const int SerialGroupNumber = 6;

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
        public static List<SerialNumberRange> GetSerialNumberRanges(List<int> serialNumberList)
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
                var value = serialNumber.Replace(key, string.Empty);
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
                var continusFind = ContinusFind(pair.Value.ToArray());
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

        /// <summary>
        /// 计算给定的数组内连续的序列和独立的值
        /// </summary>
        /// <returns></returns>
        private static List<List<int>> ContinusFind(int[] numList)
        {
            Array.Sort(numList);

            var s = 1;
            var length = numList.Length;
            var findList = new List<List<int>>();

            switch (length)
            {
                case 1:
                    findList.Add(new List<int>() { numList[s - 1] });
                    return findList;
                case 2:
                    {
                        if (numList[s] - numList[s - 1] != 1)
                        {
                            findList.Add(new List<int>() { numList[s - 1] });
                            findList.Add(new List<int>() { numList[s] });
                        }
                        else
                        {
                            findList.Add(new List<int>() { numList[s - 1], numList[s] });
                        }

                        return findList;
                    }
            }

            while (s <= length - 1)
            {
                var firstAloneFlag = s - 1 == 0 && numList[s] - numList[s - 1] != 1;
                var middleAloneFlag = s + 1 < length && numList[s] - numList[s - 1] != 1 && numList[s + 1] - numList[s] != 1;
                var lastAloneFlag = s + 1 == length && numList[s] - numList[s - 1] != 1;
                if (firstAloneFlag || middleAloneFlag || lastAloneFlag)
                {
                    var val = firstAloneFlag ? numList[s - 1] : numList[s];
                    findList.Add(new List<int> { val });
                    s += 1;
                    continue;
                }

                if (numList[s] - numList[s - 1] == 1)
                {
                    var index = s - 1;
                    var count = 1;
                    while (s <= length - 1 && numList[s] - numList[s - 1] == 1)
                    {
                        s += 1;
                        count++;
                    }

                    findList.Add(numList.ToList().GetRange(index, count));
                }
                else
                {

                    s += 1;
                }
            }

            return findList;
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