import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { ScanModel } from '../../../models/DataBase.Model';
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
import { NativeService } from "../../../services/NativeService"
import { AppService } from "../../../services/App.service";
import { map } from 'rxjs/operator/map';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { AppGlobal } from '../../../services/AppGlobal';
/**
 * Generated class for the PackageTaskListPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-package-task-list',
  templateUrl: 'package-task-list.html',
})
export class PackageTaskListPage {
  public IsShow: boolean = true; //显隐标识   1.true拣货列表  2.false扫码信息列表

  public result: any;//拣货列表集合

  public locCode: string;//库位编码

  public scanView: string;//扫描实体

  public scanModel: ScanModel;//扫描实体

  public resultSkuItem: any;//物品详情

  public btnValue: string = '提交';//btn值

  public taskId: string;//任务id

  public qtyTemp: string;//记录出事数量

  public isPackList: any[] = [];//打包物品列表

  @ViewChild('labelTypecom') labelTypecom;

  public taskItem: any;//当前选中的打包

  public total: number = 0;//合计
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    public modalCtrl: ModalController,
    public appService: AppService,
    public events: Events,
    public ngZone: NgZone
  ) {
    if (Utils.isNotEmpty(this.navParams.get('taskId'))) {
      this.taskId = this.navParams.get('taskId');
    };
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
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
      if (Utils.isNotEmpty(this.scanView)) {
        this.getStockInfoPutawayForBox();
      } else {
        this.nativeService.showToast('请扫描箱码');
      }
    }
  }
  /**
 * 按箱查询
 */
  getStockInfoPutawayForBox() {
    this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(val => {
      if (Utils.isNotEmpty(val)) {
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
   * 扫描提交待打包物品
   */
  scanUpPack() {
    if (Utils.isEmpty(this.resultSkuItem)) {
      this.nativeService.showToast('没有检测到物品');
    }
    if (Utils.isEmpty(this.resultSkuItem)) {
      this.nativeService.showToast('没有检测到数量');
    }
    let params = {
      skuCode: this.resultSkuItem.skuCode,
      skuId: this.resultSkuItem.skuId,
      stockQty: this.scanModel.qty,
      taskId: this.taskId,
      lspIds: ''
    }
    this.appService.httpRequest(Api.api + Api.SkuList, Method.Post, params, ContenType.Json, result => {
      //注入批属性
      result.data['skuLotModel'] = this.scanModel.skuLotModel;
      this.isPackList.push(result.data);
    });
  }

  /**
  * 展示标签信息
*/
  islocCode_KeyDown(model: ScanModel) {
    //this.IsShow = false;
    //this.btnValue = '保存';
    //绑定条码批属性标签
    if (Utils.isNotEmpty(this.resultSkuItem)) {
      this.barcodeService.convertGetLotModel(this.resultSkuItem.skuLot, model.skuLotModel)
      this.upPackList();
    } else {
      this.nativeService.showToast('没有查询到物品');
    }
  }
  /**
   * 扫描待打包物品
   */
  upPackList() {
    let lspIds = [];
    this.isPackList.forEach(element => {
      lspIds.push(element.lspId);
    });
    let params = {
      skuCode: this.resultSkuItem.skuCode,
      skuId: this.resultSkuItem.skuId,
      stockQty: this.scanModel.qty,
      taskId: this.taskId,
      skuLots: this.barcodeService.convertLotModel(this.scanModel),
      lspIds: lspIds.length > 0 ? lspIds.join() : ''
    }
    this.appService.httpRequest(Api.stockInner + Api.upPack, Method.Post, params, ContenType.Json, result => {
      result.data['skuLotModel'] = this.scanModel.skuLotModel;
      this.isPackList.push(result.data);
      //更新长度
      this.sumTotal();
    });
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
      this.btnValue = '提交';
    }

  }
  /**
   * 提交按钮
   */
  btnOk() {
    if (this.IsShow) {
      this.submitPackInfo();
    } else {
      for (let index = 0; index < this.isPackList.length; index++) {
        if (this.isPackList[index].systemProcId == this.taskItem.systemProcId) {
          if (this.labelTypecom.scanModel.qty > this.isPackList[index].qty) {
            this.nativeService.showToast('修改的数量不能大于尾箱的数量');
            return;
          }
          this.isPackList[index].qty = this.labelTypecom.scanModel.qty;
        }
      }
      this.IsShow = true;
      this.sumTotal();
    }
  }
  /**
   * 提交打包信息
   */
  submitPackInfo() {
    //let skuLotModel = this.barcodeService.convertLotModel(this.scanModel);
    let params = [];
    this.isPackList.forEach(element => {
      params.push({
        lspId: element.lspId,
        packQty: element.qty
      });
    });
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.stockInner + Api.submitPackInfo, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      this.nativeService.showToast('提交成功');
      if (result.data.length > 0) {
        this.clearModel();
      } else {
        this.navCtrl.popTo(this.navCtrl.getByIndex(this.navCtrl.length() - 3));
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
    this.btnValue = '提交';
    this.qtyTemp = '';
    this.total = 0;
    this.isPackList = [];
  }
  sumTotal() {
    this.total = 0;
    this.isPackList.forEach(element => {
      this.total += Number(element.qty);
    });
  }
  onClick(taskItem) {
    this.taskItem = taskItem;
    this.IsShow = false;
    this.labelTypecom.ComponentFlag = true;
    this.labelTypecom.qtyDisabled = false;
    this.labelTypecom.scanModel.skuname = this.taskItem.skuName;
    this.labelTypecom.scanModel.skucode = this.taskItem.skuCode;
    this.labelTypecom.scanModel.qty = this.taskItem.qty;
    this.labelTypecom.scanModel.skuLotModel = this.taskItem.skuLotModel;
  }
  /**
   * 打包明细查看
   */
  detailed() {
    this.navCtrl.push('PackageTaskInfoPage', { taskId: this.taskId });
  }
  /**
   * 删除打包列表
   */
  removeItem(item) {
    this.isPackList = this.isPackList.filter(x => x != item);
  }
}
