import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Platform, App } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { AppService } from "../../services/App.service";
import { Storage } from '@ionic/storage';
import { Utils } from '../../services/Utils';
import { NativeService } from "../../services/NativeService"
/**
 * Generated class for the HomePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-home',
  templateUrl: 'home.html',
})
export class HomePage {
  public menuModel: any;
  public menuRole: any = [];
  public nickName: string;

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public platform: Platform,
    public statusBar: StatusBar,
    public appService: AppService,
    public storage: Storage,
    public nativeService: NativeService,
    private app: App,

  ) {
    console.log('父组件');
  }

  ionViewWillEnter() {
    console.log('父组件生命周期');
    //权限过滤器
    this.storage.get('routes').then(val => {
      if (Utils.isNotEmpty(val)) {
        this.menuRole = val.children;
        // if (this.menuRole.length < 15) {
        //   let count = 15 - this.menuRole.length;
        //   for (let index = 0; index < count; index++) {
        //     this.menuRole.push({ source: '' });
        //   }
        // }
      }
    });
    this.statusBar.backgroundColorByHexString("#33cbcc"); // => #FFAABB
    this.storage.get('userInfo').then(val => {
      if (Utils.isNotEmpty(val)) {
        this.nickName = val.nick_name;
        // this.password = val.password;
      }
    });
  }
  expression(event) {
    // if (event.path === 'CheckinhomePage') {//收货
    //   if (Utils.isEmpty(event.children)) {
    //     this.nativeService.showToast('当前用户不存在收货子权限');
    //     return;
    //   }
    //   this.navCtrl.push(event.path, { menuRole: event.children });
    // } else if (event.path === 'TakeawayhomePage') {//拣货
    //   if (Utils.isEmpty(event.children)) {
    //     this.nativeService.showToast('当前用户不存在拣货子权限');
    //     return;
    //   }
    //   this.navCtrl.push(event.path, { menuRole: event.children });
    // } else if (event.path === 'CyclecounthomePage') {//盘点
    //   if (Utils.isEmpty(event.children)) {
    //     this.nativeService.showToast('当前用户不存在盘点子权限');
    //     return;
    //   }
    //   this.navCtrl.push(event.path, { menuRole: event.children });
    // } else if (event.path === 'MovehomePage') {//移动
    //   if (Utils.isEmpty(event.children)) {
    //     this.nativeService.showToast('当前用户不存在移动子权限');
    //     return;
    //   }
    //   this.navCtrl.push(event.path, { menuRole: event.children });
    // } else if (event.path === 'StockhomePage') {//库存
    //   if (Utils.isEmpty(event.children)) {
    //     this.nativeService.showToast('当前用户不存在移动子权限');
    //     return;
    //   }
    //   this.navCtrl.push(event.path, { menuRole: event.children });
    // } else if (event.path == 'ShipmenthomePage') {//装车
    //   if (Utils.isEmpty(event.children)) {
    //     this.nativeService.showToast('当前用户不存在装车子权限');
    //     return;
    //   }
    //   this.navCtrl.push(event.path, { menuRole: event.children });
    // } else if (event.path == 'PutawayhomePage') {//上架
    //   if (Utils.isEmpty(event.children)) {
    //     this.nativeService.showToast('当前用户不存在装车子权限');
    //     return;
    //   }
    //   this.navCtrl.push(event.path, { menuRole: event.children });
    // } eis.navCtrl.push(event.path);
    // }lse {
    //   th
    // if (Utils.isEmpty(event.children)) {
    //   this.nativeService.showToast('当前用户不存在装车子权限');
    //   return;
    // }
    if (Utils.isEmpty(event.children)) {
      this.navCtrl.push(event.path);
    } else {
      if (event.children.length == 1) {
        this.navCtrl.push(event.children[0].path);
      }
      else {
        this.navCtrl.push(event.path, { menuRole: event.children });
      }
    }

  }
  /**
   * 个人设置
   */
  userSetting() {
    this.navCtrl.push('SettingPage');
  }

  /**
   * 退出APP
   */
  detailed() {
    //this.navCtrl.pop();
    this.app.getRootNav().setRoot('LoginPage');
  }
  exit() {
    this.platform.exitApp();
  }
}
