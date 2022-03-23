import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Utils } from '../../../../services/Utils';
import { AppService } from "../../../../services/App.service";
import { Api, ContenType, Method } from '../../../../utils/appConstant';
import { NativeService } from "../../../../services/NativeService"

/**
 * Generated class for the StockPritPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-stock-prit',
  templateUrl: 'stock-prit.html',
})
export class StockPritPage {

  stockVOListIsTrue: any;//选中要打印的库存信

  num: number = 0;

  labelTypeList: any;//标签类型集合

  constructor(public navCtrl: NavController, public navParams: NavParams,
    public appService: AppService, public nativeService: NativeService) {
    if (Utils.isNotEmpty(this.navParams.get('stockVOListIsTrue'))) {
      this.stockVOListIsTrue = this.navParams.get('stockVOListIsTrue');
    };
  }

  ionViewDidLoad() {
    this.appService.httpRequest(Api.bladeSystem + Api.dictList,
      Method.Get, { code: 'xdc_label_type', is_sealed: '0' }, ContenType.Form, result => {
        this.labelTypeList = result.data;
        for (let index = 0; index < this.stockVOListIsTrue.length; index++) {
          this.stockVOListIsTrue[index]['labelTypeList'] = this.labelTypeList;
          this.stockVOListIsTrue[index]['labelTypeItem'] = this.labelTypeList[0];
          this.stockVOListIsTrue[index]['printCount'] = 1;
        }
      });
  }
  /**
   * 打印功能
   */
  btnOk() {
    let params = [];
    this.stockVOListIsTrue.forEach(element => {
      params.push({
        stockId: element.stockId,
        labelType: element.labelTypeItem.dictKey,
        printCount: element.printCount,
      }
      );
    });
    this.nativeService.showLoading();
    this.appService.httpRequest('/api/ApiPDA/print/label', Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      this.nativeService.showToast('打印成功');
    })
  }
  /**
   * 返回上一级菜单
   */
  exit() {
    this.navCtrl.pop();
  }
}
