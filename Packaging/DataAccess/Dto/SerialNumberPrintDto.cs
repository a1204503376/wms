using System;
using System.Collections.Generic;
using System.Configuration;
using DataAccess.Enitiies;

namespace DataAccess.Dto
{
    public class SerialNumberPrintDto
    {
        /// <summary>
        /// 每页序列号分组数量
        /// </summary>
        public static readonly int SerialGroupNumber = 6;

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
    }

    public class SerialNumberRange
    {
        public string Key { get; set; }
        public string Begin { get; set; }
        public string End { get; set; }
    }
}