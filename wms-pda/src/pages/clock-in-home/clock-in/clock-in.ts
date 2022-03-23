import { Component, ViewChild, ElementRef } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import ECharts from 'echarts';
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService"
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
/**
 * Generated class for the ClockInPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-clock-in',
  templateUrl: 'clock-in.html',
})
export class ClockInPage {
  @ViewChild('chart') chart: ElementRef;
  public ceshi: string;//测试d
  public UserRegisterMoth: any;//本月签到列表
  constructor(public navCtrl: NavController, public navParams: NavParams, public appService: AppService,
    public nativeService: NativeService, ) {
  }
  onClick(ele) {
    this.currentWeek();
  }
  ionViewDidLoad() {
    let tiem = Utils.dateFormat(new Date(), 'yyyy-MM-dd');
    this.appService.httpRequest(Api.api + Api.userRegisterListMoth, Method.Get, { time: tiem }, '', result => {
      this.UserRegisterMoth = result.data;
      console.log(this.UserRegisterMoth);
      this.currentWeek();
    });
  }
  ionViewDidEnter() {
    this.initChart();
  }
  initChart() {
    let element = this.chart.nativeElement;
    element.style.width = (document.body.clientWidth - 16) + 'px';//设置容器宽度
    let myChart = ECharts.init(element);
    var option = option = {
      color: ['#3398DB'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
          type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
        height: '100px'
      },
      xAxis: [
        {
          type: 'category',
          data: ['入', '出', '盘', '退'],
          axisTick: {
            alignWithLabel: true
          }
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: [
        {
          name: '直接访问',
          type: 'bar',
          barWidth: '30%',
          data: [10, 52, 200, 334, 390, 330, 220]
        }
      ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

  }
  currentWeek() {
    let ele = document.getElementById('ele');
    // var obj = this.offset(ele);
    // var x = obj.left;
    // var y = obj.top + ele.offsetHeight + 1;npm install @types/echarts --save
    //创建日历界面
    if (!document.getElementById('week')) {
      var coDiv = document.getElementById('calendar');
      // document.getElementsByTagName('ion-content')[0].appendChild(oDiv);
      //document.body.appendChild(oDiv);
      var oDiv = document.createElement('div');
      coDiv.appendChild(oDiv);
      oDiv.id = 'week';
      oDiv.style.top = 50 + "px";
      oDiv.style.left = 10 + "px";
      //创建日历title
      var h6 = document.createElement('h6');
      oDiv.appendChild(h6);

      var divborder = document.createElement('div');
      h6.appendChild(divborder);
      divborder.className = 'divborder';

      var prev = document.createElement('span');
      divborder.appendChild(prev);
      prev.className = 'prev';
      prev.innerHTML = '<';
      var content = document.createElement('span');
      content.className = "content";
      divborder.appendChild(content);
      var next = document.createElement('span');
      divborder.appendChild(next);
      next.className = 'next';
      next.innerHTML = '>';
      //创建星期日到星期六的文字行
      // var oPs = document.createElement('p');
      // oPs.className = "rlTitle";
      // oDiv.appendChild(oPs);
      // var opsCont = ['日', '一', '二', '三', '四', '五', '六'];
      // for (var i = 0; i <= 6; i++) {
      //   var oSpan = document.createElement('span');
      //   oPs.appendChild(oSpan);
      //   oSpan.innerHTML = opsCont[i];
      // }
      //创建日历上面的日期行数所需要的变量
      var oUl = document.createElement('ul');
      oUl.className = "rlCenter clearfix";
      oDiv.appendChild(oUl);
      var currentDate = new Date();
      var currentYear = currentDate.getFullYear();
      var currentMonth = currentDate.getMonth();
      this.active(currentMonth, oUl, currentYear, content);//传参数月份
      //创建日历上下翻月
      prev.onclick = function () {
        let iscurrentMonth = --currentMonth;
        let Month = iscurrentMonth;
        iscurrentMonth += 1;
        this.appService.httpRequest(Api.api + Api.userRegisterListMoth, Method.Get, { time: currentYear + '-' + iscurrentMonth + '-01' }, '', result => {
          this.UserRegisterMoth = result.data;
          this.active(Month, oUl, currentYear, content);
        });
      }.bind(this);
      next.onclick = function () {
        let iscurrentMonth = ++currentMonth;
        let Month = iscurrentMonth;
        iscurrentMonth += 1;
        this.appService.httpRequest(Api.api + Api.userRegisterListMoth, Method.Get, { time: currentYear + '-' + iscurrentMonth + '-01' }, '', result => {
          this.UserRegisterMoth = result.data;
          this.active(Month, oUl, currentYear, content);
        });
      }.bind(this);
    }
  }
  //动态创建日历上面日期，变包装成方法
  active(m, oUl, currentYear, content) {
    oUl.innerHTML = ''; //切忌一定要把这个内容去掉，要不然会点一次翻页都在日历下面依次显示出来
    var activeDate = new Date(currentYear, m, 1); //外面传进来的不断变化的日期对象
    var year = activeDate.getFullYear();
    var month = activeDate.getMonth(); //把当前的月份保存下来只是为了给title获取月份
    content.innerHTML = year + '年' + (month + 1) + '月';
    //创建日历上面的日期行数
    var n = 1 - activeDate.getDay();
    if (n == 1) {
      n = -6;
    } //为了日历更友好的显示三个月，让用户看的更明白。
    activeDate.setDate(n+1); //如果n为负数，则减少月份.在用这个月最后一天减去这个值就可以获得日历从哪天开始的。
    for (var i = 0; i < 42; i++) {
      var oLi = document.createElement('li');
      oUl.appendChild(oLi);
      var date = activeDate.getDate(); //返回日期1-31号
      //oLi.innerHTML = date.toString();
      let todayRegister = this.UserRegisterMoth.filter(x => x.lastLoginDay == date);
      //日期文字
      var ospan = document.createElement('span');
      ospan.className = "lispan";
      ospan.innerText = date.toString();
      oLi.appendChild(ospan);
      //左半
      var osdivleft = document.createElement('div');
      osdivleft.className = "left";
      if (activeDate.getMonth() != month) {
        osdivleft.style.background = '#f9f5f5';
      } else {
        if (Utils.isEmpty(todayRegister)) {
          osdivleft.style.background = '#dfdddd';
        } else {
          let todayRegisterleft = todayRegister.filter(x => x.loginStatus == 0);
          if (Utils.isEmpty(todayRegisterleft)) {
            osdivleft.style.background = '#dfdddd';
          } else {
            osdivleft.style.background = '#33cbcc';
          }
        }
      }
      oLi.appendChild(osdivleft);
      //右半
      var osdivright = document.createElement('div');
      osdivright.className = "right";
      if (activeDate.getMonth() != month) {
        osdivright.style.background = '#f9f5f5';
      } else {
        if (Utils.isEmpty(todayRegister)) {
          osdivright.style.background = '#dfdddd';
        } else {
          let todayRegisterleft = todayRegister.filter(x => x.loginStatus == 1);
          if (Utils.isEmpty(todayRegisterleft)) {
            osdivright.style.background = '#dfdddd';
          } else {
            osdivright.style.background = '#33cbcc';
          }
        }
      }
      oLi.appendChild(osdivright);
      // oLi.dateValue = year + "-" + (activeDate.getMonth() + 1) + "-" + date; //这里必须是activeDate.getMonth()+1，不能用m+1。因为这个是一直变化的。要不然日历不管点哪天都是属于当前月份的。
      oLi.onclick = function () {
        // ele.value = this.dateValue;//文本框获取的年月日
        // document.body.removeChild(oDiv); //获取到年月日，日历取消
        // oDiv = null;
      };
      if (activeDate.getMonth() != month) {
        oLi.style.background = "#f9f5f5"; //不是本月的天数颜色变成灰色 
      }
      //切忌下面这个增加天数语句，一定要判断完上面是不是本月的天数，然后在添加这条增加语句，要不然会出现错误。
      activeDate.setDate(date + 1); //如果超出该月份应有的天数则增加月份
    }
  }
  offset(ele) {
    var l = ele.offsetLeft;
    var t = ele.offsetTop;
    var p = ele.offsetParent;
    while (p) {
      if (window.navigator.userAgent.indexOf("MSIE 8") > -1) {
        l += p.offsetLeft;
        t += p.offsetTop;
      } else {
        l += p.offsetLeft + p.clientLeft;
        t += p.offsetTop + p.clientTop;
      }
      p = p.offsetParent;
    }
    return {
      left: l,
      top: t
    }
  }
}
