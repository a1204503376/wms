import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { SkuLotModel } from '../../../models/SkuLotModel'
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
import { NativeService } from "../../../services/NativeService"
import { AppService } from "../../../services/App.service";
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { AppGlobal } from '../../../services/AppGlobal';
import { ScanModel } from '../../../models/DataBase.Model';
import { SelectItem } from 'primeng/api';
import { LpnItem } from './LpnItem';
/**
 * Generated class for the PutawayboxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-putawaytray',
  templateUrl: 'putawaytray.html',
})
export class PutawayTrayPage {
  /**
    * 页面元素
    */
  // public IsShow = 0;//显隐标识

  // public Iswarehouse: number = 0;//仓库显示

  // public IsSku: number = 1;//提交显示

  public warhouserItem: any;//选中库房

  skuLotModel: SkuLotModel = new SkuLotModel();//批属性对象

  skuCode: string;//物品编码

  skuName: string;//物品名称

  scanQty: number;//数量
  total:number = 0;
  wsuName: string;//计量单位名称

  instockDate: Date;//入库日期

  produceDate: Date;//日期(生产日期)

  typeCode: string;//物品类型编码

  lotNumber: string;//批次号

  version: string;//版本号

  remarks: string;//备注

  type: string;//类型

  labelCode: string;//标签类型名称

  labelType: any;//标签类型

  labelValue: any;//标签内容

  searchCode: string;//二维码内容
  public items: any = [];
  /**界面实体 */
  public lpnCode: string = '';//容器编码

  public zoneCode: string = '';//库区编码

  public zoneName: string = '';//库区名称

  public typeCount: string = '';//物品种类数量

  public locCode: string = 'STAGE';//当前库位

  public targetlocCode: string = '';//目标库位
  public recommendLoc: string = '';//推荐库位

  public locStatusName: string = '';//当前状态

  public whName: string = '';//仓库名称

  public whId: string = '';//仓库ID

  public stockId: string;//库存ID

  public WarehouserModel: any;//库房列表

  public scanModel: ScanModel;//扫描实体

  public resultSkuItem: any;//物品详情
  public scanModelRecords: any = [];
  public locList: SelectItem[];
  public planCount: string;
  public umName: string;
  selectedCar1: string;
  public lpnItems: Array<LpnItem> = [];
  public currentItem:LpnItem = new LpnItem();
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public nativeService: NativeService, public appService: AppService,
    public barcodeService: BarcodeService,
    public modalCtrl: ModalController,
    private events: Events,
    private ngZone: NgZone
  ) {
    this.locList = [
      { label: 'STAGE', value: 'STAGE' },
      { label: 'PICK', value: 'PICK' },
      { label: 'PACK', value: 'PACK' },
      { label: 'MOVE', value: 'MOVE' },
    ];
    //获取库房列表 
    this.nativeService.showLoading('正在获取库房...');
    this.appService.httpRequest(Api.api + Api.ApiPDA, Method.Post, '', ContenType.Form, result => {
      this.nativeService.hideLoading();
      if (Utils.isNotEmpty(result.data)) {
        this.WarehouserModel = result.data;
        this.warhouserItem = result.data[0];
      }
    });
  }
  removeItem(item) {
    this.lpnItems = this.lpnItems.filter(x => x.lpnCode != item.lpnCode);
    this.collectQty();
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.targetlocCode = barcode;
        });
      }
      if (bt == BarcodeType.BoxCode) {
        this.ngZone.run(() => {
          this.getStockInfoPutawayForBox(barcode);
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }

  lpnCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      if(this.lpnItems.find((n)=>n.lpnCode==this.lpnCode)){
        this.nativeService.showToast("该托号已扫描过！");
        return;
      }

      this.queryStockByLpnCode();
      // this.boxItems.push({boxCode:this.boxCode,skuCode:'1234',skuName:'4321',reZone:'Z01-01-01',reLocCode:'A01-01-01',isChecked:true})
    }
  }
  queryStockByLpnCode() {
    if (Utils.isEmpty(this.lpnCode)) {
        this.nativeService.showToast('请输入容器编号');
        return;
    }
    let body = "lpnCode=" + this.lpnCode;
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Instock + Api.queryStockByLpnCode, Method.Post, body, ContenType.Form, result => {
        this.nativeService.hideLoading();
        if (Utils.isNotEmpty(result.data)&&result.data.typeCount!='') {
            result.data['isChecked'] = true;
            result.data['reZone'] = result.data.zoneName;
            result.data['lpnCode'] = this.lpnCode;
            result.data['reLocCode'] = result.data.targetLocCode;
            result.data['locCode'] = result.data.locCode;
            result.data['qty'] = result.data.typeCount;
            this.whId = result.data.whId;
            this.lpnItems.push(result.data);
            this.collectQty();
        }else{
          this.nativeService.showToast("该托盘没有要上架的物品!");
        }
    });
}
collectQty(){
  if(this.lpnItems.length>0){
    this.total = this.lpnItems.map(item=>item.qty).reduce((a,b)=>a+b);
  }else{
    this.total = 0;
  }
}
  updateQty(item){
    this.lpnCode = item.lpnCode;
    this.currentItem = item;
    this.collectQty();
  }
  /**
   * 箱码回车事件
   * 
   */
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      this.getStockInfoPutawayForBox(this.searchCode);
    }
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

      //判断是否多个物品
      if (result.data.length > 1) {
        this.nativeService.hideLoading();
        //选择物品
        this.openAsnRecordModal(result.data, val);
      } else if (Utils.isEmpty(result.data)) {
        this.nativeService.hideLoading();
        this.nativeService.showToast('没有查询到物品');
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
   * 箱码解析
   */
  getStockInfoPutawayForBox(searchCode) {
    if (Utils.isEmpty(this.warhouserItem)) {
      this.nativeService.showToast('请选择仓库');
      return;
    }
    if (Utils.isEmpty(searchCode)) {
      this.nativeService.showToast('扫描数据不能为空');
      return;
    }
    this.whName = '';
    this.recommendLoc = '';
    this.planCount = '';
    this.umName="";
    this.scanModel = null;
    this.barcodeService.ScanSkuBarcode(searchCode).subscribe(val => {
      if (Utils.isNotEmpty(val)) {
        this.scanModel = val;
        if (Utils.isEmpty(this.scanModelRecords)) {
          this.scanModelRecords.push(val);
        } else {
          for (let item of this.scanModelRecords) {
            if (Utils.isObjectValueEqual1(val, item, ['skuname', 'qty', 'setLotModel', 'setLotValue'])) {
              if (Number.isNaN(item.qty) || Utils.isEmpty(item.qty)) {
                item.qty = Number.parseInt(val.qty);
              } else {
                item.qty = Number.parseInt(item.qty) + Number.parseInt(val.qty);
              }
              this.scanModel = item;
            } else {
              if (!Utils.contains(this.scanModelRecords, val, ['skuname', 'qty', 'setLotModel', 'setLotValue'])) {
                this.scanModelRecords.push(val);
                break;
              }
            }
          }
        }
        //查询物品
        this.scanWmsSku(val);
      }
    })
  }
  /**
    * 查询库存信息
  */
  islocCode_KeyDown(model: ScanModel) {
    if (Utils.isEmpty(model.qty)) {
      this.nativeService.showToast('物品数量不能为空');
      return;
    }
    if (Utils.isEmpty(model.skucode)) {
      this.nativeService.showToast('物品编码不能为空');
      return;
    }
    let skuLotModel = this.barcodeService.convertLotModel(model);
    this.skuCode = model.skucode;
    this.skuName = model.skuname;
    let param = {
      locCode: this.locCode,
      qty: model.qty,
      skuCode: model.skucode,
      whId: this.warhouserItem.whId,
      skuLots: skuLotModel,
      skuId: this.resultSkuItem.skuId
    }
    this.appService.httpRequest(Api.Instock + Api.getStockInfoPutawayForBox, Method.Post, param, ContenType.Json, result => {
      if (Utils.isNotEmpty(result.data)) {
        if(!result.data.success){
          this.nativeService.showToast(result.data.msg)
          return;
        }
        let Jsonresult = result.data;
        this.zoneName = Jsonresult.zoneName;
        this.zoneCode = Jsonresult.zoneCode;
        this.typeCount = Jsonresult.typeCount;
        this.locStatusName = Jsonresult.locStatusName;
        this.whName = Jsonresult.whName;
        this.planCount = Jsonresult.planCount;
        this.umName = Jsonresult.umName;
        //this.locCode = Jsonresult.locCode;
        this.whId = Jsonresult.whId;
        this.items = Jsonresult.stockLocList;
        if (Utils.isNotEmpty(Jsonresult.targetLocCode)) {
          this.ngZone.run(() => {
            this.recommendLoc = Jsonresult.targetLocCode;
            this.nativeService.hideLoading();
          });
        } else {
          this.nativeService.hideLoading();
        }
        this.stockId = Jsonresult.stockId;
      }
    });

  }
  /**
   * 上架提交
   */
  btnOk() {
    if(this.lpnItems.length == 0){
      this.nativeService.showToast('请选择扫描的托盘号！');
      return;
    }
    const temp = this.lpnItems.filter(x=>x.isChecked);
    if(temp.length == 0){
      this.nativeService.showToast('请选择扫描的托盘号！');
      return;
    }
    if (Utils.isEmpty(this.targetlocCode)) {
      this.nativeService.showToast('目标库位不能为空！');
      return;
    }
    // const lpnCodes = this.lpnItems.map(x=>x.lpnCode);
    let param = {
      targetLocCode: this.targetlocCode,
      lpnItems: this.lpnItems.filter(x=>x.isChecked),
      whId:this.whId
    };
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Instock + Api.submitPutawayNew, Method.Post, param, ContenType.Json, result => {
      this.nativeService.showToast('提交成功');
      this.nativeService.hideLoading();
      this.clearModel();
    });
  }
  /**
 * 返回
 */
  exit() {
    this.navCtrl.pop();
  }
  clearModel() {
    this.lpnCode = '';
    this.targetlocCode = '';
    this.currentItem = new LpnItem();
    this.lpnItems = this.lpnItems.filter(x => !x.isChecked);
  }
}
