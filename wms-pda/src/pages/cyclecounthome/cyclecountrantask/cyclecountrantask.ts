import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, Events } from 'ionic-angular';
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService";
import { Api, ContenType, Method, BaseCodeConstant, AppConstant } from '../../../utils/appConstant';
import { Storage } from '@ionic/storage';
import { ArgumentType } from '@angular/core/src/view';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { AppGlobal } from '../../../services/AppGlobal';
/**
 * Generated class for the CyclecountrantaskPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-cyclecountrantask',
  templateUrl: 'cyclecountrantask.html',
})
export class CyclecountrantaskPage {
  public serialNumber: number;
  public IsShow: number;
  public Batch: number;
  public subHeader: any;
  public result: any;
  public subHeaderModel: string = '全部';
  public taskResult: any;//库位列表
  public taskResultAll: any;//库位列表(全部)
  public taskModel: any;//当前选中库位
  public sysParam: any;
  public locCode: string; //库位编号
  public locStatusDesc: string;
  public whName: string;
  public refreshType: string = '';

  public isModel: boolean = true;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public storage: Storage,
    public ngZone: NgZone,
    public events: Events,
    public barcodeService: BarcodeService
  ) {
    this.subHeader = [
      { taskType: '', taskName: '全部' },
      // { taskType: BaseCodeConstant.UnAllot, taskName: '未分配' },
      { taskType: BaseCodeConstant.UnComplate, taskName: '未完成' },
      { taskType: BaseCodeConstant.Complated, taskName: '已完成' },
    ];
    this.subHeaderModel = this.subHeader[0];

    if (Utils.isNotEmpty(this.navParams.get('result'))) {
      let result = this.navParams.get('result');
      //console.log(result, 'aaa');
      this.taskResult = result;
      this.taskResultAll = result;
    }
  }
  // ionViewWillEnter() {
  //   //事件注册（扫描结果接收）
  //   this.events.subscribe('barcode:scan', (barcode, time) => {
  //     let bt = this.barcodeService.GetBarcodeType(barcode);
  //     if (bt == BarcodeType.HuoWei) {
  //       this.ngZone.run(() => {

  //       });
  //     }
  //   });
  // }
  // ionViewWillLeave() {
  //   AppGlobal.removeSubscribe(this);
  // }
  tapEvent(item) {
    this.taskModel = item;
  }
  /**
   * 
   * @param 库位回车事件 
   */
  locCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isEmpty(this.locCode)) {
        this.taskResult = this.taskResultAll;
      }
      this.taskResult = this.taskResultAll.filter(x => x.locCode == this.locCode);
    }
  }
  headSelected(item) {
    this.subHeaderModel = item;
    this.refreshType = item.taskType;
    //筛选库位
    if (item.taskName == '全部') {
      this.taskResult = this.taskResultAll;
    } else {
      this.taskResult = this.taskResultAll.filter(x => x.locStatusDesc == item.taskName);
    }
  }
  ionViewDidLoad() {
    // this.nativeService.showLoading();
    // this.cyclecountrantask('');
    // console.log('ionViewDidLoad CyclecountrantaskPage');
  }
  onClick(item) {
    this.navCtrl.push('RandomCheckTaskPage', { result: item });
  }
  detailed() {
    this.navCtrl.push('CyclecountRecordPage',
    {countBillNo:this.taskResult[0].countBillNo});
  }
  /**
  * 返回
  */
  exit() {
    this.navCtrl.pop();
  }
  /**
   * 完成
   */
  btnOk() {
    let locList = this.taskResultAll.filter(x => x.isModel == true);
    let locstr = [];
    if (Utils.isNotEmpty(locList)) {
      locList.forEach(element => {
        locstr.push(element.countDetailId);
      });
    }
    if (locstr.length <= 0) {
      this.nativeService.showToast('请选择要完成的库位');
      return;
    }
    let params = {
      ids: locstr
    }
    let body = "ids=" + locstr;
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.StockCountPDA + Api.completeTask, Method.Post, body, ContenType.Form, result => {
      this.nativeService.hideLoading();
      if (result.data.countBillState == 30) {
        this.nativeService.showToast('提交成功');
        this.navCtrl.pop();
      } else {
        this.taskResult = result.data.countDetailVOList;;
        this.taskResultAll = result.data.countDetailVOList;;
        this.nativeService.showToast('提交成功');
      }
    })
  }
}
