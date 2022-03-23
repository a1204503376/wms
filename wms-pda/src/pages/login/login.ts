import {
  IonicPage,
  NavController,
  NavParams,
  Platform,
  Events,
} from "ionic-angular";
import { Component, ViewChild } from "@angular/core";
import { AppService } from "../../services/App.service";
import { Http } from "@angular/http";
import { Utils } from "../../services/Utils";
import { NativeService } from "../../services/NativeService";
import { Storage } from "@ionic/storage";
import { APPCONSTANT, Api, ContenType, Method } from "../../utils/appConstant";
import { CommonService } from "../../utils/common";
import { LoginUserInfoModel } from "../../models/SystemFramework.Model";
import { Jsonp } from "@angular/http";
import { StatusBar } from "@ionic-native/status-bar";
import { SysUpdateVerModel } from "../../models/SystemFramework.Model";
import { FileTransfer } from "@ionic-native/file-transfer";
import { File } from "@ionic-native/file";
import { AppVersion } from "@ionic-native/app-version";
import { FileOpener } from "@ionic-native/file-opener";
import { Printer } from "@ionic-native/printer";
import { Keyboard } from "@ionic-native/keyboard";
import { AppGlobal } from "../../services/AppGlobal";
import { Observable } from "rxjs/Rx";
import { Md5 } from "ts-md5";
declare let cordova: any;
//declare var hiprint: any;
/**
* Generated class for the LoginPage page.
*
* See https://ionicframework.com/docs/components/#navigation for more info on
* Ionic pages and navigation.
*/

@IonicPage()
@Component({
  selector: "page-login",
  templateUrl: "login.html",
})
export class LoginPage {
  // 用户名
  username: string = '';
  // 密码
  password: string = '';
  //版本
  version: string;
  //服务器地址
  ipaddress: string;
  isipaddress: boolean = false;
  //更新地址
  updateUrl: string;

  sysUpdateVerModel: SysUpdateVerModel;
  //登陆实体
  loginuser: LoginUserInfoModel;

  window: any = window;

  ceshiList: string[] = ["1", "2", "3", "4"];

  @ViewChild("user") myInputUser;

  @ViewChild("pwd") myInputPwd;

  public loginSetIcon: string = "settings";

  public isTenantId: boolean = false;
  public tenantId: any;
  constructor(
      public navCtrl: NavController,
      public navParams: NavParams,
      public appService: AppService,
      public http: Http,
      public nativeService: NativeService,
      public storage: Storage,
      public common: CommonService,
      public platform: Platform,
      public jsonp: Jsonp,
      public statusBar: StatusBar,
      public transfer: FileTransfer,
      public file: File,
      public appVersion: AppVersion,
      public fileOpener: FileOpener,
      private events: Events,
      private printer: Printer,
      public keyboard: Keyboard
  ) {
  }
  async initData(){
      let obj =await this.storage.get("userInfo");
      if(obj){
          this.username = obj.user_name;
      }
      if(this.nativeService.isMobile()){
          this.version = await this.nativeService.getVersionNumber();
      }
      this.ipaddress = await this.storage.get("apiIp");
      if(Utils.isEmpty(this.ipaddress)){
          this.ipaddress = APPCONSTANT.ServerIP1;
          this.storage.set("apiIp", this.ipaddress);
      }
      this.paramList();
  }



  ionViewDidLoad() {}
  ionViewWillEnter() {
      //事件注册（扫描结果接收）
      // this.events.subscribe("barcode:scan", (barcode, time) => {
      //     alert(barcode);
      // });
      this.statusBar.backgroundColorByHexString("#33cbcc"); // => #FFAABB
      this.initData();
  }

  ionViewWillLeave() {
      // this.events.unsubscribe('barcode:scan', null);
      // this.events.unsubscribe('keyBoard:scan', null);
      // AppGlobal.removeSubscribe(this);
  }

  /**
   * 用户名输入框按下回车输入焦点移动
   */
  keyEnter(event) {
      if (event && event.keyCode == 13) {
          this.myInputPwd.setFocus();
      }
  }

  /**
   * 密码输入框按下执行登陆
   */
  onLogin(event) {
      if (event && event.keyCode == 13) {
          this.loginIn();
      }
  }
  loginIn2() {
      console.log("测试23");
      alert("测试");
  }
  /**
   * 输入校验
   */
  checkInput() {
      if (Utils.isEmpty(this.username) || Utils.isEmpty(this.password)) {
          this.nativeService.showToast("请输入用户名和密码！");
          return false;
      }
      return true;
  }

  async loginIn() {
      if (Utils.isEmpty(this.tenantId)) {
          let data = await this.storage.get("tenantId");
          if (Utils.isNotEmpty(data)) {
              this.tenantId = data;
          } else {
              this.tenantId = APPCONSTANT.TENANTID;
          }
      }
      if (!this.checkInput()) {
          return;
      }
      let body =
          "username=" +
          this.username +
          "&password=" +
          Md5.hashStr(this.password) +
          "&tenantId=" +
          this.tenantId;
      this.appService.setApiIp(this.ipaddress);
      this.appService.setTenantId(this.tenantId);
      this.nativeService.showLoading();
      //用户登录
      this.appService.httpRequest(
          Api.api + Api.token,
          Method.Post,
          body,
          ContenType.Form,
          (result) => {
              this.changePage(result);
              this.storage
                  .set("access_token", this.loginuser.access_token)
                  .then((val) => {
                      //获取权限
                      this.appService.httpRequest(
                          Api.api + Api.routes,
                          Method.Get,
                          "",
                          "",
                          (resultrole) => {
                              this.nativeService.hideLoading();
                              let resultInfo = resultrole.data.filter(
                                  (x) => x.systemType == 0
                              );
                              if (Utils.isEmpty(resultInfo)) {
                                  this.nativeService.showToast(
                                      "当前用户不存在手持权限"
                                  );
                                  return;
                              }
                              if (Utils.isEmpty(resultInfo[0].children)) {
                                  this.nativeService.showToast(
                                      "当前用户不存在页面权限"
                                  );
                                  return;
                              }
                              this.common.setData("routes", resultInfo[0]);
                              this.navCtrl.setRoot("HomePage");
                          }
                      );
                  });
          }
      );
  }
  TextBlur(type) {
      if (type === 8) {
         this.paramList();
      } 
  }
  paramList() {
      // let result = await this.storage.get("apiIp");
      // debugger
      // if (Utils.isNotEmpty(result)) {
          // this.ipaddress = result;
      // } else {
          // this.ipaddress = APPCONSTANT.ServerIP1;
          // this.storage.set("apiIp", this.ipaddress);
      // }
      this.appService.httpRequest(
          Api.api + Api.ParamList,
          Method.Get,
          {},
          ContenType.Json,
          (result) => {
              this.common.setData("sysParam", result.data);
              let flag = false;
              Observable.from(result.data)
                  .filter((obj) => obj["paramKey"] == "system.tenant_id")
                  .subscribe({
                      next: (val) => {
                          if (val["paramValue"] != "") {
                              this.tenantId = val["paramValue"];
                              flag = true;
                          }
                      },
                      complete: () => {
                          // if (!flag) {
                          //     this.isTenantId = true;
                          // }
                          if(this.nativeService.isMobile()){
                              this.updateApp();
                          }
                      },
                  });
          }
      );
  }
  /**
   *数据保存，页面跳转
   */
  changePage(data) {
      this.appService.setApiIp(this.ipaddress);
      this.loginuser = data;
      this.loginuser.logintime = Utils.dateFormat(new Date(), "yyyy-MM-dd");
      // this.common.setData('access_token', this.loginuser.access_token);
      this.loginuser["password"] = this.password;
      this.common.setData("userInfo", this.loginuser);
      this.common.setlogin(true);
      
  }
  isAddress() {
      if (this.isipaddress) {
          this.isipaddress = false;
      } else {
          this.isipaddress = true;
      }
  }
  updateApp() {
      //在线升级
      let params = {
          moduleType: "3",
      };
      this.appService.httpRequest(
          Api.api + Api.UpdateVerDetail,
          Method.Post,
          params,
          ContenType.Json,
          (result) => {
              if (Utils.isNotEmpty(result.data)) {
                  this.updateUrl = result.data.updateUrl;
                  if (Utils.isNotEmpty(this.version)) {
                      if (result.data.verName == this.version) return;
                      this.nativeService.detectionUpgrade(
                          result.data.updateUrl
                      );
                  }
              }
          }
      );
  }
  /**
   * 退出APP
   */
  exit() {
      this.platform.exitApp();
  }
}
