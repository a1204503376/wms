import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, Events } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService"
import { AppService } from "../../../services/App.service";
import { Api, ContenType, Method } from '../../../utils/appConstant';
import { AppGlobal } from '../../../services/AppGlobal';
/**
 * Generated class for the TakeawaypiecePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-takeawaypiece',
  templateUrl: 'takeawaypiece.html',
})
export class TakeawaypiecePage {
  public taskId: string;//任务编号
  public wellenNo: string;//波次编号
  public PickInfoResult: any;//任务数据实体

  public pickPlansList: any;//任务物品集合
  public pickPlansModel: any;//当前选中任务

  //页面实体
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public nativeService: NativeService,
    public appService: AppService,
    public events: Events
  ) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad TakeawaypiecePage');
  }
  ionViewWillEnter() {
    this.wellenNo = "";
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      if (AppGlobal.scanFlag) {
        AppGlobal.scanFlag = false;
        this.wellenNo = barcode;
        this.getPickInfo();
      }

    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  itemInfo() {
    this.navCtrl.push('TakeawayinPage');
  }
  /**
* 入库单回车事件
*/
  keyEnter(event) {
    if (event && event.keyCode == 13) {
      this.getPickInfo();
    }
  }
  getPickInfo() {
    if (Utils.isEmpty(this.wellenNo)) {
      this.nativeService.showToast('请输入波次编号');
      AppGlobal.scanFlag = true;
      return;
    }
    let params = {
      wellenNo: this.wellenNo
    }
    //任务查询
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.getPickInfo, Method.Get, params, ContenType.Form, result => {
      this.nativeService.hideLoading();
      AppGlobal.scanFlag = true;
      this.PickInfoResult = result.data;
      this.pickPlansList = this.PickInfoResult.pickPlans;
      if (Utils.isEmpty(this.pickPlansList)) {
        this.nativeService.showToast('当前任务下没有物品');
        return;
      }
      //默认取第一条
      this.pickPlansModel = this.pickPlansList[0];
      if (Utils.isNotEmpty(this.pickPlansModel)) {
        this.navCtrl.push('TakeawayinPage', { pickPlansModel: this.pickPlansModel, PickInfoResult: this.PickInfoResult, pickPlansList: this.pickPlansList, wellenNo: this.wellenNo });
      }
    });
  }
}
