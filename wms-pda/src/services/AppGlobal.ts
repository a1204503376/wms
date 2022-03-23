/**
 * Created by GimaiDev on 2017/8/25.
 */
import { LoadingController, AlertController, ToastController } from 'ionic-angular';
import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class AppGlobal {
  //缓存key的配置
  static cache: any = {};

  //本地调试接口（需本地代理跳转）
  static domain = "http://localhost:8100/api/";

  //正式版本接口
  // static domain = "http://114.215.129.212:5308/api/"

  //接口地址
  static API: any = {
    //用户登陆
    postLoginin: 'UserLogin/',
  };
  static scanFlag: boolean = true;
  
  static removeSubscribe(obj) {
    obj.events.unsubscribe('barcode:scan', null);
    this.scanFlag = true;
  }
}

