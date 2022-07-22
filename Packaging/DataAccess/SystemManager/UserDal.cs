using System;
using System.Collections.Generic;
using System.Linq;
using DataAccess.Enitiies;

namespace DataAccess.SystemManager
{
    public class UserDal
    {
        /// <summary>
        /// 用户名和密码验证
        /// </summary>
        public static User ValidateUser(string account, string password)
        {
            var user = Db.FreeSql.Select<User>()
                .Where(d => d.Account == account
                            && d.Password == password)
                .First<User>();
            return user;
        }
    }
}
