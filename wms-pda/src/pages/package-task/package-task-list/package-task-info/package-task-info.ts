import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Utils } from '../../../../services/Utils';
import { AppService } from "../../../../services/App.service";
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../../utils/appConstant';
import { NativeService } from "../../../../services/NativeService"

/**
 * Generated class for the PackageTaskInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-package-task-info',
  templateUrl: 'package-task-info.html',
})
export class PackageTaskInfoPage {

  public isTilebool: boolean = true;

  public taskId: string;

  public stockPackVOList: any;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService
  ) {
    if (Utils.isNotEmpty(this.navParams.get('taskId'))) {
      this.taskId = this.navParams.get('taskId');
    };
  }

  ionViewDidLoad() {
    this.packStockList('20');
  }
  /**
   * 打包记录
   */
  packStockList(packState: string) {
    let params = {
      packState: packState,
      taskId: this.taskId
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.stockInner + Api.packStockList, Method.Get, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      this.stockPackVOList = result.data;
    })
  }
  isTitle(event) {
    this.isTilebool = event;
    if (event) {
      this.packStockList('20');
    } else {
      this.packStockList('10');
    }
  }
  exit() {
    this.navCtrl.pop();
  }
}
