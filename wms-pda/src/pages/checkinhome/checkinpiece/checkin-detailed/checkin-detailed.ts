import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Utils } from '../../../../services/Utils';
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../../utils/appConstant';
import { AppService } from "../../../../services/App.service";

/**
 * Generated class for the CheckinDetailedPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-checkin-detailed',
  templateUrl: 'checkin-detailed.html',
})
export class CheckinDetailedPage {
  trailerTaskList: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9]
  public asnBillNo: string;
  public current: number = 1;//当前页
  public size: number = 10;//数量
  public records: any = [];
  public groupRecords: any = [];
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService, ) {
    if (Utils.isNotEmpty(this.navParams.get('asnBillNo'))) {//订单编号输入
      this.asnBillNo = this.navParams.get('asnBillNo');
    }
  }

  /**
   * 获取单据清点详情
   */
  getContainerList(infiniteScroll) {
    let params = {
      current: this.current.toString(),
      size: this.size.toString(),
      asnBillNo: this.asnBillNo
    };
    this.appService.httpRequest(Api.Instock + Api.ContainerLogList, Method.Get, params, '', result => {
      this.records = this.records.concat(result.data.records);
      if(Utils.isNotEmpty(this.records)){
        for(let item of this.records){
          if(Utils.isEmpty(this.groupRecords)){
            this.groupRecords.push(item);
          }else{
            let arr=this.groupRecords.find((v) => {
              return v.skuCode == item.skuCode;
            });
            if(Utils.isEmpty(arr)){
              this.groupRecords.push(item);
              break;
            }
          }
        }
      }
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
    this.getContainerList(infiniteScroll);

  }
  ionViewDidLoad() {
    this.getContainerList('');
  }

}
