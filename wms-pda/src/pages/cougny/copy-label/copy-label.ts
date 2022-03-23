import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, Events,AlertController } from 'ionic-angular';
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService"
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
import { SkuLotModel } from '../../../models/SkuLotModel';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { ScanModel } from '../../../models/DataBase.Model';
import { AppGlobal } from '../../../services/AppGlobal';

/**
 * Generated class for the CheckinBoxInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-copy-label',
  templateUrl: 'copy-label.html',
})

export class CopyLabelPage {

  public result: any;//单据信息
  public boxCode: string;//箱号
  public scanModelFlag: boolean = true;//二维码状态
  public sname: string;//供应商名称
  public planCountQty: string;//计划总数量
  public scanCountQty: string;//实际总数量
  public finish: string; //明细单完成数
  public count: string//明细单总数
  public um:any='PCS';
  public ruleCode: number;//规则编号

  public labelType: any;//标签类型

  EidtFlag: boolean = true;//箱码是否可编辑
  isEidtFlag: boolean = true;//箱码是否显示


  public planQtyName: string;//单据明细计划名称
  public scanQtyName: string;//单据明细已完成名称

  public planQty: string;//单据明细计划
  public scanQty: string;//单据明细已完成
  public skuCode: string;//物品编号
  public skuName: string;//物品名称
  public skuSpec: string;//物品规格
  public detailStatus: string;//接受状态 10:未接收,20:已接收
  public sku: any;//物品信息 
  public asnInfo: any;

  public skuLotModel: SkuLotModel = new SkuLotModel();

  public scanModel: ScanModel = new ScanModel();//箱码解析实体

  public scanView: string;//箱码页面实体

  public asnDetails: any;//存储单据ID  和明细ID
  public copyNum:number = 1;
  public skuPackageDetails: string[] = [];
  @ViewChild('labelTypecom') labelTypecom;

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    private events: Events,
    public ngZone: NgZone,
    public alertControl: AlertController
  ) {
   
  }
  ionViewWillEnter() {
    this.labelTypecom.ComponentFlag = true;
    this.labelTypecom.qtyDisabled =false;
    this.labelTypecom.locCodeFlag = false;
    this.labelTypecom.EidtFlag =false; 
    this.labelTypecom.isCopyLabel = true; 
    this.scanModel.um=this.um;
    this.labelTypecom.changeRun(this.scanModel);
    //事件注册（扫描结果接收）
    if (AppGlobal.scanFlag) {
      AppGlobal.scanFlag = false;
      this.events.subscribe('barcode:scan', (barcode, time) => {
        this.analysisDate(barcode);
      });
    }
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
 * 数据解析
 */
  analysisDate(barcode) {
    let bt = this.barcodeService.GetBarcodeType(barcode);
    if (bt == BarcodeType.BoxCode) {
      this.ngZone.run(() => {
        this.scanWmsSku(barcode);
      });
    }
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad CheckinBoxInfoPage');
  }
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.scanView)) {
        this.scanWmsSku(this.scanView);
      } else {
        this.nativeService.showToast('请扫描箱码');
      }
    }
  }
  /**
   * 检测物品信息
   * @param scanView 
   */
  scanWmsSku(scanView) {
    //箱码解析
    this.barcodeService.ScanSkuBarcode(scanView).subscribe(val => {
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
        AppGlobal.scanFlag = true;
        //判断是否多个物品
        if (result.data.length >= 1) {
          //基础计量单位
          let levelUm = result.data[0].skuPackage.skuPackageDetailVOList.find(x => x.skuLevel == 1);
          if (Utils.isEmpty(levelUm)) {
            this.nativeService.showToast('没有检测到当前物品的基础计量单位');
            return;
          }
          this.getLabelInfoWithStock(val,levelUm);
        } else {
          this.nativeService.showToast('没有检测到物品信息');
        }
      });
    })
  }
  getLabelInfoWithStock(model, levelUm) {
    if (Utils.isNotEmpty(model)) {
      this.scanModel = model;
      let param = {
        skuCode: model.skucode
      }
      this.appService.httpRequest(Api.Instock + Api.getLabelInfoWithStock, Method.Post, param, ContenType.Json, result => {
        this.nativeService.hideLoading();
        if (!result.success) {
          this.nativeService.showToast(result.msg);
        } else {
          result.data.skuConfig.forEach(element => {
            model.skuLotModel[`skuLotName${element.skuLotIndex}`] = element.skuLotLabel;
          })
          model.um = levelUm.wsuName;
          this.labelTypecom.locCodeFlag=false; 
          this.labelTypecom.changeRun(model);
        }
      });
    }

  }
  runParent(asnInfo: any) {
    this.scanModelFlag = false;

  }
  

  /**
   * 收货提交
   */
  btnOk() {
    if(Utils.isEmpty( this.scanModel.skucode)){
      this.nativeService.showToast("请输入物料编号");
      return;
    }
    if(Utils.isEmpty( this.scanModel.skuname)){
      this.nativeService.showToast("请输入物料名称");
      return;
    }
    if(Utils.isEmpty( this.scanModel.qty)){
      this.nativeService.showToast("请输入物料数量");
      return;
    }
    if(Number.isNaN(this.copyNum)
    ||Utils.isEmpty(this.copyNum)
    ||this.copyNum<=0){
      this.copyNum = 1;
      this.submit();
    }else if(this.copyNum>1){
      this.alertControl.create({
        title: '温馨提示',
        subTitle: '由于需要打印多张标签，请再次确认打印信息是否正确！',
        buttons: [{ text: '取消' },
        {
          text: '确定',
          handler: () => {
            this.submit();
          }
        }
        ]
      }).present();
    }else{
      this.submit();
    }
    
    
  }
  submit(){
    let skuLotModel = this.barcodeService.convertLotModel(this.scanModel);
    let params ={
      skuCode: this.scanModel.skucode,
      skuName: this.scanModel.skuname,
      wsuName: this.scanModel.um,
      qty:this.scanModel.qty,
      skuLots: skuLotModel,
      printCount:this.copyNum
    };
    this.nativeService.showLoading();
    this.appService.httpRequest('/api/ApiPDA/print/copy/label', Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      this.nativeService.showToast('打印成功');
    })
  }
  /**
   * 数据初始化
   */
  clearModel() {
    this.labelTypecom.clearModel();
    this.scanModelFlag = true;
    this.scanView = '';
    this.scanModel = null;
  }
 
}
