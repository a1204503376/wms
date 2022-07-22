using System;
using FreeSql.DataAnnotations;

namespace DataAccess.Enitiies
{
    public class BaseEntity<T>
    {
        [Column(IsIdentity = true)]
        public T Id { get; set; }
        [Column(Position = -2)]
        public DateTime CreateTime { get; set; } = DateTime.Now;
        [Column(Position = -2)]
        public DateTime? UpdateTime { get; set; }
    }
}