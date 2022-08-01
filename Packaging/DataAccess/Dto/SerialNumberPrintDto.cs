using System;
using System.Collections.Generic;
using System.Linq;
using DataAccess.Enitiies;

namespace DataAccess.Dto
{
    public class SerialNumberPrintDto
    {
        public string BoxType { get; set; }
        public string SkuName { get; set; }
        public string Model { get; set; }
        public string PrintDate { get; set; } = DateTime.Now.ToString("yyMMdd");
        public string UserName { get; set; }
        public string SpeedClass { get; set; } = "";
        public string Qty { get; set; }
        public string BoxNumber { get; set; }

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


        public static void SetSerialNumberRanges(SerialNumberPrintDto serialNumberPrintDto,
            List<string> serialNumberList)
        {
            Dictionary<string, List<string>> serialNumberDictionary = new Dictionary<string, List<string>>();
            foreach (var serialNumber in serialNumberList)
            {
                if (serialNumber.Length < 7)
                {
                    continue;
                }
                var key = serialNumber.Substring(0, 6);
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
            SetSerial(serialNumberPrintDto, serialNumberRanges);
            serialNumberPrintDto.SerialNumberRanges = serialNumberRanges;
        }

        private static void SetSerial(SerialNumberPrintDto serialNumberPrintDto, List<SerialNumberRange> serialNumberRanges)
        {
            for (var i = 0; i < serialNumberRanges.Count; i++)
            {
                var serialNumberRange = serialNumberRanges[i];
                switch (i)
                {
                    case 0:
                        serialNumberPrintDto.SerialKey1 = serialNumberRange.Key;
                        serialNumberPrintDto.SerialBegin1 = serialNumberRange.Begin;
                        serialNumberPrintDto.SerialEnd1 = serialNumberRange.End;
                        break;
                    case 1:
                        serialNumberPrintDto.SerialKey2 = serialNumberRange.Key;
                        serialNumberPrintDto.SerialBegin2 = serialNumberRange.Begin;
                        serialNumberPrintDto.SerialEnd2 = serialNumberRange.End;
                        break;
                    case 2:
                        serialNumberPrintDto.SerialKey3 = serialNumberRange.Key;
                        serialNumberPrintDto.SerialBegin3 = serialNumberRange.Begin;
                        serialNumberPrintDto.SerialEnd3 = serialNumberRange.End;
                        break;
                    case 3:
                        serialNumberPrintDto.SerialKey4 = serialNumberRange.Key;
                        serialNumberPrintDto.SerialBegin4 = serialNumberRange.Begin;
                        serialNumberPrintDto.SerialEnd4 = serialNumberRange.End;
                        break;
                    case 4:
                        serialNumberPrintDto.SerialKey5 = serialNumberRange.Key;
                        serialNumberPrintDto.SerialBegin5 = serialNumberRange.Begin;
                        serialNumberPrintDto.SerialEnd5 = serialNumberRange.End;
                        break;
                    case 5:
                        serialNumberPrintDto.SerialKey6 = serialNumberRange.Key;
                        serialNumberPrintDto.SerialBegin6 = serialNumberRange.Begin;
                        serialNumberPrintDto.SerialEnd6 = serialNumberRange.End;
                        break;
                }
            }
        }
    }

    public class SerialNumberRange
    {
        public string Key { get; set; }
        public string Begin { get; set; }
        public string End { get; set; }
    }
}