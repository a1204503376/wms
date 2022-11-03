using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Threading.Tasks;
using DataAccess.Dto;
using DataAccess.Encasement;
using DataAccess.Wms;
using Packaging.Common;
using Packaging.Utility;

namespace Packaging.Encasement
{
    public partial class SerialNumberReport : DevExpress.XtraReports.UI.XtraReport
    {
        public readonly List<SerialNumberPrintDto> SerialNumberPrintDtoList;

        private SerialNumberReport()
        {
            InitializeComponent();
        }

        public SerialNumberReport(SerialNumberPrintDto serialNumberPrintDto) 
            : this(new List<SerialNumberPrintDto>{serialNumberPrintDto})
        {

        }

        public SerialNumberReport(List<SerialNumberPrintDto> serialNumberPrintDtoList) : this()
        {
            SerialNumberPrintDtoList = serialNumberPrintDtoList;
            this.objectDataSource1.DataSource = SerialNumberPrintDtoList;
        }
    }
}
