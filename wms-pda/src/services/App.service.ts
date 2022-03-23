import { Injectable } from "@angular/core";
import { LoginUserInfoModel } from './../models/SystemFramework.Model';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { AlertController, App, ToastController, DateTime } from "ionic-angular";
import { CommonService } from '../utils/common';
import { Storage } from '@ionic/storage';
import { Utils } from "./Utils";
import { NativeService } from './NativeService';
import { APPCONSTANT, Api, ContenType, Method } from '../utils/appConstant';
import { AppGlobal } from '../services/AppGlobal';
@Injectable()
export class AppService {
  sysDate: Date;
  constructor(public http: HttpClient,
    private alertCtrl: AlertController,
    private toastCtrl: ToastController,
    public common: CommonService,
    public app: App,
    public storage: Storage,
    public nativeService: NativeService) {

  }

  setApiIp(ip) {
    this.common.setData('apiIp', ip);
  }
  setTenantId(id) {
    this.common.setData('tenantId', id);
  }
  async httpRequest(url, method, body, ContentType, callback) {
    if (Utils.isEmpty(this.sysDate)) {
      this.sysDate = new Date();
      this.httpRequestExecute(url, method, body, ContentType, callback);
    } else {
      let currentTime = new Date();
      let Seconds = (currentTime.getTime() - this.sysDate.getTime()) / 1000;
      if (Seconds >= 600) {
        //刷新token
        this.storage.get('userInfo').then(val => {
          if (Utils.isNotEmpty(val)) {
            let bodyRef = "grant_type=refresh_token" + "&refresh_token=" + val.refresh_token + "&tenantId=" + '000000'
            this.refreshToken(Api.api + Api.token, Method.Post, bodyRef, ContenType.Form, result => {
              this.storage.set('access_token', result.access_token).then(val => {
                this.storage.set('userInfo', result).then(valUser => {
                  this.sysDate = new Date();
                  this.httpRequestExecute(url, method, body, ContentType, callback);
                })
              });
            })
          }
        });
      } else {
        this.httpRequestExecute(url, method, body, ContentType, callback);
      }
    }
  }
  httpRequestExecute(url, method, body, ContentType, callback) {
    let requestHeader: HttpHeaders;
    this.storage.get('access_token').then((val) => {
      if (!Utils.isEmpty(val)) {
        requestHeader = new HttpHeaders({
          'Method': method,
          'Content-Type': ContentType,
          'Authorization': 'Basic c2FiZXI6c2FiZXJfc2VjcmV0',
          'isNeedReLogin': 'true',
          'Blade-Auth': 'bearer ' + val,
          'isReady': '',
          'observe': 'response'
        });
      } else {
        requestHeader = new HttpHeaders({
          'Method': method,
          'Content-Type': ContentType,
          'Authorization': 'Basic c2FiZXI6c2FiZXJfc2VjcmV0',
          'isNeedReLogin': 'true',
          'isReady': '',
        });
      }
      this.storage.get('apiIp').then(val => {
        let apiIp = "http://" + val;
        //封装get请求url
        if (method != Method.Post) {
          if (Utils.isNotEmpty(body)) {
            let parmsArr = Object.keys(body);
            if (parmsArr.length == 1) {
              // url += '?' + parmsArr[0] + '=' + Utils.urlFormat(body[parmsArr[0]]);
              url += '?' + parmsArr[0] + '=' + body[parmsArr[0]];
            } else {
              for (let index = 0; index < parmsArr.length; index++) {
                if (index == 0) {
                  url += '?' + parmsArr[0] + '=' + body[parmsArr[0]];
                } else {
                  url += '&' + parmsArr[index] + '=' + body[parmsArr[index]];
                }
              }
            }
          }
        }
        this.http.request(
          method,
          apiIp + url,
          {
            observe: 'response',
            headers: requestHeader,
            responseType: "json",
            body: body,
          }).subscribe(response => {
            this.nativeService.hideLoading();
            if (Utils.isNotEmpty(response.body['error_code'])) {
              this.toast(response.body['error_description']);
              this.nativeService.hideLoading();
              // this.nativeService.hide();
              return;
            }
            if (Utils.isNotEmpty(response.body['code']) && response.body['code'] == '401') {
              this.toast(response.body['msg']);
              this.nativeService.hideLoading();
              return;
            }
            if (Utils.isNotEmpty(response.body['code']) && response.body['code'] == '500') {
              this.toast(response.body['msg']);
              this.nativeService.hideLoading();
              return;
            }
            callback(response.body == null ? null : response.body);
          },
            error => {
              this.handleError(error)
            });
      });
    });
  }
  async refreshToken(url, method, body, ContentType, callback) {
    let requestHeader: HttpHeaders;
    requestHeader = new HttpHeaders({
      'Method': method,
      'Content-Type': ContentType,
      'Authorization': 'Basic c2FiZXI6c2FiZXJfc2VjcmV0',
      'isNeedReLogin': 'true',
      'isReady': ''
    });

    this.storage.get('apiIp').then(val => {
      let apiIp = "http://" + val;
      //封装get请求url
      if (method != Method.Post) {
        if (Utils.isNotEmpty(body)) {
          let parmsArr = Object.keys(body);
          if (parmsArr.length == 1) {
            url += '?' + parmsArr[0] + '=' + Utils.urlFormat(body[parmsArr[0]]);
          } else {
            for (let index = 0; index < parmsArr.length; index++) {
              if (index == 0) {
                url += '?' + parmsArr[0] + '=' + Utils.urlFormat(body[parmsArr[0]]);
              } else {
                url += '&' + parmsArr[index] + '=' + Utils.urlFormat(body[parmsArr[index]]);
              }
            }
          }
        }
      }
      this.http.request(
        method,
        apiIp + url,
        {
          observe: 'response',
          headers: requestHeader,
          responseType: "json",
          body: body,
        }).subscribe(response => {
          this.nativeService.hideLoading();
          if (Utils.isNotEmpty(response.body['error_code'])) {
            this.toast(response.body['error_description']);
            this.nativeService.hideLoading();
            return;
          }
          if (Utils.isNotEmpty(response.body['code']) && response.body['code'] == '401') {
            this.toast(response.body['msg']);
            this.nativeService.hideLoading();
            return;
          }
          if (Utils.isNotEmpty(response.body['code']) && response.body['code'] == '500') {
            this.toast(response.body['msg']);
            this.nativeService.hideLoading();
            return;
          }
          callback(response.body == null ? null : response.body);
        },
          error => {
            this.handleError(error)
          });
    });
  }

  async handleError(error: Response | any) {
    AppGlobal.scanFlag = true;
    let msg = '';
    this.nativeService.hideLoading();
    if (error.status == 401) {
      let userInfo: LoginUserInfoModel = await this.storage.get('userInfo');

      this.common.setData('userInfo', userInfo);
      this.toast(error.msg);
      this.app.getRootNav().push('LoginPage');
      this.app.getRootNav().setRoot('LoginPage');
      return;
    }
    if (error.status == 404) {
      msg = '请求资源不存在(code：404)';
    }
    if (error.status == 500 || error.status == 400) {
      msg = error.error.msg;
    }
    if (msg != '') {
      this.toast(msg);
    }
  }

  alert(message, callback?) {
    if (callback) {
      let alert = this.alertCtrl.create({
        title: '提示',
        message: message,
        buttons: [{
          text: "确定",
          handler: data => {
            callback();
          }
        }]
      });
      alert.present();
    } else {
      let alert = this.alertCtrl.create({
        title: '提示',
        message: message,
        buttons: ["确定"]
      });
      alert.present();
    }
  }

  toast(message, callback?) {
    let toast = this.toastCtrl.create({
      message: message,
      duration: 2000,
      position: 'middle',
      dismissOnPageChange: true,
    });
    toast.present();
    if (callback) {
      callback();
    }
  }

}
