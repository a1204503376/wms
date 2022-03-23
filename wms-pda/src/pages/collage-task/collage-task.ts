import { Component , NgZone} from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events,AlertController } from 'ionic-angular';
import { Utils } from '../../services/Utils';
import { SkuLotModel } from '../../models/SkuLotModel'
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../utils/appConstant';
import { NativeService } from "../../services/NativeService"
import { AppService } from "../../services/App.service";
import { BarcodeService } from '../../services/BarCodeService';
import { ScanModel } from '../../models/DataBase.Model';
import { AppGlobal } from '../../services/AppGlobal';
/**
 * Generated class for the CollageTaskPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-collage-task',
  templateUrl: 'collage-task.html',
})
export class CollageTaskPage {

  scanView: string;//二维码内容

  scanModel: ScanModel;//扫描箱码实体

  scanModelList: ScanModel[] = [];//扫描箱码实体集合

  resultSkuItem: any;//当前扫描物品

  resultList: any;//打包任务详情

  isShow: boolean = true;

  lpnCode: string;//卡板号

  length: string;//卡板长

  width: string;//卡板宽

  height: string;//卡板高

  weight: string;//重量

  remaker: string;//备注
  isUpdate:boolean;
  taskId: string;//任务ID
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public nativeService: NativeService,
    public appService: AppService,
    public barcodeService: BarcodeService,
    public modalCtrl: ModalController,
    private ngZone: NgZone,
    public events: Events,
    public alertControl: AlertController
  ) {
    this.resultList = this.navParams.get('result');
    this.taskId = this.navParams.get('taskId');
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad CollageTaskPage');
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    if (AppGlobal.scanFlag) {
      AppGlobal.scanFlag = false;
      this.events.subscribe('barcode:scan', (barcode, time) => {
        if(!this.isShow){
          this.ngZone.run(() => {
            this.lpnCode = barcode;
          });
        }else{
          this.getStockInfoPutawayForBox(barcode);
        }
      });
    }
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
   * 二维码数据解析
   * 
   */
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isEmpty(this.scanView)) {
        this.nativeService.showToast('扫描数据不能为空');
        return;
      }
      this.getStockInfoPutawayForBox(this.scanView);
    }
  }
  /**
  * 箱码解析
  */
  getStockInfoPutawayForBox(scanView) {
    this.barcodeService.ScanSkuBarcode(scanView).subscribe(val => {
      if (Utils.isNotEmpty(val)) {
        this.scanModel = val;
        //查询物品
        this.scanWmsSku(val);
      } else {
        AppGlobal.scanFlag = true;
      }
    })
  }
  /*物品扫描*/
  scanWmsSku(val: ScanModel) {
    let params = {
      skuCode: val.skucode
    }
    //获取物品
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.SkuList, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      AppGlobal.scanFlag = true;
      this.resultSkuItem = null;
      //判断是否多个物品
      if (result.data.length > 1) {
        //选择物品
        this.openAsnRecordModal(result.data, val);
      } else if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('没有查询到物品');
      } else {
        this.resultSkuItem = result.data[0];
        val['skuId'] = this.resultSkuItem.skuId;
        val['skuName'] = val.skuname;
        if (this.CheckFinish()) {
          this.scanModelList.push(val);
        }
      }
    });
  }

  CheckFinish() {
    if (this.scanModelList.length + this.resultList.finish + 1 > this.resultList.total) {
      return false;
    } else {
      return true;
    }
  }
  /**
   * 删除拼托明细
   */
  removeItem(item) {
    this.scanModelList = this.scanModelList.filter(x => x != item);
  }
  /**
  *查看物品明细
  */
  openAsnRecordModal(skuList, val: ScanModel) {
    let myModal = this.modalCtrl.create('SkuListModal', {
      skuList: skuList,
    });
    myModal.onDidDismiss(data => {
      if (Utils.isNotEmpty(data)) {
        this.resultSkuItem = data;
        val['skuId'] = this.resultSkuItem.skuId;
        val['skuName'] = val.skuname;
        if (this.CheckFinish()) {
          this.scanModelList.push(val);
        }
      }
    });
    myModal.present();
  }
  /**
   * 提交出库拼托信息
   */
  submitOutStockLpnInfo() {
    if (Utils.isEmpty(this.lpnCode)) {
      this.nativeService.showToast('请填写卡板号');
      return;
    }
    if (Utils.isEmpty(this.length)) {
      this.nativeService.showToast('请填写卡板长度');
      return;
    }
    if (Utils.isEmpty(this.width)) {
      this.nativeService.showToast('请填写卡板宽度');
      return;
    }
    if (Utils.isEmpty(this.height)) {
      this.nativeService.showToast('请填写卡板高度');
      return;
    }
    if (Utils.isEmpty(this.weight)) {
      this.nativeService.showToast('请填写卡板重量');
      return;
    }
    for (let index = 0; index < this.scanModelList.length; index++) {
      this.scanModelList[index]['pickQty'] = this.scanModelList[index].qty;
    }
    let params = {
      taskId: this.taskId,
      soBillId: this.resultList.soBillId,
      whId: this.resultList.whId,
      lpnCode: this.lpnCode,
      length: this.length,
      width: this.width,
      height: this.height,
      weight: this.weight,
      detailList: this.scanModelList,
      remaker:this.remaker,
      isUpdate:this.isUpdate
    }

    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.submitOutStockLpnInfo, Method.Post, params, ContenType.Json, result => {
      if(result.success){
        this.getOutstockLpnInfo();
      }else{
        this.alertControl.create({
          title: '提示',
          subTitle: result.message,
          buttons: [{ text: '取消' },
          {
            text: '确定',
            handler: () => {
              this.isUpdate = true;
              this.submitOutStockLpnInfo();
            }
          }
          ]
        }).present();
      }
    });
  }
  /**
   * 查询拼托接口
   */
  getOutstockLpnInfo() {
    this.appService.httpRequest(Api.Outstock + Api.getOutstockLpnInfo, Method.Get, { taskId: this.taskId }, ContenType.Content, result => {
      this.nativeService.hideLoading();
      this.nativeService.showToast('提交成功');
      this.clearModel();
      if(result.complated){
        this.navCtrl.pop();
      }
      // if (Utils.isNotEmpty(result.data)) {
      //   this.resultList = result.data;
      //   if (this.resultList.finish == this.resultList.total) {
      //     this.taskPDAClose();
      //   }
      // }

    });
  }
  /**
 * 关闭拼托任务
 */
  taskPDAClose() {
    this.appService.httpRequest(Api.taskPDA + Api.taskPDAClose, Method.Get, { ids: this.taskId }, ContenType.Content, result => {
      this.nativeService.hideLoading();
      this.navCtrl.pop();
    });
  }
  /**
   * 清空页面部分数据
   */
  clearModel() {
    this.scanModel = null;
    this.scanModelList = [];
    this.length = '';
    this.lpnCode = '';
    this.width = '';
    this.height = '';
    this.weight = '';
    this.isShow = true;
  }
  /**
   * 头部标签点击
   */
  handItemClick() {
    if (this.isShow) {
      if (this.resultList.lpnList.length > 0) {
        this.navCtrl.push('CollageTaskListPage', { result: this.resultList });
      }
    } else {
      if (this.scanModelList.length > 0) {
        let myModal = this.modalCtrl.create('CollageTaskBoxPage', {
          scanModelList: this.scanModelList,
          result: this.resultList
        });
        myModal.onDidDismiss(data => {
          if (data != undefined) {
            this.scanModelList = data;
          }
        });
        myModal.present();
      }
    }
  }
  /**
   * 提交按钮
   */
  btnOk() {
    if (this.isShow) {
      if (this.scanModelList.length <= 0) {
        this.nativeService.showToast('没有扫描箱码,请扫描');
        return;
      }
      this.isShow = false;
      // this.getLpnCode();
    } else {
      //提交托盘
      this.submitOutStockLpnInfo();
    }
  }
  getLpnCode(){
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.getLpnCode, Method.Get, { billNo: this.resultList.soBillNo }, ContenType.Content, result => {
      this.nativeService.hideLoading();
      if(result.success){
        this.lpnCode = result.data;
      }
    });
  }
  /**
   * 返回按钮 
   */
  exit() {
    if (this.isShow) {
      this.navCtrl.pop();
    } else {
      this.isShow = true;
    }
  }
}
