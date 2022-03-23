import { Component, ViewChild } from '@angular/core';
import { Platform, Nav, IonicApp } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { NativeService } from '../services/NativeService';
import { Events } from 'ionic-angular';
import { AndroidPermissions } from '@ionic-native/android-permissions';
import { Keyboard } from '@ionic-native/keyboard';
declare let cordova: any;
@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  rootPage: any = 'LoginPage';

  deviceBind: any;

  windows: any = Window;
  @ViewChild('myNav') nav: Nav;
  backButtonPressed: boolean = false;
  window: any = window;
  constructor(private platform: Platform,
    statusBar: StatusBar,
    splashScreen: SplashScreen,
    public nativeService: NativeService,
    public events: Events,
    public keyboard: Keyboard,
    public ionicApp: IonicApp,
    private androidPermissions: AndroidPermissions
  ) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      statusBar.backgroundColorByHexString("#ffffff"); // => #FFAABB
      // statusBar.overlaysWebView(true);
      this.nativeService.statusBarStyleDefault();
      this.nativeService.splashScreenHide();
      this.nativeService.statusBarStyleDefault();
      this.getPrmissions();
      this.nativeService.splashScreenHide();
      // //扫描初始化
      this.scanInit();
      //注册程序返回
      this.deviceResume();
      //注册程序中止
      this.closeScanHeader();
      //注册返回按钮
      this.registerBackButtonAction();
      // //注册键盘弹起
      // this.inikeycode();
    });
  }
  /**
* pda扫描初始化
*/
  scanInit() {
    //pda扫描初始化
    if (this.platform.is('android')) {
      this.deviceBind = this.onDeviceReady.bind(this);
      //初始化扫描
      document.addEventListener("deviceready", this.deviceBind);
    }
  }
  /**
   * 程序返回重新初始化
   */
  deviceResume() {
    this.platform.resume.subscribe(() => {
      this.scanInit();
    });
  }
  /**
   * pda扫描初始化
   */
  onDeviceReady() {
    this.initHardBarcode();
  }

  /**
   * 扫描结果返回
   */
  initHardBarcode() {
    let that = this;
    cordova.plugins.barCodePlugin.setReceiveScanCallback(function (data: any) {
      data = data.replace(/[\r\n]/g, "");
      that.events.publish('barcode:scan', data, Date.now());
    });
    // cordova.plugins.autoid9Plugin.initialise(() => {
    //   cordova.plugins.autoid9Plugin.setReceiveScanCallback(function (data: any) {
    //     // alert(data);
    //     data = data.replace(/[\r\n]/g, "");
    //     that.events.publish('barcode:scan', data, Date.now());
    //   });
    // });
  }

  /**
   *  取消事件注册
   */
  closeScanHeader() {
    this.platform.pause.subscribe(() => {
      if (this.platform.is('android')) {
        // cordova.plugins.autoid9Plugin.destroy(function () {
        // });
        //取消绑定
        document.removeEventListener("deviceready", this.deviceBind);
        this.deviceBind = null;
      }
    });
  }

  /**
   * 注册返回按键事件
   */
  registerBackButtonAction() {
    this.platform.registerBackButtonAction(() => {
      if (this.keyboard.isVisible) {//如果键盘开启则隐藏键盘
        this.keyboard.hide();
        return;
      }

      let activePortal = this.ionicApp._loadingPortal.getActive() ||
        this.ionicApp._modalPortal.getActive() ||
        this.ionicApp._toastPortal.getActive() ||
        this.ionicApp._overlayPortal.getActive();

      if (activePortal) {
        // ready = false;
        activePortal.dismiss();
        activePortal.onDidDismiss(() => {
        });

        console.log("handled with portal");
        return;
      }

      if (this.nav.canGoBack()) {
        this.nav.pop();
        return;
      } else {
        //执行退出
        this.showExit();
      }
    }, 1);
  }

  /**
   * 监听键盘是否弹起
   */
  // inikeycode() {
  //   let that = this;
  //   this.keyboard.onKeyboardWillShow().subscribe(() => {
  //     alert('false');
  //     that.events.publish('keyBoard:scan', false, Date.now());
  //   });
  //   this.keyboard.onKeyboardWillHide().subscribe(() => {
  //     that.events.publish('keyBoard:scan', true, Date.now());
  //     alert('true');
  //   });
  // }
  //双击退出提示框
  showExit() {
    if (this.backButtonPressed) { //当触发标志为true时，即2秒内双击返回按键则退出APP
      this.platform.exitApp();
    } else {
      this.nativeService.showToast('再按一次退出应用');
      this.backButtonPressed = true;
      setTimeout(() => { //2秒内没有再次点击返回则将触发标志标记为false
        this.backButtonPressed = false;
      }, 2000)
    }
  }
  getPrmissions() {
    this.androidPermissions.checkPermission(this.androidPermissions.PERMISSION.WRITE_EXTERNAL_STORAGE).then(
      (result) => {
        console.log('本机权限状态:' + result.hasPermission);
      }, (err) => {
        //申请手机权限
        this.androidPermissions.requestPermission(this.androidPermissions.PERMISSION.WRITE_EXTERNAL_STORAGE)
      });
    return new Promise((resolve) => {
      this.androidPermissions.requestPermissions([this.androidPermissions.PERMISSION.WRITE_EXTERNAL_STORAGE, this.androidPermissions.PERMISSION.GET_ACCOUNTS]).then((res) => {
        resolve(res);
      })
    })
  }
}

