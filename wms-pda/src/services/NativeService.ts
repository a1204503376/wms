import { Injectable } from "@angular/core";
import { ToastController, LoadingController, Platform, Loading, AlertController, Alert } from "ionic-angular";
import { StatusBar } from "@ionic-native/status-bar";
import { SplashScreen } from "@ionic-native/splash-screen";
import { APPCONSTANT } from "../utils/appConstant";
import { CommonService } from "../utils/common";
import { Toast } from "@ionic-native/toast";
import { FileTransfer, FileUploadOptions, FileTransferObject } from '@ionic-native/file-transfer';
import { File } from '@ionic-native/file';
import { AppVersion } from '@ionic-native/app-version';
import { FileOpener } from '@ionic-native/file-opener';

@Injectable()
export class NativeService {
  private loading: Loading;
  private loadingIsOpen: boolean = false;


  constructor(private platform: Platform,
    private toastCtrl: ToastController,
    private statusBar: StatusBar,
    private splashScreen: SplashScreen,
    private toast: Toast,
    private loadingCtrl: LoadingController,
    private common: CommonService,
    public transfer: FileTransfer,
    public file: File,
    public appVersion: AppVersion,
    public fileOpener: FileOpener,
    public alertControl: AlertController
  ) {
  }


  /**
   * 使用默认状态栏
   */
  statusBarStyleDefault(): void {
    this.statusBar.styleDefault();
  }


  /**
   * 隐藏启动页面
   */
  splashScreenHide(): void {
    this.splashScreen.hide();
  }


  /**
   * 是否真机环境
   */
  isMobile(): boolean {
    return this.platform.is('mobile') && !this.platform.is('mobileweb');
  }

  /**
   * 是否android真机环境
   */
  isAndroid(): boolean {
    return this.isMobile() && this.platform.is('android');
  }

  /**
   * 是否ios真机环境
   */
  isIos(): boolean {
    return this.isMobile() && (this.platform.is('ios') || this.platform.is('ipad') || this.platform.is('iphone'));
  }

  alert(title: string, subTitle: string = "",): void {
    this.alertControl.create({
      title: title,
      subTitle: subTitle,
      buttons: [{ text: '确定' }]
    }).present();
  }

  /**
   * 统一调用此方法显示提示信息
   * @param message 信息内容
   * @param duration 显示时长
   */
  showToast(message: string = '操作完成', duration: number = 3000): void {
    this.hideLoading();
    if (this.isMobile()) {
      this.toast.show(message, String(duration), 'center').subscribe();
    } else {
      this.toastCtrl.create({
        message: message,
        duration: duration,
        position: 'middle',
        showCloseButton: false,
        cssClass: 'showToast'
      }).present();
    }
  };

  /**
   * 统一调用此方法显示loading
   * @param content 显示的内容
   */
  showLoading(content: string = ''): void {
    if (!this.common.showLoading) {
      return;
    }
    if (!this.loadingIsOpen) {
      this.loadingIsOpen = true;
      this.loading = this.loadingCtrl.create({
        content: content
      });
      this.loading.present();
      setTimeout(() => {
        this.loadingIsOpen && this.loading.dismiss();
        this.loadingIsOpen = false;
      }, APPCONSTANT.REQUEST_TIMEOUT);
    }
  };

  /**
   * 关闭loading
   */
  hideLoading(): void {
    if (!this.common.showLoading) {
      this.common.showLoading = true;
    }
    this.loadingIsOpen && this.loading.dismiss();
    this.loadingIsOpen = false;
  };
  show(content: string = '') {
    this.loading = this.loadingCtrl.create({
      content: content
    });
    this.loading.present();
  }
  // 隐藏loading
  hide() {
    if (this.loading) {
      this.loading.dismiss();
    }
  }
  /**n b
   */
  detectionUpgrade(APK_DOWNLOAD) {
    //这里连接后台获取app最新版本号,然后与当前app版本号(this.getVersionNumber())对比
    //版本号不一样就需要申请,不需要升级就return
    this.alertControl.create({
      title: '升级',
      subTitle: '发现新版本,是否立即升级？',
      buttons: [{ text: '取消' },
      {
        text: '确定',
        handler: () => {
          this.downloadApp(APK_DOWNLOAD);
        }
      }
      ]
    }).present();
  }
  /**
 * 下载安装app
 */
  downloadApp(APK_DOWNLOAD) {
    let url = encodeURI(APK_DOWNLOAD);
    let alert;
    if (this.isAndroid()) {
      alert = this.alertControl.create({
        title: '下载进度：0%',
        enableBackdropDismiss: false,
        buttons: ['后台下载']
      });
      alert.present();
      const fileTransfer: FileTransferObject = this.transfer.create();
      const apk = this.file.externalRootDirectory + 'WMSPDA.apk'; //apk保存的目录
      fileTransfer.download(url + 'WMSPDA.apk', apk).then((entry) => {
        this.fileOpener.open(apk, 'application/vnd.android.package-archive');
      }, (error) => {
        console.log(error);
      });
      fileTransfer.onProgress((event: ProgressEvent) => {
        let num = Math.floor(event.loaded / event.total * 100);
        if (num === 100) {
          alert.dismiss();
        } else {
          let title = document.getElementsByClassName('alert-title')[0];
          title && (title.innerHTML = '下载进度：' + num + '%');
        }
      });
    }
    if (this.isIos()) {
      //this.openUrlByBrowser(APP_DOWNLOAD);
    }
  }
  /**
 * 获得app版本号,如0.01
 * @description  对应/config.xml中version的值
 * @returns {Promise<string>}
 */
  getVersionNumber(): Promise<string> {
    return new Promise((resolve) => {
      this.appVersion.getVersionNumber().then((value: string) => {
        resolve(value);
      }).catch(err => {
        console.log('getVersionNumber:' + err);
      });
    });
  }

}
