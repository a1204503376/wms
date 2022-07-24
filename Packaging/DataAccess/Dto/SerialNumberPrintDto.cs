using System.Collections.Generic;
using DataAccess.Enitiies;

namespace DataAccess.Dto
{
    public class SerialNumberPrintDto
    {
        public string BoxType { get; set; }
        public string SkuName { get; set; }
        public string Model { get; set; }
        public string PrintDate { get; set; }
        public string UserName { get; set; }
        public string SpeedClass { get; set; }
        public string Qty { get; set; }
        public string BoxNumber { get; set; }

        public string BoxNumberSuffix
        {
            get => BoxNumber.Substring(BoxNumber.Length - 4, 4);
        }

        public string ProductIdentificationCode { get; set; }
        public string SpecialCustomer { get; set; }
        public List<SerialNumberRange> SerialNumberRanges { get; set; }
        public List<ReceiveDetailLpn> ReceiveDetailLpns { get; set; }
        public short Copies { get; set; }
    }

    public class SerialNumberRange
    {
        public string Key { get; set; }
        public string Begin { get; set; }
        public string End { get; set; }
    }
}