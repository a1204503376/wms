import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../utils/appConstant';
import { Utils } from '../../services/Utils';
import { NativeService } from "../../services/NativeService"
import { AppService } from "../../services/App.service";
import { SkuLotModel } from '../../models/SkuLotModel'
import { ScanModel } from '../../models/DataBase.Model'
import { BarcodeService } from '../../services/BarCodeService'

/**
 * Generated class for the CheckInBoxLabelTypePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-check-in-box-label-type',
  templateUrl: 'check-in-box-label-type.html',
})
export class CheckInBoxLabelTypePage {

  skuLotModel: SkuLotModel = new SkuLotModel();//批属性对象

  text: string;
  /**
   * 页面元素
   */
  skuCode: string;//物品编码

  skuName: string;//物品名称

  scanQty: number;//数量

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

  ComponentFlag: boolean = true;//组件显示

  EidtFlag: boolean = true;//是否可编辑
  qtyFlag : boolean = true;
  qtyDisabled: boolean = true;//数量是否可编辑

  locDisabled: boolean = false;//库位是否可编辑

  locCode: string = 'STAGE';//库位编码

  locCodeFlag: boolean = true;//库位显隐

  scanModel: ScanModel = new ScanModel();//箱码解析实体

  public isCopyLabel:boolean = false;
  @Output() private outer = new EventEmitter<string>();

  @Input() ruleCode: any;//扫描二维码实体
  @Input() asnBillNo: string;//扫描二维码实体

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public nativeService: NativeService,
    public appService: AppService,
    public barcodeService: BarcodeService
  ) {
    this.labelType = AppConstant.labelType;
    console.log('constructor CheckInBoxLabelTypePage');
  }


  ionViewDidLoad() {
    console.log('子组件---ionViewDidLoad---CheckInBoxLabelTypePage');
  }
  ionViewWillLeave() {
    console.log('子组件---ionViewWillLeave---CheckInBoxLabelTypePage');
  }
  /**
   * 初始化数据
   * @param scan 
   */
  changeRun(scan: ScanModel) {
    //解析条码规则
    if (Utils.isNotEmpty(scan)) {
      this.ComponentFlag = true;
      this.scanModel = scan;
      this.outer.emit('');
    }
  }
  /**
   * 数据初始化
   */
  clearModel() {
    this.skuCode = '';
    this.scanQty = 0;
    this.wsuName = '';
    this.instockDate = null;
    this.produceDate = null;
    this.typeCode = '';
    this.lotNumber = '';
    this.version = '';
    this.remarks = '';
    this.type = '';
    this.labelCode = '';
    this.locCode = 'STAGE';
    this.skuLotModel = new SkuLotModel();
    this.ComponentFlag = false;
  }
}
