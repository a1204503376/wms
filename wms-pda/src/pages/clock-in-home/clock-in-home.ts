import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AppService } from "../../services/App.service";
import { Utils } from '../../services/Utils';
import { NativeService } from "../../services/NativeService"
import { Api, ContenType, Method } from '../../utils/appConstant';
import { Storage } from '@ionic/storage';
/**
 * Generated class for the ClockInHomePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-clock-in-home',
  templateUrl: 'clock-in-home.html',
})
export class ClockInHomePage {
  public time: any;//定时器
  public Modelvalue: string = '';//签到值
  public clockList: any = [];//签到记录
  public clockItem: any = null;//人员记录
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public storage: Storage
  ) {
  }
  setDom() {
    if (Utils.isEmpty(this.clockItem)) {
      this.Modelvalue = '签到';
      this.setDomInfo();
      return;
    }
    if (this.clockItem.loginStatus == 0) {
      this.Modelvalue = '签到';
    } else {
      this.Modelvalue = '签退';
    }
    this.setDomInfo();
  }
  setDomInfo() {
    let oStu = document.getElementById('element2');
    oStu.setAttribute('data-text', this.Modelvalue);//替换
  }
  ionViewDidLoad() {
    this.time = setInterval(function () {
      var date = new Date();
      var year = date.getFullYear(); //获取当前年份 
      var mon = date.getMonth() + 1; //获取当前月份 
      var da = date.getDate(); //获取当前日 
      var day = date.getDay(); //获取当前星期几 
      var h = date.getHours(); //获取小时 
      var m = date.getMinutes(); //获取分钟 
      var s = date.getSeconds(); //获取秒 
      var d = document.getElementById('date');
      d.innerHTML = year + '年' + mon + '月' + da + '日' + '&emsp;' + h + '时' + m + '分' + s + '秒';
    }, 1000)
    //获取当前人员签到
    this.appService.httpRequest(Api.api + Api.UserOnlineList, Method.Get, '', '', result => {
      this.clockItem = result.data[0];
      this.setDom();
    });
    this.clockGetApi();
  }
  /**
   * 签到
   */
  onClick() {
    this.clockInApi();
  }
  /**
   * 获取签到列表
   */
  clockInApi() {
    this.storage.get('access_token').then((val) => {
      if (Utils.isNotEmpty(val)) {
        let parmams = {
          token: val,
          loginStatus: '',
          suoId: ''
        };
        if (Utils.isEmpty(this.clockItem)) {
          parmams['loginStatus'] = '1';
        } else {
          parmams['suoId'] = this.clockItem.suoId;
          parmams['loginStatus'] = this.clockItem.loginStatus;
          if (this.clockItem.loginStatus == '0') {
            parmams['loginStatus'] = '1';
          } else {
            parmams['loginStatus'] = '0';
          }
        }
        this.nativeService.showLoading();
        this.appService.httpRequest(Api.api + Api.UserOnlinesubmit, Method.Post, parmams, ContenType.Json, result => {
          this.nativeService.hideLoading();
          //签到日志
          this.clockList = result.data.userRegister;
          //用户签到状态
          this.clockItem = result.data.userOnline;
          this.setDom();
        });
      }
    });
  }
  /**
   * 当天日志列表
   */
  clockGetApi() {
    this.appService.httpRequest(Api.api + Api.UserRegisterList, Method.Get, '', '', result => {
      this.clockList = result.data;
    });
  }
  ionViewWillLeave() {
    clearInterval(this.time);
  }
  detailed() {
    this.navCtrl.push('ClockInPage');
  }
}
