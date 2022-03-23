import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
import { AppService } from "../../../services/App.service";

/**
 * Generated class for the CyclecountPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-cyclecountrecord',
  templateUrl: 'cyclecountrecord.html',
})
export class CyclecountRecordPage {
  public countBillNo: string;
  public current: number = 1;//当前页
  public size: number = 10;//数量
  public records: any = [];
  public skuLotNum:any;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService, ) {
    if (Utils.isNotEmpty(this.navParams.get('countBillNo'))) {
      this.countBillNo = this.navParams.get('countBillNo');
      this.skuLotNum = Array(30).fill(0).map((x,i)=>i);
    }
  }

  range(item:any) { 
    var arr = [];
    for (var i = 1; i <= 30; i++) {
      if(item[`skuLot${i}`]&&item[`skuLot${i}`]!=''){
        arr.push(item[`skuLot${i}`]);
      }
    }
    return arr;
  }

  /**
   * 获取盘点记录
   */
  getCountRecords(infiniteScroll) {
    let params = {
      current: this.current.toString(),
      size: this.size.toString(),
      countBillNo: this.countBillNo
    };
    this.appService.httpRequest(Api.StockCountPDA + Api.countRecords, Method.Get, params, '', result => {
      this.records = this.records.concat(result.data.records);
      this.current++;
      if (infiniteScroll) {
        infiniteScroll.complete();   /*请求完成数据更新页面*/
        if (result.data.records< this.size) {   /*没有数据 禁用上拉更新*/
          infiniteScroll.enable(false);
        }
      }
    });
  }
  doInfinite(infiniteScroll) {  /*接收事件对象传值*/
    this.getCountRecords(infiniteScroll);

  }
  ionViewDidLoad() {
    this.getCountRecords('');
  }

}
