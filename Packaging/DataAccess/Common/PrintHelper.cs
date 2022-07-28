using System.Collections.Generic;
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
    }
}