import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService"
import { Api, ContenType, Method } from '../../../utils/appConstant';
import { Component, ViewChild } from '@angular/core';

/**
 * Generated class for the moveStock page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-movestock',
  templateUrl: 'movestock.html',
})
export class moveStock {
  public isShow: string = 'warehouse';
  public warhouser: any;
  public warhouserItem: any;
  public whName: string;
  public beforeLocCode: string;//原库位
  public beforelpnCode: string;//原容器
  public IsbeforeLocCode: string;
  public IsbeforelpnCode: string;
  public targetLocCode: string;//目标库位
  public IstargetLocCode: string;//目标库位
  @ViewChild('vbeforelpnCode') vbeforelpnCode;
  @ViewChild('vbeforeLocCode') vbeforeLocCode;
  constructor(public navCtrl: NavController, public navParams: NavParams, public appService: AppService,
    public nativeService: NativeService) {
    this.appService.httpRequest(Api.api + Api.ApiPDA, Method.Post, '', ContenType.Form, result => {
      if (Utils.isNotEmpty(result.data)) {
        this.warhouser = result.data;
      }
    });
  }
  clearModel() {
    this.warhouserItem = null;
    this.whName = '';
    this.beforeLocCode = '';
    this.beforelpnCode = '';
    this.IsbeforeLocCode = '';
    this.IsbeforelpnCode = '';
    this.targetLocCode = '';
    this.IstargetLocCode = '';
  }
  toggle() {
    this.whName = this.warhouserItem.whName;
  }
  exit() {
    if (this.isShow == 'before') {
      this.isShow = 'warehouse';
    } else if (this.isShow == 'target') {
      this.isShow = 'before';
    } else {
      this.navCtrl.pop();
    }
  }
  /**
  * 原库位回车事件
  */
  beforeLocCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      this.IsbeforeLocCode = this.beforeLocCode;
      this.beforeLocCode = '';
      this.vbeforelpnCode.setFocus();
    }
  }
  beforelpnCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      //查询当前容器库位编码
      if (Utils.isEmpty(this.beforelpnCode)) {
        this.nativeService.showToast('原容器不能为空');
        return;
      }
      this.appService.httpRequest(Api.api + Api.stockGetLocByLpnCode, Method.Post, 'lpnCode=' + this.beforelpnCode, ContenType.Form, result => {
        if (Utils.isNotEmpty(result.data)) {
          this.IsbeforelpnCode = this.beforelpnCode;
          this.IsbeforeLocCode = result.data.locCode;
          this.beforelpnCode = '';
        } else {
          this.nativeService.showToast('当前库房不存在该容器');
        }
      });
    }
  }
  btnOk() {
    let params = {
      lpnCode: this.IsbeforelpnCode,
      location: {
        locCode: this.IsbeforeLocCode
      }
    }
    if (this.isShow == 'warehouse') {
      if (Utils.isEmpty(this.warhouserItem)) {
        this.nativeService.showToast('请选择库房');
        return;
      }
      this.isShow = 'before';
    } else if (this.isShow == 'before') {
      if (Utils.isEmpty(this.IsbeforelpnCode)) {
        this.nativeService.showToast('容器编码不能为空');
        return;
      }
      if (Utils.isEmpty(this.IsbeforeLocCode)) {
        this.nativeService.showToast('库位编码不能为空');
        return;
      }
      this.isShow = 'target';
      // this.appService.httpRequest(Api.api + Api.StockDetail, Method.Post, params, ContenType.Json, result => {
      //   if (Utils.isNotEmpty(result.data)) {

      //   } else {
      //     this.nativeService.showToast('当前库房不存容器和库位');
      //   }
      // });
    } else {
      //托盘移动提交
      if (Utils.isEmpty(this.targetLocCode)) {
        this.nativeService.showToast('目标库位不能为空');
        return;
      }
      let params = {
        lpnCode: this.IsbeforelpnCode,
        locCode: this.targetLocCode,
        whId: this.warhouserItem.whId
      }
      //let body = "locCode=" + this.targetLocCode + "&lpnCode=" + this.IsbeforelpnCode;
      this.appService.httpRequest(Api.Outstock + Api.submitMOVE, Method.Post, params, ContenType.Json, result => {
        if (result.data) {
          this.nativeService.showToast('转移成功');
          this.isShow = 'warehouse';
          this.clearModel();
        } else {
          this.nativeService.showToast('移库失败');
        }
      });
    }
  }
}
