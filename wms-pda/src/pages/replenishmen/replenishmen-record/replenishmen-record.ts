import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { ScanModel } from "../../../models/DataBase.Model";
import { AppService } from "../../../services/App.service";
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
import { NativeService } from "../../../services/NativeService"
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { s } from '@angular/core/src/render3';
import { SelectItem } from 'primeng/api';
import { AppGlobal } from '../../../services/AppGlobal';

@IonicPage()
@Component({
  selector: 'page-replenishmen-record',
  templateUrl: 'replenishmen-record.html',
})
export class ReplenishmenRecordPage {
  result: any = {};
  selectItem: any = {};
  flag:boolean = false;
  selectModel: any = {}; 
  callback:any;
  taskId:string;
  @ViewChild('labelTypecom') labelTypecom;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public modalController: ModalController,
    public appService: AppService,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    public events: Events,
    public ngZone: NgZone
  ) {
    if (Utils.isNotEmpty(this.navParams.get('result'))) {
      this.result = this.navParams.get('result');
    };
    if (Utils.isNotEmpty(this.navParams.get('flag'))) {
      this.flag = this.navParams.get('flag');
    };
    if (Utils.isNotEmpty(this.navParams.get('callback'))) {
      this.callback = this.navParams.get('callback');
    };
    if (Utils.isNotEmpty(this.navParams.get('taskId'))) {
      this.taskId = this.navParams.get('taskId');
    };
    if (Utils.isNotEmpty(this.navParams.get('currentItem'))) {
      this.selectModel = this.navParams.get('currentItem');
      this.resetSelected();
      let items = this.result.detailList.filter(item=>item.relDetailId==this.selectModel.relDetailId);
      items[0]['selected']=true;
      this.selectItem = items[0];
    };
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
 * 选择要处理的入库单
 */
    resetSelected(){
      for(let item of this.result.detailList){
        item['selected'] = false;
      }
    }
    headSelected(item) {
      this.resetSelected();
      item['selected'] = true;
      this.selectItem = item;
    }
  ionViewWillEnter() {
    if(this.flag){
      this.finishDetailed();
    }
  }
  finishDetailed() {
    this.appService.httpRequest(Api.stockInner + Api.getUnfinishReplenishmenList, Method.Get, {
      taskId: this.taskId
    },
      ContenType.Content, result => {
        if (Utils.isNotEmpty(result.data.detailList)) {
          this.result = result.data;
        } else {
          this.nativeService.showToast('暂无补货记录');
        }
      });
  }
  ionViewWillLeave() {
  }
  ionViewDidLoad() {
  }
  exit(){
    this.navCtrl.pop();
  }
  btnOk() {
    if (Utils.isNotEmpty(this.selectItem)) {
      this.callback(this.selectItem).then((result)=>{
        this.navCtrl.pop();
      },(error)=>{
        console.log("error",error)
      })
    }
  }
}
