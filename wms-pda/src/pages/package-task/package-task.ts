import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Utils } from '../../services/Utils';
import { ScanModel } from '../../models/DataBase.Model';
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../utils/appConstant';
import { NativeService } from "../../services/NativeService"
import { AppService } from "../../services/App.service";
import { BarcodeService, BarcodeType } from '../../services/BarCodeService';
import { AppGlobal } from '../../services/AppGlobal';

/**
 * Generated class for the PackageTaskPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-package-task',
  templateUrl: 'package-task.html',
})
export class PackageTaskPage {

  public IsShow: boolean = true; //显隐标识   1.true拣货列表  2.false扫码信息列表

  public result: any;//拣货列表集合

  public locCode: string;//库位编码

  public scanView: string;//扫描实体

  public scanModel: ScanModel;//扫描实体

  public resultSkuItem: any;//物品详情

  public btnValue: string = '下一步';//btn值

  public taskId: string;//任务id

  public qtyTemp: string;//记录出事数量

  @ViewChild('labelTypecom') labelTypecom;

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    public modalCtrl: ModalController,
    public appService: AppService,
    public events: Events,
    public ngZone: NgZone
  ) {
    if (Utils.isNotEmpty(this.navParams.get('result'))) {
      this.result = this.navParams.get('result');
    };
    if (Utils.isNotEmpty(this.navParams.get('taskId'))) {
      this.taskId = this.navParams.get('taskId');
    };

  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.locCode = barcode;
        });
      }
      if (bt == BarcodeType.BoxCode) {
        //扫描箱码
        this.scanView = barcode;
        this.getStockInfoPutawayForBox();
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
   * 箱码扫描事件
   * @param event 
   */
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.scanView) && Utils.isNotEmpty(this.locCode)) {
        this.getStockInfoPutawayForBox();
      } else {
        this.nativeService.showToast('请扫描箱码或库位');
      }
    }
  }
  /**
  * 按箱查询
  */
  getStockInfoPutawayForBox() {
    this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(val => {
      if (Utils.isNotEmpty(val)) {
        this.qtyTemp = val.qty;
        this.scanModel = val;
        //查询物品
        this.scanWmsSku(val);
      }
    })
  }
  /*物品扫描*/
  scanWmsSku(val: ScanModel) {
    if (Utils.isEmpty(val.skucode)) {
      this.nativeService.showToast('物品编码不能为空');
    }
    let params = {
      skuCode: val.skucode
    }
    //获取物品
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.SkuList, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      //判断是否多个物品
      if (result.data.length > 1) {
        //选择物品
        this.openAsnRecordModal(result.data, val);
      } else if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('没有查询到物品');
        result;
      } else {
        this.resultSkuItem = result.data[0];
        this.islocCode_KeyDown(val);

      }
    });
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
        this.islocCode_KeyDown(val);
      }
    });
    myModal.present();
  }

  /**
  * 展示标签信息
  */
  islocCode_KeyDown(model: ScanModel) {
    this.IsShow = false;
    this.btnValue = '保存';
    //绑定条码批属性标签
    if (Utils.isNotEmpty(this.resultSkuItem)) {
      this.barcodeService.convertGetLotModel(this.resultSkuItem.skuLot, model.skuLotModel)
      this.labelTypecom.changeRun(model);
      this.labelTypecom.locCode = this.locCode;
      this.labelTypecom.locCodeFlag = true;
      this.labelTypecom.qtyDisabled = false;
      this.labelTypecom.locDisabled = true;
    } else {
      this.nativeService.showToast('没有查询到物品');
    }
  }
  /**
   * 返回
   */
  exit() {
    if (this.IsShow) {
      this.navCtrl.pop();
    }
    else {
      this.IsShow = true;
      this.btnValue = '下一步';
    }

  }
  /**
   * 提交按钮
   */
  btnOk() {
    if (this.IsShow) {
      //下一步
      this.navCtrl.push('PackageTaskListPage', { taskId: this.taskId });
    } else {
      //拣货提交
      this.submitPickInfo();
    }
  }
  /**
   * 拣货提交
   */
  submitPickInfo() {
    let skuLotModel = this.barcodeService.convertLotModel(this.scanModel);
    let params = {
      locCode: this.locCode,
      skuCode: this.resultSkuItem.skuCode,
      skuId: this.resultSkuItem.skuId,
      packState: '0',
      stockQty: this.qtyTemp,
      pickQty: this.labelTypecom.scanModel.qty,
      taskId: this.taskId,
      skuLots: skuLotModel
    };
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.stockInner + Api.stockInnerSubmitPickInfo, Method.Post, params,
      ContenType.Json, result => {

        //拣货列表初始化
        this.upPickList({ taskId: this.taskId });
        //界面初始化
        this.clearModel();
      });
  }
  /**
   * 打包任务
   */
  upPickList(params) {
    this.appService.httpRequest(Api.stockInner + Api.upPickList, Method.Get, { taskId: params.taskId },
      ContenType.Content, result => {
        if (Utils.isEmpty(result.data)) {
          this.navCtrl.push('PackageTaskListPage', { taskId: params.taskId });
        } else {
          this.result = result.data;
        }
      });
  }
  /**
   * 界面清除
   */
  clearModel() {
    this.result = null;
    this.locCode = '';
    this.scanView = '';
    this.scanModel = null;
    this.resultSkuItem = null;
    this.btnValue = '下一步';
    this.qtyTemp = '';
    this.IsShow = true;
  }
}
