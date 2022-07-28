using System.Collections.Generic;
using System.ComponentModel;
using DataAccess.Dto;
using DevExpress.DataAccess.ObjectBinding;

namespace PackagingWeb.Services
{
    [DisplayName("Batch")]
    [HighlightedClass]
    public class BatchDataSource
    {
        [HighlightedMember]
        public IEnumerable<BatchPrintDto> GetBatchPrintDtoList()
        {
            return new List<BatchPrintDto>();
        }
    }
}