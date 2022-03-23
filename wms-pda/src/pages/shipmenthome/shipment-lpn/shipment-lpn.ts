import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { AppService } from "../../../services/App.service";
import { Api, ContenType, Method } from '../../../utils/appConstant';
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService";
import { AppGlobal } from '../../../services/AppGlobal';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
/**
 * Generated class for the ShipmentLpnPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-shipment-lpn',
  templateUrl: 'shipment-lpn.html',
})
export class ShipmentLpnPage {
  public truckView: number = 0;//装车牌界面
  public ShipmentLpnView: number = 1;//托盘发运界面
  public ShipmentBllView: number = 2;//托盘发运单据界面
  public IsShow: number = 0;//当前界面
  //发运首界面
  public loadId: string = '';//装车牌
  public truckId: string = '';//车次id
  public truckCode: string = '';//车次编号
  public driverName: string = '';//司机名称
  public driverPhone: string = '';//司机电话
  public plateNumber: string = '';//车牌号
  public truckHeaderWaybill: string = '';//运单编号
  public whId: String = '';
  public ShipmenArray: any = [];//装车集合
  public LpnCode: string = '';//容器编码  

  public model: any;
  constructor(public navCtrl: NavController, public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public modalCtrl: ModalController,
    public barcodeService: BarcodeService,
    public events: Events,
    public ngZone: NgZone
  ) {
  }
  clearModel() {
    this.IsShow = 0;//当前界面
    this.loadId = '';//装车牌
    this.truckId = '';//车次id
    this.truckCode = '';//车次编号
    this.driverName = '';//司机名称
    this.driverPhone = '';//司机电话
    this.plateNumber = '';//车牌号
    this.ShipmenArray = [];//装车集合
    this.LpnCode = '';
    this.truckHeaderWaybill = '';
    this.whId = '';
  }
  ionViewDidLoad() {
    console.log('ionViewDidLoad ShipmentLpnPage');
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.TuoPan) {
        this.ngZone.run(() => {
          this.LpnCode = barcode;
          this.getSkuStock(this.LpnCode);
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
  * 装车牌回车事件
  */
  // truckCode_KeyDown(event) {
  //   if (event && event.keyCode == 13) {
  //     this.getTruckCode();
  //   }
  // }
  /**
  * 容器编码回车事件
  */
  LpnCode_Enter(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isEmpty(this.LpnCode)) {
        this.nativeService.showToast('请输入托盘或容器编码！');
        return;
      }
      this.getSkuStock(this.LpnCode);
    }
  }

  /**
   * 发运生成
   */
  nextBtnOk() {
    if (!this.whId) {
      this.nativeService.showToast('请先扫描运单信息！')
      return;
    }

    let param = {
      whId: this.whId,
      plateNumber: this.plateNumber,
      driverName: this.driverName,
      driverPhone: this.driverPhone,
      truckHeaderWaybill: this.truckHeaderWaybill
    }
    this.appService.httpRequest(Api.LoadingPDA + Api.soTruckHeaderSubmit, Method.Post, param, ContenType.Json, result => {
      this.nativeService.hideLoading();
      this.truckCode = result.truckCode;
      this.truckId = result.truckId;
      this.IsShow = this.ShipmentLpnView;
      //获取单据明细
      this.appService.httpRequest(Api.LoadingPDA + Api.Lodingdetail, Method.Get, { truckId: result.truckId }, '', result => {
        if (Utils.isNotEmpty(result)) {
          result.data.forEach(element => {
            if (this.checkScaned(element.lpnCode)) {
              return;
            }
            this.getSkuStockInfo(element.lpnCode);
          });
        }
      });
    });
  }
  /**
   * 检查是否重复扫描容器
   */
  checkScaned(lpnCode) {
    let arr = this.ShipmenArray.filter(x => x.stockPdaVO.lpnCode.toUpperCase() == lpnCode.toUpperCase());
    if (arr.length > 0) {
      this.nativeService.showToast('请不要重复扫描容器');
      return true;
    }
    return false;
  }
  getSkuStock(lpnCode) {
    if (this.checkScaned(lpnCode)) {
      return;
    }
    this.nativeService.showLoading();
    //获取物品库存简版
    this.appService.httpRequest(Api.LoadingPDA + Api.getSkuStock, Method.Post, 'LpnCode=' + lpnCode, ContenType.Form, result => {
      this.nativeService.hideLoading();
      // this.ShipmenArray.push(result.data);
      //添加发车明细
      let soTruckDetailList = [];
      soTruckDetailList.push({
        lpnCode: result.data.stockPdaVO.lpnCode,
        soBillId: result.data.soBillId,
        truckId: this.truckId,
        truckCode: this.truckCode
      });
      this.appService.httpRequest(Api.LoadingPDA + Api.saveSoTruckDetail, Method.Post, soTruckDetailList, ContenType.Json, result2 => {
        this.nativeService.hideLoading();
        this.ShipmenArray.push(result.data);
      });
    });
  }
  getSkuStockInfo(lpnCode) {
    this.nativeService.showLoading();
    //获取物品库存简版
    this.appService.httpRequest(Api.LoadingPDA + Api.getSkuStock, Method.Post, 'LpnCode=' + lpnCode, ContenType.Form, result => {
      this.ShipmenArray.push(result.data);
      this.nativeService.hideLoading();
    });
  }
  /**
  * 根据车牌号查询车次明细信息
  */
  getTruckDetailByPlateNumber() {
    this.nativeService.showLoading();
    let param = {
      driverName: this.driverName,
      driverPhone: this.driverPhone,
      plateNumber: this.plateNumber,
      truckHeaderWaybill: this.truckHeaderWaybill
    }

    this.appService.httpRequest(Api.LoadingPDA + Api.getTruckHeader, Method.Get, param, '', result => {
      this.nativeService.hideLoading();
      this.truckCode = result.truckCode;
      this.truckId = result.truckId;
      this.IsShow = this.ShipmentLpnView;
    });
  }
  exit() {
    if (this.IsShow == this.truckView) {
      this.navCtrl.pop();
    } else if (this.IsShow == this.ShipmentLpnView) {
      this.IsShow = this.truckView;
    }
  }
  btnOk() {
    if (this.IsShow == this.truckView) {
      if (Utils.isEmpty(this.loadId)) {
        this.nativeService.showToast('请输入装车牌!');
        return;
      }
      this.getTruckDetailByPlateNumber();
    } else if (this.IsShow == this.ShipmentLpnView) {
      this.saveSoTruckDetail();
    }
  }
  saveSoTruckDetail() {
    if (this.ShipmenArray.length == 0) {
      this.nativeService.showToast('请扫描发运托盘');
      return;
    }
    debugger
    let soTruckDetailList = [];
    this.ShipmenArray.forEach(element => {
      soTruckDetailList.push({
        lpnCode: element.stockPdaVO.lpnCode,
        soBillId: element.soBillId,
        truckId: this.truckId,
        truckCode: this.truckCode
      });
    });
    //发运
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.LoadingPDA + Api.SaveconfirmSo, Method.Post, 'truckId=' + this.truckId, ContenType.Form, result => {
      this.nativeService.hideLoading();
      this.nativeService.showToast('提交成功');
      this.clearModel();
    });
  }
  headSelected(item) {
    this.nativeService.showLoading();
    
    this.appService.httpRequest(Api.LoadingPDA + Api.getSkuStockDetailList, Method.Post, 'LpnCode=' + item.stockPdaVO.lpnCode, ContenType.Form, result => {
      this.nativeService.hideLoading();
      if (Utils.isNotEmpty(result)) {
        let profileModal2 = this.modalCtrl.create('SkuBillModal', { result: result.data });
        profileModal2.present();
      }
    });

  }
  /**
 * 删除扫描序列号
 * @param item 
 */
  removeItem(item) {

    this.appService.httpRequest(Api.LoadingPDA + Api.deleteTruckDetail, Method.Post,
      "truckId=" + this.truckId + "&truckCode=" + this.truckCode + "&lpnCode=" + item.stockPdaVO.lpnCode
      , ContenType.Form, result => {
        this.nativeService.hideLoading();
        if (result.code == 200) {
          this.ShipmenArray = this.ShipmenArray.filter(x => x != item);
        }
        this.nativeService.showToast(result.msg);
        this.IsShow = this.ShipmentLpnView;
      });




  }
  /**
 * 查询装车牌明细
 */
  keyEnter(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isEmpty(this.loadId)) {
        this.nativeService.showToast('请输入装车牌！');
        return;
      }
      this.nativeService.showLoading();
      let param = {
        loadId: this.loadId
      }
      this.appService.httpRequest(Api.LoadingPDA + Api.soRegisterDetail, Method.Get, param, ContenType.Form, result => {
        this.nativeService.hideLoading();
        this.ShipmenArray = [];
        if (Utils.isNotEmpty(result.data)) {
          this.plateNumber = result.data.plateNumber;
          this.driverName = result.data.driverName;
          this.driverPhone = result.data.driverPhone;
          this.loadId = result.data.loadId;
          this.whId = result.data.whId;
        } else {
          this.nativeService.showToast(result.msg);
        }
      });
    }
  }
}
