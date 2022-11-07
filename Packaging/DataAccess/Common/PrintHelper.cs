using System;
using System.Collections.Generic;
using System.Linq;
using DataAccess.Dto;

namespace DataAccess.Common
{
    public class PrintHelper
    {
        public static List<BatchPrintDto> AddCopiesForBatch(BatchPrintDto batchPrintDto, short copies)
        {
            return AddCopiesForBatch(new List<BatchPrintDto>{ batchPrintDto}, copies);
        }

        public static List<BatchPrintDto> AddCopiesForBatch(List<BatchPrintDto> batchPrintDtos, short copies)
        {
            var printDtos = new List<BatchPrintDto>();
            foreach (var batchPrintDto in batchPrintDtos)
            {
                for (var i = 0; i < copies; i++)
                {
                    printDtos.Add(batchPrintDto);
                }
            }

            return printDtos;
        }

        public static List<SerialNumberPrintDto> AddCopiesForSerial(SerialNumberPrintDto serialNumberPrintDto,
            short copies)
        {
            return AddCopiesForSerial(new List<SerialNumberPrintDto> { serialNumberPrintDto }, copies);
        }

        public static List<SerialNumberPrintDto> AddCopiesForSerial(List<SerialNumberPrintDto> serialNumberPrintDtos,short copies)
        {
            var numberPrintDtos = new List<SerialNumberPrintDto>();
            foreach (var serialNumberPrintDto in serialNumberPrintDtos)
            {
                for (var i = 0; i < copies; i++)
                {
                    numberPrintDtos.Add(serialNumberPrintDto);
                }
            }

            return numberPrintDtos;
        }

        /// <summary>
        /// 计算给定的数组内连续的序列和独立的值
        /// </summary>
        /// <returns></returns>
        public static List<List<int>> ContinusFind(int[] numList)
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
    }
}