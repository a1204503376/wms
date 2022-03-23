/**
 * Created by yanxiaojun617@163.com on 12-27.
 */
import { Injectable } from '@angular/core';

/**
 * Utils类存放和业务无关的公共方法
 * @description
 */
@Injectable()
export class Utils {

  //去除所有空格
  static clearSpace(value): string {
    return value.replace(/\s*/g, "");
  }
  static isEmpty(value): boolean {
    return value == null || value == undefined || typeof value === 'string' && value.length === 0 || value.length === 0 || JSON.stringify(value) == "{}";
  }

  static isNotEmpty(value): boolean {
    return !Utils.isEmpty(value);
  }

  /**
   * 格式“是”or“否”
   * @param value
   * @returns {string|string}
   */
  static formatYesOrNo(value: number | string): string {
    return value == 1 ? '是' : (value == '0' ? '否' : null);
  }


  /**
   * 日期对象转为日期字符串
   * @param date 需要格式化的日期对象
   * @param sFormat 输出格式,默认为yyyy-MM-dd                        年：y，月：M，日：d，时：h，分：m，秒：s
   * @example  dateFormat(new Date())                               "2017-02-28"
   * @example  dateFormat(new Date(),'yyyy-MM-dd')                  "2017-02-28"
   * @example  dateFormat(new Date(),'yyyy-MM-dd HH:mm:ss')         "2017-02-28 13:24:00"   ps:HH:24小时制
   * @example  dateFormat(new Date(),'yyyy-MM-dd hh:mm:ss')         "2017-02-28 01:24:00"   ps:hh:12小时制
   * @example  dateFormat(new Date(),'hh:mm')                       "09:24"
   * @example  dateFormat(new Date(),'yyyy-MM-ddTHH:mm:ss+08:00')   "2017-02-28T13:24:00+08:00"
   * @example  dateFormat(new Date('2017-02-28 13:24:00'),'yyyy-MM-ddTHH:mm:ss+08:00')   "2017-02-28T13:24:00+08:00"
   * @returns {string}
   */
  static dateFormat(date: Date, sFormat: String = 'yyyy-MM-dd'): string {
    const time = {
      Year: 0,
      TYear: '0',
      Month: 0,
      TMonth: '0',
      Day: 0,
      TDay: '0',
      Hour: 0,
      THour: '0',
      hour: 0,
      Thour: '0',
      Minute: 0,
      TMinute: '0',
      Second: 0,
      TSecond: '0',
      Millisecond: 0
    };
    time.Year = date.getFullYear();
    time.TYear = String(time.Year).substr(2);
    time.Month = date.getMonth() + 1;
    time.TMonth = time.Month < 10 ? '0' + time.Month : String(time.Month);
    time.Day = date.getDate();
    time.TDay = time.Day < 10 ? '0' + time.Day : String(time.Day);
    time.Hour = date.getHours();
    time.THour = time.Hour < 10 ? '0' + time.Hour : String(time.Hour);
    time.hour = time.Hour < 13 ? time.Hour : time.Hour - 12;
    time.Thour = time.hour < 10 ? '0' + time.hour : String(time.hour);
    time.Minute = date.getMinutes();
    time.TMinute = time.Minute < 10 ? '0' + time.Minute : String(time.Minute);
    time.Second = date.getSeconds();
    time.TSecond = time.Second < 10 ? '0' + time.Second : String(time.Second);
    time.Millisecond = date.getMilliseconds();

    return sFormat.replace(/yyyy/ig, String(time.Year))
      .replace(/yyy/ig, String(time.Year))
      .replace(/yy/ig, time.TYear)
      .replace(/y/ig, time.TYear)
      .replace(/MM/g, time.TMonth)
      .replace(/M/g, String(time.Month))
      .replace(/dd/ig, time.TDay)
      .replace(/d/ig, String(time.Day))
      .replace(/HH/g, time.THour)
      .replace(/H/g, String(time.Hour))
      .replace(/hh/g, time.Thour)
      .replace(/h/g, String(time.hour))
      .replace(/mm/g, time.TMinute)
      .replace(/m/g, String(time.Minute))
      .replace(/ss/ig, time.TSecond)
      .replace(/s/ig, String(time.Second))
      .replace(/fff/ig, String(time.Millisecond));
  }
  static urlFormat(sFormat: string): string {
    return sFormat.replace(/\+/g, String('%2B'))
      .replace(/\s+/g, String('%20'))
      .replace(/\?/g, '%3F')
      //.replace(/%/g, '%25')
      .replace(/#/g, '%23')
      .replace(/&/g, '%26')
      .replace(/=/g, '%3D');
  }

  /**
   * 字符串转日期
   * @param dateStr 
   * @param separator 
   */
  static stringToDate(dateStr, separator) {
    if (!separator) {
      separator = "-";
    }
    var dateArr = dateStr.split(separator);
    var year = parseInt(dateArr[0]);
    var month;
    //处理月份为04这样的情况                         
    if (dateArr[1].indexOf("0") == 0) {
      month = parseInt(dateArr[1].substring(1));
    } else {
      month = parseInt(dateArr[1]);
    }
    var day = parseInt(dateArr[2]);
    var date = new Date(year, month - 1, day);
    return date;
  }

  /**
   * 每次调用sequence加1
   * @type {()=>number}
   */
  static getSequence = (() => {
    let sequence = 1;
    return () => {
      return ++sequence;
    };
  })();

  /**
   * 返回字符串长度，中文计数为2
   * @param str
   * @returns {number}
   */
  static strLength(str: string): number {
    let len = 0;
    for (let i = 0, length = str.length; i < length; i++) {
      str.charCodeAt(i) > 255 ? len += 2 : len++;
    }
    return len;
  }

  /** 产生一个随机的32位长度字符串 */
  static uuid() {
    let text = '';
    const possible = 'abcdef0123456789';
    for (let i = 0; i < 19; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text + new Date().getTime();
  }

  /**
 * 返回当前日期三年后的日期
 * @type Date
 */
  static get3YearAfter() {
    let date = new Date();
    let year = date.getFullYear() + 3;
    let month = date.getMonth() + 1;
    let Tmonth = month < 10 ? '0' + month : String(month);
    let day = date.getDate();
    let Tday = day < 10 ? '0' + day : String(day);

    return new Date(year.toString() + "-" + Tmonth.toString() + "-" + Tday.toString());
  }
  /**
   * 字符串(YYYYMMDD)转日期
   * @type Date
   */
  static getDateFromYYYYMMDD(str: string) {
    let year = str.substr(0, 4);
    let month = str.substr(4, 2);
    let day = str.substr(6, 2);

    return year + "-" + month + "-" + day;
  }
  /**
   * 字符串(YYYY*MM*DD)转日期
   * @type Date
   */
  static getDateFromWhatever(str: string) {
    let year = str.substr(0, 4);
    let month = str.substr(5, 2);
    let day = str.substr(8, 2);
    let symbol = str.substr(4, 1);
    return year + "-" + month + "-" + day;
  }

  /**
  * 获取时间批属性处理
  * @type Date
  */
  static getDateBath(dateType: string, date: string) {
    if (date.length == 8) {
      return this.getDateFromYYYYMMDD(date);
    } else if (date.length == 10) {
      return this.getDateFromWhatever(date);
    } else {
      return '';
    }
  }
  /**
  * 提交时间批属性处理
  * @type Date
  */
  static postDateBath(dateType: string, date: string) {
    if (dateType.length == 8) {
      return date.replace(/-/g, '');
    } else if (dateType.length == 10) {
      return date.replace(/-/g, dateType.substr(4, 1));
    } else {
      return '';
    }
  }
  /**
   * 判断是否正整数
   * @type Boolean
   */
  static IsPositiveInt(str: string): boolean {
    if (Utils.isEmpty(str)) {
      return false;
    }

    if (str.trim() === ("0")) {
      return false;
    }
    let rules = /^[1-9]\d*$/g;
    let regexp = new RegExp(rules);
    return regexp.test(str);
  }

  /**
  * 判断是否为整数或浮点数
  * @type Boolean
  */
  static IsPositiveDecimal(str: string): boolean {
    let rules = /^\d+(\.\d+)?$/g;
    let regexp = new RegExp(rules);
    return regexp.test(str);
  }
  /**
* 判断是否包含
* @type Boolean
*/
  static Contains(str: string, value: string): boolean {
    return str.indexOf(value) != -1;
  }
  /**
   * 全角转半角
   * @param str 
   */
  static ToCDB(str) {
    var tmp = "";
    for (var i = 0; i < str.length; i++) {
      if (str.charCodeAt(i) > 65248 && str.charCodeAt(i) < 65375) {
        tmp += String.fromCharCode(str.charCodeAt(i) - 65248);
      }
      else {
        tmp += String.fromCharCode(str.charCodeAt(i));
      }
    }
    return tmp
  }
  /**
   * 半角转全角
   * @param txtstring 
   */
  static ToDBC(txtstring) {
    var tmp = "";
    for (var i = 0; i < txtstring.length; i++) {
      if (txtstring.charCodeAt(i) == 32) {
        tmp = tmp + String.fromCharCode(12288);
      } else if (txtstring.charCodeAt(i) < 127) {
        tmp = tmp + String.fromCharCode(txtstring.charCodeAt(i) + 65248);
      }
    }
    return tmp;
  }

  static isObjectValueEqual(a, b) {
    //取对象a和b的属性名
    var aProps = Object.getOwnPropertyNames(a);
    var bProps = Object.getOwnPropertyNames(b);
    //判断属性名的length是否一致
    if (aProps.length != bProps.length) {
      return false;
    }
    //循环取出属性名，再判断属性值是否一致 
    for (var i = 0; i < aProps.length; i++) {
      var propName = aProps[i];
      if (a[propName] !== b[propName]) {
        return false;
      }
    }
    return true;
  }
  static isObjectValueEqual1(a, b, nots) {
    // 判断两个对象是否指向同一内存，指向同一内存返回true
    if (a === b) return true
    // 获取两个对象键值数组
    let aProps = Object.getOwnPropertyNames(a)
    let bProps = Object.getOwnPropertyNames(b)
    // 判断两个对象键值数组长度是否一致，不一致返回false
    // if (aProps.length !== bProps.length) return false
    // 遍历对象的键值
    for (let prop in a) {
      if (nots.includes(prop)) continue;
      // 判断a的键值，在b中是否存在，不存在，返回false
      if (b.hasOwnProperty(prop)) {
        // 判断a的键值是否为对象，是则递归，不是对象直接判断键值是否相等，不相等返回false
        if (typeof a[prop] === 'object') {
          if (!Utils.isObjectValueEqual1(a[prop], b[prop], nots)) return false
        } else if (a[prop] !== b[prop]) {
          return false
        }
      } else {
        return false
      }
    }
    return true
  }
  static contains(a, obj, nots) {
    for (var i = 0; i < a.length; i++) {
      if (Utils.isObjectValueEqual1(a[i], obj, nots)) {
        return true;
      }
    }
    return false;
  }
}
