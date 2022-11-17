using System;
using System.Collections.Generic;
using DataAccess.Dto;

namespace DataAccess.Common
{
    public class PrintHelper
    {
        public static List<BatchPrintDto> AddCopiesForBatch(BatchPrintDto batchPrintDto, short copies)
        {
            return AddCopiesForBatch(new List<BatchPrintDto> { batchPrintDto }, copies);
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
        public static List<List<int>> ContinusFind(int[] serialNumberArray)
        {
            Array.Sort(serialNumberArray);
            var index = 0;
            var continuousList = new List<List<int>>();
            while (index< serialNumberArray.Length)
            {
                continuousList.Add(GetContinuousList(serialNumberArray, ref index));
            }

            return continuousList;
        }

        private static List<int> GetContinuousList(IReadOnlyList<int> serialNumberArray, ref int index)
        {
            var continuousSerialNumberList = new List<int>();
            for (int i = index, l = serialNumberArray.Count; i < l; i++)
            {
                if (i + 1 < l && serialNumberArray[i] + 1 == serialNumberArray[i + 1])
                {
                    continuousSerialNumberList.Add(serialNumberArray[i]);
                }
                else
                {
                    continuousSerialNumberList.Add(serialNumberArray[i]);
                    index = i + 1;
                    break;
                }
            }

            return continuousSerialNumberList;
        }
    }
}