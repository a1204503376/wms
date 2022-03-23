import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Utils } from '../../services/Utils';
import { ScanModel } from "../../models/DataBase.Model";
import { AppService } from "../../services/App.service";
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../utils/appConstant';
import { NativeService } from "../../services/NativeService"
import { BarcodeService, BarcodeType } from '../../services/BarCodeService';
import { s } from '@angular/core/src/render3';
import { SelectItem } from 'primeng/api';
import { AppGlobal } from '../../services/AppGlobal';
/**
 * Generated class for the ReplenishmenhomePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-replenishmenhome',
  templateUrl: 'replenishmenhome.html',
})
export class ReplenishmenhomePage {

  IsShow: boolean = true;

  taskId: string;//任务ID

  result: any = {};//补货数据集

  selectModel: any = {}; //选中任务

  scanView: string;//扫描信息

  locList: SelectItem[] = [];

  public locCode: string;//当前库位


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
    if (Utils.isNotEmpty(this.navParams.get('taskId'))) {
      this.taskId = this.navParams.get('taskId');
    };
    if (Utils.isNotEmpty(this.navParams.get('result'))) {
      this.result = this.navParams.get('result');
      this.selectModel = this.result.detailList[0];
    };

  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.BoxCode) {
        this.ngZone.run(() => {
          this.scanView = barcode;
          this.scanViewIsModel();
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  ionViewDidLoad() {
    this.fillScanModel();
  }
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.scanView)) {
        this.scanViewIsModel();
      } else {
        this.nativeService.showToast('请扫描箱码');
      }
    }
  }
  /**
   * 箱码解析
   */
  scanViewIsModel() {
    this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(val => {
      this.scanView = '';
      if (Utils.isNotEmpty(val)) {
        this.getSkuListByCode(val);
      }
    })
  }
  /**
   * 根据物品编码获取物品明细
   */
  getSkuListByCode(model: ScanModel) {
    this.appService.httpRequest(Api.stockInner + Api.getSkuListByCode, Method.Get,
      { skuCode: model.skucode, taskId: this.taskId },
      ContenType.Form, result => {
        if (result.data.length > 1) {
          this.openAsnRecordModal(result.data);
        } else {
          let model = this.result.detailList.find(x => x.skuId == result.data[0].skuId);
          if (Utils.isNotEmpty(model)) {
            console.log(model);
            this.selectModel = model;
            //界面初始化
            this.fillScanModel();
          } else {
            this.nativeService.showToast('当前条码没有在补货列表查询到数据');
          }
        }
      })
  }

  /**
  *查看物品明细
  */
  openAsnRecordModal(skuList) {
    let myModal = this.modalController.create('SkuListModal', {
      skuList: skuList,
    });
    myModal.onDidDismiss(data => {
      if (Utils.isNotEmpty(data)) {
        let model = this.result.detailList.find(x => x.skuId == data.skuId);
        if (Utils.isNotEmpty(model)) {
          console.log(model);
          this.selectModel = model;
          //界面初始化
          this.fillScanModel();
        } else {
          this.nativeService.showToast('当前条码没有在补货列表查询到数据');
        }
      }
    });
    myModal.present();
  }
  /**
   * 初始化构建扫描组件
   */
  fillScanModel() {
    this.IsShow = true;
    this.labelTypecom.ComponentFlag = true;
    this.labelTypecom.locCodeFlag = false;
    this.labelTypecom.qtyDisabled = false;
    let scanModel = new ScanModel();
    scanModel.um = this.selectModel.wsuName;
    scanModel.skucode = this.selectModel.skuCode;
    scanModel.skuname = this.selectModel.skuName;
    scanModel.qty = '0';
    //批属性加载
    this.selectModel.skuLotValList.forEach((element, index) => {
      scanModel.skuLotModel[`skulot${index++}`] = element.skuLotValue;
      scanModel.skuLotModel[`skuLotName${index++}`] = element.skuLotLabel;
    });
    this.labelTypecom.changeRun(scanModel);
    //加载库位
    console.log(this.locList);
    this.selectModel.locationList.forEach(element => {
      this.locList.push({
        label: element, value: element
      });
    });
  }
  /**
   * 查看明细列表
   * @param flag   true 已完成    false 未完成
   */
  detailed(flag, result) {
    let myModal = this.modalController.create('ReplenishmenDetilePage', {
      result: result,
      replenIsFalg: flag //当前补货明细
    });
    //回调
    myModal.onDidDismiss(data => {
      if (Utils.isNotEmpty(data) && data != undefined) {
        this.selectModel = data;
        this.fillScanModel();
      }
    });
    myModal.present();
  }
  /**
   * 提交补货信息
   */
  submitReplenishmen() {
    if (Utils.isEmpty(this.selectModel)) {
      this.nativeService.showToast('当前没有选中物品');
      return;
    }
    if (Utils.isEmpty(this.locCode)) {
      this.nativeService.showToast('请选择或输入目标库位');
      return;
    }
    const params = {
      transferDetailId: this.selectModel.transferDetailId,
      skuId: this.selectModel.skuId,
      wspId: this.selectModel.wspId,
      transQty: String(this.labelTypecom.scanModel.qty),
      targetLocCode: this.locCode
    }
    //记录之前物品ID
    let nextSkuId = this.selectModel.skuId;
    this.appService.httpRequest(Api.stockInner + Api.submitReplenishmen, Method.Post, params, ContenType.Json, result => {
      this.getReplenishmenInfo(nextSkuId);
    })
  }
  /**
 * 补货详情
 */
  getReplenishmenInfo(nextSkuId) {
    this.appService.httpRequest(Api.stockInner + Api.getReplenishmenInfo, Method.Get, {
      taskId: this.taskId
    },
      ContenType.Content, result => {
        if (Utils.isNotEmpty(result.data)) {
          //补货任务已完成  
          if (result.data.finish == result.data.total || Utils.isEmpty(result.data.detailList)) {
            this.navCtrl.pop();
          } else {
            this.result = result.data;
            let nextMode = this.result.detailList.find(x => x.skuId == nextSkuId);
            if (Utils.isNotEmpty(nextMode)) {
              this.selectModel = nextMode;
            } else {
              this.selectModel = this.result.detailList[0];
            }
            this.fillScanModel();
          }
        } else {
          this.navCtrl.pop();
        }
      });
  }
  /**
  * 未完成的明细
  */
  noFinishDetailed() {
    this.detailed(false, this.result);
  }
  /**
   * 已完成补货数量
   */
  finishDetailed() {
    this.appService.httpRequest(Api.stockInner + Api.getUnfinishReplenishmenList, Method.Get, {
      taskId: this.taskId
    },
      ContenType.Content, result => {
        if (Utils.isNotEmpty(result.data)) {
          let resultModel = {
            finish: this.result.finish,
            total: this.result.total,
            detailList: result.data
          }
          this.detailed(true, resultModel);
        } else {
          this.nativeService.showToast('没有已完成的数据');
        }
      });
  }
  /**
   * 返回
   */
  exit() {
    if (this.IsShow) {
      this.navCtrl.pop();
    } else {
      this.IsShow = true;
    }
  }
  /**
   * 提交
   */
  btnOk() {

    if (this.IsShow) {
      this.IsShow = false;
    } else {
      //补货提交
      this.submitReplenishmen();
    }
  }
}
