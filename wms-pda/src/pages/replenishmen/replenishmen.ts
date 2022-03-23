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
import {Storage} from '@ionic/storage';
@IonicPage()
@Component({
  selector: 'page-replenishmen',
  templateUrl: 'replenishmen.html',
})
export class ReplenishmenPage {

  IsShow: boolean = true;

  taskId: string;//任务ID

  selectModel: any = {}; //选中任务

  scanView: string;//扫描信息

  zoneList: SelectItem[] = [];
  public zoneId:  string;
  public targetLocCode:  string;
  public targetLpnCode:  string;
  public result:any = {};
  public currentDetail:any = {};
  public isBack:boolean;
  public sysparms: any = [];//系统参数
  @ViewChild('labelTypecom') labelTypecom;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public modalController: ModalController,
    public appService: AppService,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    public events: Events,
    private storage: Storage,
    public ngZone: NgZone
  ) {
    if (Utils.isNotEmpty(this.navParams.get('taskId'))) {
      this.taskId = this.navParams.get('taskId');
    };
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
  });
   
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
  ionViewWillEnter() {
    this.labelTypecom.locCodeFlag=false;
    this.labelTypecom.skuCodeFlag=true;
    this.labelTypecom.skuNameFlag=true;
    if(!this.isBack){
      this.labelTypecom.ComponentFlag = false;
      this.getReplenishmenInfo();
    }
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.BoxCode) {
        this.ngZone.run(() => {
          this.scanView = barcode;
          this.scanViewIsModel();
        });
      }
      if (bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.targetLocCode = barcode;
        });
      }
      if (bt == BarcodeType.TuoPan) {
        this.ngZone.run(() => {
          this.targetLpnCode = barcode;
        });
      }
    });
  }
  callback=(data)=>
  {
    return new Promise((resolve,reject)=>{
      if(typeof data!="undefined")
      {
        this.isBack = true;
        this.currentDetail =data;
        this.IsShow=true;
        this.selectModel = null;
        this.labelTypecom.ComponentFlag = false;
        resolve('ok')
      }else
      {
        reject(Error('error'))
      }
    })
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
            if(model.skuId!=this.currentDetail.skuId){
              this.nativeService.showToast('请扫描提示的物料！');
            }else{
              this.selectModel = model;
              //界面初始化
              this.fillScanModel();
            }
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
    this.labelTypecom.qtyDisabled = false;
    this.labelTypecom.isCopyLabel = false;
    let scanModel = new ScanModel();
    scanModel.um = this.selectModel.umName;
    scanModel.skucode = this.selectModel.skuCode;
    scanModel.skuname = this.selectModel.skuName;
    scanModel.qty = '0';
    //批属性加载
    if( Utils.isNotEmpty(this.selectModel.skuLotValList)){
      this.selectModel.skuLotValList.forEach((element, index) => {
        scanModel.skuLotModel[`skulot${element.skuLotIndex}`] = element.skuLotValue;
        scanModel.skuLotModel[`skuLotName${element.skuLotIndex}`] = element.skuLotLabel;
      });
    }
    this.labelTypecom.changeRun(scanModel);
    this.selectModel.zoneList.forEach(element => {
      this.zoneList.length=0;
      this.zoneList.push({
        label: element.zoneName, value: element.zoneId
      });
    });
  }
  ionViewWillLeave() {
  }
  ionViewDidLoad() {
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

  getReplenishmenInfo() {
    this.appService.httpRequest(Api.stockInner + Api.getReplenishmenInfo, Method.Get, {
      taskId: this.taskId
    },
      ContenType.Content, result => {
        this.nativeService.hideLoading();
        if (Utils.isNotEmpty(result.data)) {
          this.result = result.data;
          if (Utils.isNotEmpty(result.data.detailList)) {
             this.currentDetail = this.result.detailList[0];
          }else{
             this.navCtrl.pop();
          }
        }
      });
  }
  noFinishDetailed() {
    this.navCtrl.push('ReplenishmenRecordPage',{result:this.result,flag:false,currentItem:this.currentDetail,callback:this.callback});
  }
  finishDetailed() {
    this.navCtrl.push('ReplenishmenRecordPage',{flag:true,taskId:this.taskId});
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
    /**
   * 提交补货信息
   */
  submitReplenishmen() {
    if (Utils.isEmpty(this.selectModel)) {
      this.nativeService.showToast('当前没有选中物品');
      return;
    }
    if (Utils.isEmpty(this.zoneId)) {
      this.nativeService.showToast('请选择目标库区');
      return;
    }
    if (Utils.isEmpty(this.targetLocCode)) {
      this.nativeService.showToast('请选择或输入目标库位');
      return;
    }
    if (Utils.isEmpty(this.targetLpnCode)&&this.sysparms.paramValue==0) {
      this.nativeService.showToast('请选择或输入目标容器');
      return;
    }
    const params = {
      relDetailId: this.selectModel.relDetailId,
      skuId: this.selectModel.skuId,
      wspId: this.selectModel.wspId,
      umName:this.selectModel.umName,
      realQty: String(this.labelTypecom.scanModel.qty),
      targetZoneId: this.zoneId,
      targetLocCode: this.targetLocCode,
      targetLpnCode: this.targetLpnCode
    }
    this.appService.httpRequest(Api.stockInner + Api.submitReplenishmen, Method.Post, params, ContenType.Json, result => {
      if(result.success){
        this.IsShow=true;
        this.selectModel = null;
        this.labelTypecom.ComponentFlag = false;
        this.getReplenishmenInfo();
      }
    })
  }
}
