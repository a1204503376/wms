import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Platform, App } from 'ionic-angular';
import { Utils } from '../../services/Utils';
import { AppService } from "../../services/App.service";
import { Storage } from '@ionic/storage';
import { NativeService } from "../../services/NativeService"
import { Api, ContenType, Method } from '../../utils/appConstant';

@IonicPage()
@Component({
    selector: 'page-setting',
    templateUrl: 'setting.html',
})
export class SettingPage {
    // 用户名
    public username: string = "";
    public logintime: string = "";
    public signStatus:string = "未签到";
    public version:string="";
    //用户信息
    public userInfo: any;
    constructor(public navCtrl: NavController,
        public navParams: NavParams,
        public platform: Platform,
        public appService: AppService,
        public nativeService: NativeService,
        private storage: Storage,
        private app: App
    ) {
        this.storage.get('userInfo').then(val => {
            if (Utils.isNotEmpty(val)) {
                console.log(val);
                this.userInfo = val;
                this.username = this.userInfo.nick_name;
                this.logintime = this.userInfo.logintime;
            }
        });
    }
    ionViewWillEnter(){ 
        this.appService.httpRequest(Api.api + Api.UserOnlineList, Method.Get, '', '', result => {
            if(result.data[0].loginStatus == 1) {
                this.signStatus = '已签到';
              } else {
                this.signStatus = '未签到';
              }
          });
        this.nativeService.getVersionNumber().then((data) => {
            this.version = data;
        });
    }
    /**
     * 修改密码
     */
    uptPwd() {
        this.navCtrl.push('SettingPwdPage', { userInfo: this.userInfo });
    }
    /**
     * 主题颜色
     */
    setTheme() {
        this.navCtrl.push('SettingThemePage');
    }
    /**
       * 退出APP
       */
    exit() {
        this.app.getRootNav().setRoot('LoginPage');
    }
}
