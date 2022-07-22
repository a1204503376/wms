using FreeSql.DataAnnotations;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Enitiies
{
    public class PackingSpeedClass:BaseEntity<long>
    {
        public string SpeedClass { get; set; }
        public int Order { get; set; }
    }
}
